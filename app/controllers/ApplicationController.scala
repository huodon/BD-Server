package controllers

import java.nio.file.{FileSystemException, Path}
import java.sql.SQLTimeoutException
import java.util.NoSuchElementException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import controllers.Auth._
import except.TemplateHasExists
import forms.{OrgAddVenuesForm, OrgSignInForm}
import model.slick.Tables
import model.slick.Tables.PublishedEventRow
import module.slick.SlickConfig
import org.postgresql.util.PSQLException
import play.api.Logger.{logger => LOG}
import play.api.cache.CacheApi
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.libs.Files.TemporaryFile
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc._
import service._
import scala.concurrent.duration.{Duration, _}
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}
import play.api.libs.json.JsValue


class ApplicationController @Inject()(cache: CacheApi,
                                      userService: UserService,
                                      eventService: EventService,
                                      orgService: OrgService,
                                      storeService: StoreService,
                                      messagesApi: MessagesApi)
  extends Controller {


  lazy val db = SlickConfig.dbConfig.db

  import slick.driver.PostgresDriver.api._

  private[this] def indexPayload(useCache: Boolean = false) = Future {

    def queryPublishedEventAndCache(): Option[Seq[Tables.PublishedEventRow]] = {
      val eventsQuery = Tables.PublishedEvent.take(9 * 5)
      val task = db.run(eventsQuery.result)
      if (useCache) {
        task.onSuccess { case seq if !seq.isEmpty => cache.set("event.hots", seq, 1.days) }
      }
      task.onComplete(dbioReport)
      Try(Await.result(task, 5.seconds)).toOption
    }

    def queryVenuesAndCache(): Option[Seq[Tables.VenuesRow]] = {
      val expr = Tables.Venues.take(50)
      val task = db.run(expr.result)
      if (useCache) {
        task.onSuccess { case seq if (!seq.isEmpty) => cache.set("venues.hots", seq, 1.days) }
      }
      task.onComplete(dbioReport)
      Try(Await.result(task, 5.seconds)).toOption
    }

    val cachedEvents = cache.get[Seq[Tables.PublishedEventRow]]("event.hots").orElse(queryPublishedEventAndCache())
    val cachedVenues = cache.get[Seq[Tables.VenuesRow]]("venues.hots").orElse(queryVenuesAndCache())

    (cachedEvents, cachedVenues)
  }


  def index = Action.async { implicit request =>
    // hot events

    Future {
      try {
        val v = Await.result(indexPayload(), 5.seconds)

        val events: Option[Seq[Tables.PublishedEventRow]] = v._1
        val venues: Option[Seq[Tables.VenuesRow]] = v._2

        if (events.isDefined && venues.isDefined) {


          if (events.get.size >= 9) {
            LOG.info("switch to dataful index")
            Ok(views.html.indexWithData(events.get, venues.get, Seq("")))
          } else {
            LOG.debug(s"index data too little: top events: ${events.get.size}, top venies: ${venues.get.size}")
            Ok(views.html.index())
          }
        } else {
          LOG.debug("event and venues is empty! using static home page")
          Ok(views.html.index())
        }
      } catch {
        case t: Throwable => {
          LOG.debug("homepage data not payload: " + t.getMessage)
          Ok(views.html.index())
        }
      }
    }
  }

  def uploadImage(file: FilePart[TemporaryFile]): String = {
    val meta = file.contentType
    val fileName = java.util.UUID.randomUUID().toString
    storeService.saveImage(fileName, meta)
    file.ref.moveTo(new java.io.File(sys.props("user.dir") + s"/public/upload/$fileName"))
    fileName
  }


  def uploadedImage(name: String) = Action.async {

    Future {
      val query = Tables.ImagesStore.filter(e => e.name === name)
      val imageTask: Future[Option[Tables.ImagesStoreRow]] = db.run(query.result.headOption)

      val image: Option[Tables.ImagesStoreRow] = Await.result(imageTask, 5.seconds)

      val result: Option[Result] = image.map { row =>
        val ty = row.meta.getOrElse("image/JPEG")
        try {
          val path: Path = java.nio.file.Paths.get(sys.props("user.dir") + s"/public/upload/$name")
          val buf: Array[Byte] = java.nio.file.Files.readAllBytes(path)
          Ok(buf).withHeaders("Content-Type" -> ty)
        } catch {
          case t: Throwable =>
            BadRequest("error") //.withHeaders("Content-Type" -> ty)
        }
      }

      result.getOrElse(BadRequest("error"))

    }
  }

  // GET /signin
  def signInPage() = Action.async { implicit request =>
    Future {
      Ok(views.html.login(None, None))
    }
  }

  // POST /signin
  def signIn = Action.async { implicit request =>
    Future {
      forms.UserSignInForm.form.bindFromRequest().fold(
        errorForm => {
          LOG.debug(s"forms error: $errorForm")
          Redirect(routes.ApplicationController.signInPage()).flashing("error" -> "login error!")
        },

        validForm => {

          try {

            if (request.session.get("captcha").get != validForm.captcha) {
              throw new Exception("password error")
            }


            val cacheKey = s"user.${validForm.name}"

            def findUser(): Option[Tables.UserRow] = {
              val queryExpr = Tables.User.filter(e => e.loginName === validForm.name)
              val task = db.run(queryExpr.result.headOption)
              task.onComplete {
                case Success(optUser) =>
                  LOG.info(s"User ${validForm.name} login suyccdess")
                  optUser.map { u => cache.set(cacheKey, u, 1.minutes) }
                case Failure(t) =>
                  LOG.error(s"User ${validForm.name} not found:  ${t.getMessage}")
              }

              val result: Option[Tables.UserRow] = Try {
                Await.result(task, 5.seconds)
              }.toOption.getOrElse(None)

              result
            }

            val maybeUser: Option[Tables.UserRow] = cache.get(cacheKey).orElse {
              findUser()
            }

            maybeUser match {
              case Some(u) =>
                if (u.hashedPassword != validForm.password) {
                  throw new Exception("Password error")
                }

                Redirect(routes.ApplicationController.userHome()).withCookies(Auth.authCookie(UserRole(u.loginName)))
              case None =>
                Redirect(routes.ApplicationController.signInPage()).flashing("error" -> "User not found")
            }

          } catch {
            case t: Throwable =>
              Redirect(routes.ApplicationController.signInPage()).flashing("error" -> t.getMessage)
          }
        })
    }
  }


  // TODO 验证码
  def signInOrg = Action.async { implicit request =>
    forms.OrgSignInForm.form.bindFromRequest().fold(
      invalidData => {
        LOG.error(s"Unit login failed: $invalidData")
        Future.successful(Ok("Error: " + invalidData.errors.head))
      },
      (validData: OrgSignInForm) => {
        Future {
          try {

            if (request.session.get("captcha").get != validData.captcha) {
              val msg = Symbol("Capture code error")
              LOG.error(s"${validData.email} $msg")
              throw new Exception(msg.toString())
            }

            val queryUser = Tables.Org.filter(e => e.email === validData.email)

            val task: Future[Option[Tables.OrgRow]] = db.run(queryUser.result.headOption)

            val orgEntity = Await.result(task, 5.seconds)

            orgEntity match {
              case Some(o) =>
                if (o.hashedPassword != validData.password) {

                  val message = ""
                  LOG.error(message)
                  sys.error(message)
                }
              case None =>
                val message = s"User ${validData.email} not found"
                LOG.error(message)
                sys.error(message)
            }


            LOG.debug(s"${validData.email} login success")
            Redirect(routes.ApplicationController.orgAdmin()).withCookies(Auth.authCookie(OrgRole(validData.email)))

          } catch {
            case t: Throwable =>
              LOG.error(t.getMessage)
              Redirect(routes.ApplicationController.signInPage())
                .discardingCookies(DiscardingCookie("token")).flashing("error" -> t.getMessage)
          }
        }
      }
    )
  }

  private def findOrgProfiles(email: String): Option[Tables.OrgProfilesRow] = {
    val queryExpr = Tables.OrgProfiles.filter(e => e.org === email)

    val result: Option[Tables.OrgProfilesRow] =
      Try {
        Await.result(db.run(queryExpr.result.headOption), 5.seconds)
      }.toOption.getOrElse(None)

    result
  }

  def orgSetProfiles() = Action.async(bodyParser = parse.multipartFormData) { implicit request =>

    Future {

      readAuthFromCookie(request) match {
        case Some(OrgRole(email: String)) =>
          forms.OrgProfilesForm.form.bindFromRequest().fold(
            err => {
              BadRequest(err.toString).flashing("error" -> err.errors.head.message)
            },
            success = ok => {


              def insertProfile(image: Option[String]) = {
                val a = Tables.OrgProfiles.map(e => (
                  e.org, e.name, e.address, e.tel, e.province, e.city, e.nature, e.code, e.leadername,
                  e.leaderid, e.personnelcount, e.tags, e.parent, e.image, e.safe, e.summary, e.orgType,
                  e.classify)) +=
                  (email, ok.name, Some(ok.address), ok.tel, ok.province, ok.city, Some(ok.nature),
                  ok.code, ok.leaderName, ok.leaderId, Some(ok.personnelCount), Some(ok.tags), ok.parent,
                  image, Some(ok.safe), Some(ok.summary), ok.orgType, ok.classify)

                db.run(a).onComplete {
                  case Success(t) => LOG.info("机构基本信息新增成功")
                  case Failure(t) => LOG.error(s"机构基本信息新增失败: ${t.getMessage}")
                }
              }

              def updateProfile(image: Option[String]) = {
                val a = Tables.OrgProfiles.map(e => (
                  e.org, e.name, e.address, e.tel, e.province, e.city,
                  e.nature, e.code, e.leadername, e.leaderid, e.personnelcount,
                  e.tags, e.image, e.safe,
                  e.summary, e.orgType, e.classify))
                        .update(email, ok.name, Some(ok.address), ok.tel,
                          ok.province, ok.city, Some(ok.nature), ok.code,
                          ok.leaderName, ok.leaderId, Some(ok.personnelCount),
                          Some(ok.tags), image, Some(ok.safe), Some(ok.summary),
                          ok.orgType, ok.classify)

                db.run(a).onComplete {
                  case Success(t) => LOG.info("机构基本信息更新成功")
                  case Failure(t) => LOG.error(s"机构基本信息更新失败: ${t.getMessage}")
                }
              }

              if (request.body.file("image").isDefined) {
                val name = uploadImage(request.body.file("image").get)
                LOG.debug(s"update profiles with image $name")
                findOrgProfiles(email) match {
                  case Some(p) =>
                    LOG.debug("some profile, update")
                    updateProfile(Some(name))
                  case None =>
                    LOG.debug("none profile, insert with image")
                    insertProfile(Some(name))
                }
              } else {
                LOG.debug("update profiles without image")
                findOrgProfiles(email) match {
                  case Some(p) => {
                    LOG.debug("some profile, update")
                    updateProfile(p.image)
                  }
                  case None => {
                    LOG.debug("none profile, insert without image")
                    insertProfile(None)
                  }
                }
              }


              Redirect(routes.ApplicationController.orgAdminSetting()).flashing("saveStatus" -> "OK")
            })
        case _ =>
          Redirect(routes.ApplicationController.signIn())
      }
    }
  }

  // GET signout
  def signOut = Action {
    implicit request =>
      Redirect(routes.ApplicationController.index()).discardingCookies(DiscardingCookie("token"))
  }


  def orgAdminPassword() = Action {
    implicit request =>
      readAuthFromCookie(request) match {
        case Some(OrgRole(email)) =>
          Ok(views.html.org.orgAdminPass(email, None))
        case _ =>
          Redirect(routes.ApplicationController.signIn()).flashing("error" -> "please re-signin")
      }
  }

  def orgChangePassword() = Action { implicit request =>
    try {

      forms.OrgPasswordForm.form.bindFromRequest().fold(
        err => {
          throw new Exception(s"form error: ${err.errors}")
        },
        ok => {
          if (request.session.get("captcha").isEmpty) {
            throw new Exception(s"please re-input captcha")
          }
          if (request.session.get("captcha").get != ok.captcha)
            throw new Exception(s"captcha error")

          readAuthFromCookie(request) match {
            case Some(OrgRole(email)) =>
              val expr = Tables.Org.filter(e => e.email === email)
                .map(e => e.hashedPassword)
                .update(ok.newPassword)

              val task = db.run(expr)
              val n = Await.result(task, 5.seconds)

              if (n == 1) {
                Redirect(routes.ApplicationController.orgAdminPassword()).flashing("success" -> "password updated")
              } else {
                Redirect(routes.ApplicationController.orgAdminPassword()).flashing("error" -> "password change failed")
              }

            case _ => Unauthorized
          }
        }
      )
    } catch {
      case t: Throwable =>
        Redirect(routes.ApplicationController.orgAdminPassword()).flashing("error" -> t.getMessage)
    }
  }

  def orgAdminIdVerified() = Action {
    implicit request =>
      readAuthFromCookie(request) match {
        case Some(OrgRole(email)) =>
          Ok(views.html.org.orgAdminAuth(email, None))
        case _ =>
          Redirect(routes.ApplicationController.signIn()).flashing("error" -> "please re-signin")
      }
  }

  def signUpOrg = Action.async { implicit request =>
    forms.OrgSignUpForm.form.bindFromRequest().fold(
      invalidForm => {
        LOG.debug(s"form error: ${invalidForm.toString}}")
        Future.successful(BadRequest(views.html.register(None, Some(invalidForm))))
      },

      validForm => {
        LOG.debug(s"unit signup: ${validForm.toString}}")
        Future {
          try {

            if (request.session.get("captcha").get != validForm.captcha) {
              throw new Exception("captcha error")
            }

            val n = Await.result(
              orgService.save(validForm), Duration(5, TimeUnit.SECONDS))
            require(n == 1)

            LOG.debug(s"${validForm.email} register success")
            //            Redirect(routes.ApplicationController.orgAdmin()).withCookies(Auth.authCookie(OrgRole(validForm.email)))
            Redirect(routes.ApplicationController.signUpSuccess())
              .flashing("org.signup.email" -> validForm.email)
              .withCookies(Auth.authCookie(OrgRole(validForm.email)))
          } catch {
            case t: PSQLException =>
              Redirect(routes.ApplicationController.signUp()).flashing("error" -> t.getServerErrorMessage.getDetail)
            case t: Throwable =>
              Redirect(routes.ApplicationController.signUp()).flashing("error" -> t.getMessage)
            case t: SQLTimeoutException =>
              Redirect(routes.ApplicationController.signUp()).flashing("error" -> t.getLocalizedMessage)
          }
        }
      }
    )
  }


  private def findUserByName(name: String): Option[Tables.UserRow] = {
    val queryExpr = Tables.User.filter(e => e.loginName === name)
    val task = db.run(queryExpr.result.headOption)
    task.onComplete {
      case Success(maybeUser) =>
        LOG.error(s"$name query success")
      case Failure(t) =>
        LOG.error(s"$name query failed")
    }

    Try(Await.result(task, 5.seconds)).toOption.getOrElse(None)
  }

  def findUserByMobile(mobile: String): Option[Tables.UserRow] = {
    val queryExpr = Tables.User.filter(e => e.mobile === mobile)
    val task = db.run(queryExpr.result.headOption)
    task.onComplete {
      case Success(maybeUser) =>
        LOG.error(s"$mobile query success")
      case Failure(t) =>
        LOG.error(s"$mobile query failed")
    }

    Try(Await.result(task, 5.seconds)).toOption.getOrElse(None)
  }

  def changeNotice(id: Int) = Action { implicit request =>

    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        forms.OrgPostNoticeForm.form.bindFromRequest().fold(
          err => {
            BadRequest
          },
          ok => {
            LOG.debug(s"change ${id} as ${ok.content}")
            val expr = Tables.OrgNotice.filter(e => e.org === email && e.id === id)
              .map(e => e.content)
              .update(ok.content)

            val task = db.run(expr)
            try {
              Await.result(task, 5.seconds)
              // FIXME 重定向到错误的位置
              Redirect(routes.ApplicationController.orgAdminNotice())
            } catch {
              case _: Throwable =>
                NotModified
            }
          }
        )
      case _ =>
        Redirect(routes.ApplicationController.orgAdminNotice())
    }

    Redirect( routes.ApplicationController.orgAdminNotice() )

  }


  // POST /signup
  def signUp = Action.async { implicit request =>

    Future {

      forms.UserSignUpForm.form.bindFromRequest().fold(
        invalidData => {
          Redirect(routes.ApplicationController.signUpPage())
            .flashing("error" -> s"${invalidData.errors.head}")
        },
        validData => {
          LOG.debug(s"Create user: $validData")
          try {

            val userByName = findUserByName(validData.name)
            val userByMobile = findUserByMobile(validData.mobile)

            if (userByName.isDefined) {
              cache.set(s"user.${validData.name}:${validData.mobile}", userByName.get)
              throw new Exception("User exists")
            }
            else if (userByMobile.isDefined) {
              cache.set(s"user.${validData.name}:${validData.mobile}", userByName.get)
              throw new Exception("User exists")
            }
            else {

            }





            val save = userService.save(validData)
            val n = Await.result(save, Duration(5, TimeUnit.SECONDS))
            LOG.debug("Storage user info: " + n)

            // Redirect(routes.ApplicationController.userHome()).withCookies(Auth.authCookie(UserRole(validData.name)))
            Redirect(routes.ApplicationController.signUpSuccess())
              .withCookies(Auth.authCookie(UserRole(validData.name)))
              .flashing("user.signup.name" -> validData.name, "user.signup.mobile" -> validData.mobile)
          }
          catch {
            case t: Throwable =>
              LOG.error(t.getMessage)
              Redirect(routes.ApplicationController.signIn()).flashing("error" -> t.getMessage)
          }
        }
      )


    }

  }

  // GET /signup
  def signUpPage() = Action {
    implicit request =>
      Ok(views.html.register(Some(forms.UserSignUpForm.form), None))
  }

  // GET /signupSuccess
  def signUpSuccess() = Action {
    implicit request =>
      Ok(views.html.registerSuccess())
  }

  // GET /forgotPassword
  def forgotPassword = Action.async {
    implicit request =>
      Future {
        Ok("TODO")
      }
  }

  def forgotPasswordPage() = Action.async {
    implicit request =>
      Future {
        Ok(views.html.forgetPasswordUser())
      }
  }

  // GET /event
  def event = Action.async {
    implicit request =>
      readAuthFromCookie(request) match {
        case Some(role) =>
          try {
            eventService.listPublishedEvent().map { pubEventList =>
              Ok(views.html.event(pubEventList, Some(role)))
            }
          } catch {
            case t: Throwable =>
              Future.successful(
                BadRequest(views.html.event(Seq[PublishedEventRow](), None))
                  .flashing("errpr" -> t.getMessage)
              )
          }
        case _ =>
          Future.successful(BadRequest(views.html.event(Seq[PublishedEventRow](), None)))
      }

  }

  // TODO read file upload doc! http://my.oschina.net/dongming/blog/51090
  def orgAddEventTemplate() = Action.async(bodyParser = parse.multipartFormData) { implicit request =>
    LOG.debug(s"${request.body}")
    Future {
      forms.OrgAddEventTemplateForm.form.bindFromRequest().fold(
        invalidForm => {
          LOG.error(s"Create template form failed: $invalidForm")
          Redirect(routes.ApplicationController.orgAdminEvents())
            .flashing("error" -> s"${invalidForm.errors.head.key}: ${invalidForm.errors.head.messages}")
        },
        validForm => {
          readAuthFromCookie(request) match {
            case Some(OrgRole(email)) =>
              LOG.debug(email)
              try {

                val existTemplate = Await.result(eventService.findTempleteByOrg(email), Duration(3, TimeUnit.SECONDS))

                if (existTemplate.map(_.name).contains(validForm.name))
                  throw TemplateHasExists("Template exists")

                // 处理文件上传
                request.body.file("image").foreach { (f: FilePart[TemporaryFile]) =>
                  LOG.debug(s"Process upload file: ${f.ref.file.getPath}<${f.ref.file.length()}>")
                  val meta = f.contentType
                  val fileName = java.util.UUID.randomUUID().toString

                  storeService.saveImage(fileName, meta)

                  f.ref.moveTo(new java.io.File(sys.props("user.dir") + s"/public/upload/$fileName"))
                  eventService.saveTemplate(email, fileName, validForm)
                }
                Redirect(routes.ApplicationController.orgAdminEvents()).flashing("success" -> "save success")
              } catch {
                case t: NoSuchElementException =>
                  LOG.debug(t.getClass.getName)
                  Redirect(routes.ApplicationController.signOut()).flashing("error" -> "Internal error")
                case t: TemplateHasExists =>
                  Redirect(routes.ApplicationController.orgAdminEvents()).flashing("saveState" -> t.getMessage)
                case t: FileSystemException =>
                  LOG.error(t.getReason)
                  Redirect(routes.ApplicationController.orgAdminEvents())
                    .flashing("error" -> s"Internal error: reason: ${t.getReason}, message: ${t.getMessage}")
              }
            case _ =>
              Redirect(routes.ApplicationController.signIn())
          }
        }
      )
    }
  }


  /**
   * ?
   * POST /event/pub
   * @return Result
   */
  def pubEvent() = Action.async { implicit request =>
    Future {
      readAuthFromCookie(request) match {
        case Some(OrgRole(email)) =>
          forms.PubEventForm.form.bindFromRequest().fold(
            err => {
              BadRequest("Error: " + err.errors.head.message)
            },
            ok => {
              try {
                val image: Option[String] = {
                  Try {
                    Await.result(
                      db.run(Tables.Event.filter(e => e.name === ok.template).map(_.image).result.headOption),
                      5.seconds)
                  }.toOption.getOrElse(None)
                }
                val pub = Tables.PublishedEvent
                  .map(e => (
                  e.org, // org: String
                  e.template, // template: String
                  e.name,
                  e.venues, // venues: String
                  e.startTime, // startTime: String
                  e.endTime, // endTime: String
                  e.peopleLimit, // peopleLimit: Int
                  e.counsellor, // counsellor: Option[String] = None
                  e.image, // image: Option[String] = None
                  e.originPrice, // originPrice: Int
                  e.currentPrice, // currentPrice: Int
                  e.insurancePrice, // insurancePrice: Int
                  e.priceExplain // priceExplain: String
                  )) +=(
                  email, ok.template, Some(ok.name), ok.venues, ok.startTime, ok.endTime,
                  ok.peopleLimit, Some(ok.counsellor), image,
                  ok.originPrice, ok.currentPrice, ok.insurancePrice, ok.priceExplain
                  )
                val changeEventStatusAction = Tables.Event.filter(e => e.name === ok.template).map(e => e.publishCount)
                db.run(changeEventStatusAction.result).onSuccess {
                  case seq =>
                    seq.head.map(n =>
                      db.run(Tables.Event.filter(e => e.name === ok.template)
                        .map(e => e.publishCount)
                        .update(Some(n + 1)))
                    )
                }
                db.run(pub)

                Redirect(routes.ApplicationController.orgAdminEvents()).flashing("success" -> "events has publised")
              } catch {
                case t: Throwable =>
                  BadRequest(t.getMessage)
              }
            }
          )
        case _ =>
          Redirect(routes.ApplicationController.signIn())
      }
    }
  }

  def editEvent(id: Int) = Action(bodyParser = parse.multipartFormData) { implicit request =>

    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>

        val expr = Tables.Event.filter(e => e.org === email && e.id === id)
        val task = db.run(expr.result)

        try {
          val result: Seq[Tables.EventRow] = Await.result(task, 5.seconds)
          result.headOption match {
            case Some(row) =>
              forms.OrgAddEventTemplateForm.form.bindFromRequest().fold(
                err => {
                  BadRequest(err.errors.toString())
                },
                ok => {
                  val image: Option[String] = request.body.file("image").map(uploadImage)
                  val apj = ok.allowParentsJoin == "on"
                  val nm = ok.needManager == "on"

                  val expr = sqlu"""
                        update "EVENT"
                        set
                          name = '#${ok.name}',
                          classify = '#${ok.classify}',
                          form = '#${ok.form}',
                          tag = '#${ok.tag}',
                          summary = '#${ok.summary}',
                          age_limit_max = #${ok.ageLimitMax},
                          age_limit_min = #${ok.ageLimitMin},
                          time_span = '#${ok.timeSpan}',
                          allow_parents_join = #$apj,
                          need_manager = #$nm,
                          notice = '#${ok.notice}',
                          safeinfo = '#${ok.safeInfo}'
                        where org = '#${email}' and id = #$id  """

                  val imgExpr = if (image.isDefined) {
                    sqlu"""update "EVENT"  set image='#${image.get}'  where org='#${email}' and id=#$id  """
                  } else {
                    sqlu"""update "EVENT"  set image=image  where org='#${email}' and id=#$id  """
                  }

                  val exprSeq = DBIO.seq(expr, imgExpr)

                  db.run(exprSeq)


                  Redirect(routes.ApplicationController.orgAdminEvents())
                })
            case None =>
              NotFound("event not found")
          }
        } catch {
          case t: Throwable =>
            LOG.error("edit event errors", t)
            NotModified
        }

      case _ =>
        Unauthorized
    }

    Redirect( routes.ApplicationController.orgAdminEvents() )
  }

  def topEvents() = Action { implicit request =>
    import slick.jdbc.GetResult._

    val query = sql""" select row_to_json(row) from (
        select id, name, classify, form, tag, image, summary,
          age_limit_min, age_limit_max, time_span, allow_parents_join,
          need_manager, notice, safeinfo, publish_count from "EVENT" limit 47) as row; """.as[String]

    val queryTyped = Tables.Event.take(47).map(e => (
      e.id,
      e.name,
      e.image,
      e.summary))

    val task = db.run(queryTyped.result)

    try {
      val result = Await.result(task, 5.seconds)

      val jsSeq = for (i <- result) yield Json.obj(
        "id" -> i._1,
        "title" -> i._2,
        "image" -> i._3,
        "summary" -> i._4
      )
      Ok(Json.toJson(jsSeq))
    } catch {
      case t: Throwable =>
        LOG.error(t.getMessage)
        BadRequest
    }
  }

  def deleteVenues(id: Int) = Action.async { implicit request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        val expr = Tables.Venues.filter(e => e.id === id && e.org === email)
        val task = db.run(expr.delete)
        task.onComplete(dbioReport)
        task.map(n => Ok)
      case _ =>
        Future.successful(NonAuthoritativeInformation)
    }
  }

  def addVenues() = Action.async(bodyParser = parse.multipartFormData) { implicit request =>

    Future {
      readAuthFromCookie(request) match {
        case Some(OrgRole(email)) =>
          OrgAddVenuesForm.form.bindFromRequest().fold(
            err => {
              val errInfo = err.errors.toString()
              LOG.error(errInfo)
              Redirect(routes.ApplicationController.orgAdminVenues()).flashing("error" -> "form error")
            },
            ok => {
              LOG.debug(s"$email added: $ok")

              def insertVenues(image: Option[String]) = {
                // FIXME point type support!
                val queryExpr = Tables.Venues.map(e => (e.org, e.name, e.area, e.cap, e.image, e.by, e.location)) +=
                  (email, ok.name, Some(ok.area), Some(ok.capability), image, Some(ok.belong), Some(ok.location))

                val task = db.run(queryExpr)
                task.onComplete(dbioReport)
                task
              }

              def readyExist(): Boolean = {
                val queryExpr = Tables.Venues.filter(e => e.org === email).map(e => e.name)
                val task = db.run(queryExpr.result)
                val result: Seq[String] = Await.result(task, 5.seconds)
                result.contains(ok.name)
              }

              LOG.debug(s"${request.body.file("image")}")

              try {
                if (!readyExist()) {
                  val imageFile = request.body.file("image")
                  val imageName: Option[String] = imageFile.map(uploadImage)
                  if (imageFile.isDefined) {
                    val task = insertVenues(imageName)
                    Await.result(task, 5.seconds)
                  } else {
                    throw new Exception("please select venues image")
                  }
                  Redirect(routes.ApplicationController.orgAdminVenues()).flashing("success" -> "added")
                } else {
                  throw new Exception("venues exists")
                }

              } catch {
                case t: Throwable =>
                  LOG.error(t.getMessage)
                  Redirect(routes.ApplicationController.orgAdminVenues()).flashing("error" -> t.getMessage)
              }
            }
          )
        case _ =>
          Redirect(routes.ApplicationController.signIn())
      }
    }
  }

  /*
  def addCertificate() = Action(bodyParser = parse.multipartFormData) { implicit request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        request.body.file("image") match {
          case Some(f) =>
            val fileName = uploadImage(f)
            val insertExpr = Tables.OrgCert.map(e => e) += Tables.OrgCertRow(Some(email), fileName)
            db.run(insertExpr)

            Ok(fileName)
          case None =>
            BadRequest
        }
      case _ =>
        NonAuthoritativeInformation
    }
  }*/
  //AJAX返回错误修改 BY LIUHUAN
  def addCertificate() = Action(bodyParser = parse.multipartFormData) { implicit request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        request.body.file("image") match {
          case Some(f) =>
            val fileName = uploadImage(f)
            val insertExpr = Tables.OrgCert.map(e => e) += Tables.OrgCertRow(Some(email), fileName)
            db.run(insertExpr)
            val re = "{\"error\":\"\",\"msg\":\"" + fileName + "\"}";
            Ok(re).as("text/html")
          case None =>
            BadRequest
        }
      case _ =>
        NonAuthoritativeInformation
    }
  }

  def addObj() = Action(bodyParser = parse.multipartFormData) { implicit request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        request.body.file("image") match {
          case Some(f) =>
            val fileName = uploadImage(f)
            val insertObj = Tables.InfoMeta.map(e => e) += Tables.InfoMetaRow(fileName, Some(email), Some("slider"), None)
            db.run(insertObj)
            val re = "{\"error\":\"\",\"msg\":\"" + fileName + "\"}";
            Ok(re).as("text/html")
          case None =>
            BadRequest
        }
      case _ =>
        NonAuthoritativeInformation
    }
  }

  def findOrgCertificate(name: String) = Action { request =>

    val orgExpr = Tables.Org.filter(e => e.email === name)

    try {
      val org: Seq[Tables.OrgRow] = Await.result(
        db.run(orgExpr.result),
        5.seconds)

      val certExpr = Tables.OrgCert.filter(e => e.org === org.head.email).result

      val result: Seq[Tables.OrgCertRow] = Await.result(
        db.run(certExpr),
        5.seconds)

      Ok(Json.toJson(result.map(r => r.image)))
    } catch {
      case t: Throwable =>
        LOG.error("await  OrgRow errors", t)
        InternalServerError
    }
  }

  def findObj(name: String, ttype: String) = Action { request =>

    val orgExpr = Tables.Org.filter(e => e.email === name)
    val org: Seq[Tables.OrgRow] = Await.result(
      db.run(orgExpr.result),
      5.seconds)
    val info = Tables.InfoMeta.filter(e => e.obj === org.head.email && e.ext === ttype).result

    val result: Seq[Tables.InfoMetaRow] = Await.result(
      db.run(info),
      5.seconds)

    Ok(Json.toJson(result.map(r => r.name)))
  }

  def deletedCertificate(name: String) = Action { request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        val query = Tables.OrgCert.filter(e => e.image === name).delete
        //val query = Tables.ImagesStore.filter(e => e.name === name).delete
        val task = db.run(query)
        try {
          Await.result(task, 5.seconds)
          Ok
        } catch {
          case t: Throwable =>
            LOG.error(t.getMessage)
            BadRequest
        }
      case _ =>
        NonAuthoritativeInformation
    }
  }

  def deleteObj(name: String, ttype: String) = Action { request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        val query = Tables.InfoMeta.filter(e => e.name === name && e.ext === ttype).delete
        val task = db.run(query)
        try {
          Await.result(task, 5.seconds)
          Ok
        } catch {
          case t: Throwable =>
            LOG.error(t.getMessage)
            BadRequest
        }
      case _ =>
        NonAuthoritativeInformation
    }
  }

  // GET /event/:id
  def eventView(id: Int) = Action.async {
    implicit request =>
      Future {

        try {
          val exist =  Await.result(
                          db.run(Tables.PublishedEvent
                            .filter(e => e.id === id)
                            .exists.result),
                          3.seconds)

          if (!exist)
            throw new Exception("event not found")
          
          // TODO

          //          Ok(publishedEvent.toString)
          Ok(views.html.eventView(None, None, None))
        } catch {
          case t: Throwable =>
            BadRequest(t.getMessage)
        }
      }
  }

  def orgAddManager(name: String) = Action { request =>

    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>

        val existsExpr = Tables.OrgManager.filter(e => e.org === email).map(e => e.user)

        val existsResult: Seq[Option[String]] = Await.result(db.run(existsExpr.result), 5.seconds)

        if (existsResult.exists(e => e.get == name)) {
          LOG.debug("org manager has exists")
          BadRequest
        } else {

          val user = Tables.User.filter(e => e.loginName === name)
          val task = db.run(user.result.headOption)
          val result: Option[Tables.UserRow] = Try {
            Await.result(task, 5.seconds)
          }.toOption.getOrElse(None)

          result match {
            case Some(u) =>
              val insert = Tables.OrgManager.map(e => e) += Tables.OrgManagerRow(Some(email), Some(u.loginName))
              val task = db.run(insert)
              try {
                Await.result(task, 5.seconds)
                LOG.debug("org manager added success")
                Ok
              } catch {
                case t: Throwable =>
                  LOG.error(t.getMessage)
                  BadRequest
              }
            case None =>
              NotFound
          }
        }

      case _ =>
        NonAuthoritativeInformation
    }
  }

  def orgDeleteManager(name: String) = Action { request =>

    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>

        val expr = Tables.OrgManager.filter(e => e.org === email && e.user === name).delete
        val task = db.run(expr)
        try {
          val result: Int = Await.result(task, 5.seconds)
          if (result == 0) {
            NotModified
          } else {
            Ok
          }
        } catch {
          case t: Throwable =>
            LOG.error(t.getMessage)
            RequestTimeout
        }

      case _ =>
        Unauthorized
    }
    Ok
  }


  def findOrgManager() = Action { request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        val expr = Tables.OrgManager.filter(e => e.org === email)
        val task: Future[Seq[Tables.OrgManagerRow]] = db.run(expr.result)

        try {
          val result: Seq[Tables.OrgManagerRow] = Await.result(task, 5.seconds)
          val users: Seq[String] = result.map(r => r.user.get)

          val userSet: Seq[Tables.UserRow] =
            for (i <- users) yield {
              val findUser = Tables.User.filter(e => e.loginName === i)
              val findUserTask = db.run(findUser.result)
              Await.result(findUserTask, 5.seconds).head
            }

          val jsArr =
            for (row <- userSet) yield Json.obj(
              "id" -> row.id,
              "name" -> row.loginName,
              "avatar" -> row.avatar
            )

          Ok(Json.toJson(jsArr))
        } catch {
          case t: Throwable =>
            LOG.error(t.getMessage)
            BadRequest
        }
      case _ =>
        Unauthorized
    }
  }

  def joinEvent(id: Int) = Action { implicit request =>
    /* TODO
    readAuthFromCookie(request) match {
      case Some(UserRole(name)) =>
        sql"""
             with users as (select * from "USER_EVENT" where username = #$name),

           """
        Tables.UserEventLittle.map(e => e) += Tables.UserEventLittleRow()

      case _ =>
        Redirect(routes.ApplicationController.eventView(id))
          .discardingCookies(DiscardingCookie("token"))
          .flashing("error" -> "请先登陆")
    }
  */
    NotImplemented
  }


  def evolution() = Action { implicit request =>
    Ok(views.html.grow())
  }

  // ?

  def org() = Action { implicit request =>
    //Ok(views.html.org.org())
    Ok("TODO")
  }

  def orgBy(id: Int) = Action {implicit request =>
        val a = Tables.Org.filter(m => m.id === id).take(1)
        val run = db.run(a.result).map { v =>
          if (v.isEmpty) None else Some(v)
        }
        val orgs: Option[Seq[Tables.OrgRow]] = Await.result(run, 5.seconds)
        
        val b = Tables.OrgProfiles.filter(m => m.org === orgs.get(0).email).take(1)
        val runProfile = db.run(b.result).map { v =>
          if (v.isEmpty) None else Some(v)
        }
        val profiles: Option[Seq[Tables.OrgProfilesRow]] = Await.result(runProfile, 5.seconds)
        
        
        val info = Tables.InfoMeta.filter(e => e.obj === orgs.get(0).email && e.ext === "slider").take(5)
        val slider = db.run(info.result).map { v =>
          if (v.isEmpty) None else Some(v)
        }
        val sliders: Option[Seq[Tables.InfoMetaRow]] = Await.result(slider, 5.seconds)
      
        Ok(views.html.org.org(orgs,profiles,sliders))
  }

  def orgAbout(id: Int) = Action { implicit request =>
    Ok(views.html.org.orgAbout())
  }

  def orgReview(id: Int) = Action { implicit request =>
    Ok(views.html.org.orgReview())
  }

  def orgReviewView(event: Int, view: Int) = Action { implicit request =>
    Ok(views.html.org.orgReviewView())
  }


  def orgNotice(id: Int) = Action { implicit request =>
    Ok(views.html.org.orgNotice())
  }

  def orgDiary(id: Int) = Action { implicit request =>
    Ok(views.html.org.orgDiary())
  }

  def orgInstructor(id: Int) = Action { implicit request =>
    Ok(views.html.org.orgInstructor())
  }

  def orgDiaryView(orgId: Int, diaryId: Int) = Action { implicit request =>

    Ok(views.html.org.orgDiaryView())
  }

  def orgAdmin() = Action.async { implicit request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        try {
          eventService
            .findPublishedEventByOrg(email)
            .map(e => Ok(views.html.org.orgAdmin(email, e)))
        } catch {
          case t: Throwable =>
            LOG.error("find published event errors", t)
            Future.successful(BadRequest)
        }

      case _ =>
        Future.successful(
          Redirect(routes.ApplicationController.signUp()))
    }
  }

  def orgAdminSetting() = Action.async { implicit request =>


    Future {
      readAuthFromCookie(request) match {
        case Some(OrgRole(email)) =>

          val profiles: Option[Tables.OrgProfilesRow] = findOrgProfiles(email)

          Ok(views.html.org.orgAdminSetting(email, profiles))

        case _ =>
          Redirect(routes.ApplicationController.signIn())
      }
    }
  }


  def findVenues(email: String, limit: Int = 30): Option[Seq[Tables.VenuesRow]] = {

    val expr = Tables.Venues.filter(e => e.org === email)
    val task = db.run(expr.result)
    task.onComplete(dbioReport)
    Try {
      Await.result(task, 5.seconds)
    }.toOption
  }

  def orgAdminVenues() = Action { implicit request =>

    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        val venues = findVenues(email)
        venues match {
          case Some(v: Seq[Tables.VenuesRow]) =>
            Ok(views.html.org.orgAdminVenues(email, Some(v)))
          case None =>
            Ok(views.html.org.orgAdminVenues(email, None))
        }

      case _ => Redirect(routes.ApplicationController.signIn())
    }
  }

  def orgAdminLetter() = Action { implicit request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        val letters = findLetters(email)
        letters match {
          case Some(v: Seq[Tables.OrgLetterRow]) =>
            Ok(views.html.org.orgAdminLetter(email, Some(v)))
          case None =>
            Ok(views.html.org.orgAdminLetter(email, None))
        }
      case _ =>
        Redirect(routes.ApplicationController.signIn())
    }
  }

  private def findLetters(email: String, limit: Int = 50): Option[Seq[Tables.OrgLetterRow]] = {

    val expr = Tables.OrgLetter.filter(e => e.to === email).sortBy(_.id.desc.nullsFirst)
    val task = db.run(expr.result)
    task.onComplete(dbioReport)
    Try {
      Await.result(task, 10.seconds)
    }.toOption
  }

  def orgAdminNotice() = Action { implicit request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        val a = Tables.OrgNotice.filter(m => m.org === email).take(20)
        val notices = Await.result(db.run(a.result), 5.seconds)
        val run = db.run(a.result).map { v =>
          if (v.isEmpty) None else Some(v)
        }

        val values: Option[Seq[Tables.OrgNoticeRow]] = Await.result(run, 5.seconds)

        Ok(views.html.org.orgAdminNotice(email, values))
      case _ =>
        Redirect(routes.ApplicationController.signIn())
    }
  }


  def orgPostNotice() = Action.async { implicit request =>

    Future {
      readAuthFromCookie(request) match {

        case Some(OrgRole(email)) =>
          forms.OrgPostNoticeForm.form.bindFromRequest().fold(
            err => {
              BadRequest(err.toString)
            },
            ok => {

              LOG.debug(ok.toString)

              val q = Tables.OrgNotice.map(e => (e.org, e.content, e.created)) +=
                (email, ok.content, java.sql.Date.valueOf(java.time.LocalDate.now()))
              val run = db.run(q)

              run.onFailure {
                case t: Throwable => LOG.error(t.getMessage)
              }

              Redirect(routes.ApplicationController.orgAdminNotice())
            }
          )
        case _ =>
          Redirect(routes.ApplicationController.signIn())
      }
    }
  }


  def orgDeleteNotice(id: Int) = Action.async { implicit request =>

    Future {
      readAuthFromCookie(request) match {
        case Some(OrgRole(mail)) =>
          val rm = Tables.OrgNotice.filter(e => e.org === mail && e.id === id).delete
          db.run(rm)
          Redirect(routes.ApplicationController.orgAdminNotice())
        case _ =>
          Redirect(routes.ApplicationController.signIn())
      }
    }
  }


  def orgEditCounsellor(id: Int, enable: Boolean) = Action { implicit request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        val expr = Tables.OrgCounsellor.filter(e => e.id === id)
          .map(e => e.enabled)
          .update(Some(enable))

        val task = db.run(expr)
        try {
          Await.result(task, 5.seconds)
          Redirect(routes.ApplicationController.orgAdminCounsellor())
        } catch {
          case _: Throwable =>
            NotModified
        }


        val nocounsellor = findOrgCounsellor(email, Some(false), 10);
        val yescounsellor = findOrgCounsellor(email, Some(true), 10);
        Ok(views.html.org.orgAdminCounsellor(email, nocounsellor, yescounsellor))
      case _ =>
        Redirect(routes.ApplicationController.signIn())
    }
  }

  def orgUEditCounsellor(id: Int, index: Int, show: Boolean) = Action { implicit request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        val expr = Tables.OrgCounsellor.filter(e => e.id === id)
          .map(e => (e.show, e.index))
          .update(Some(show), Some(index))
        val task = db.run(expr)

        val nocounsellor = findOrgCounsellor(email, Some(false), 10);
        val yescounsellor = findOrgCounsellor(email, Some(true), 10);
        Ok(views.html.org.orgAdminCounsellor(email, nocounsellor, yescounsellor))
      case _ =>
        Redirect(routes.ApplicationController.signIn())
    }
  }

  def orgAdminCounsellor() = Action { implicit request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        val nocounsellor = findOrgCounsellor(email, Some(false), 10);

        val yescounsellor = findOrgCounsellor(email, Some(true), 10);
        Ok(views.html.org.orgAdminCounsellor(email, nocounsellor, yescounsellor))
      case _ =>
        Redirect(routes.ApplicationController.signIn())
    }
  }


  def addEventReview() = Action(bodyParser = parse.multipartFormData) { implicit request =>

    def bindFormAndInsertData() = {
      forms.OrgAddEventReviewForm.form.bindFromRequest().fold(
        err => {
          BadRequest(err.errors.toString)
        },
        ok => {

          val image: Option[String] = request.body.file("image").map(uploadImage)

          val expr = Tables.EventReview.map(e => (e.name, e.content, e.relationEvent, e.stayTop, e.image)) +=
            (ok.name, ok.content, ok.relationEvent, ok.stayTop, image)
          val task: Future[Int] = db.run(expr)

          try {

            val n = Await.result(task, 5.seconds)
            require(n > 0)

            Redirect(routes.ApplicationController.orgAdminEvents())

          } catch {
            case t: Throwable =>
              LOG.error("add event review error", t)
              InternalServerError
          }
        }
      )
    }

    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        bindFormAndInsertData()
      case _ =>
        Unauthorized
    }




    Ok
  }

  /**
   *
   */
  def findOrgCounsellor(org: String, enable: Option[Boolean], limit: Int = 50): Option[Seq[Tables.OrgCounsellorRow]] = {
    val expr = Tables.OrgCounsellor.filter(e => e.org === org && e.enabled === enable).sortBy(_.id.desc.nullsFirst)
    val task = db.run(expr.result)
    task.onComplete(dbioReport)
    Try {
      Await.result(task, 10.seconds)
    }.toOption
  }

  private def dbioReport[_]: PartialFunction[Try[_], Unit] = {
    case Success(v) =>
      LOG.info("Success: " + v.toString)
    case Failure(t) =>
      LOG.error("Failure: " + t.getMessage)
  }

  private def findOrgByEmail(email: String): Option[Tables.OrgRow] = {
    val expr = Tables.Org.filter(e => e.email === email)
    val task = db.run(expr.result)
    task.onComplete(dbioReport)

    Try {
      Await.result(task, 5.seconds).head
    }.toOption
  }

  private def findUserInCounsellor(email: String, name: String): Option[Tables.OrgCounsellorRow] = {
    val expr = Tables.OrgCounsellor.filter(e => e.org === email && e.name === name)
    val task = db.run(expr.result)
    task.onComplete(dbioReport)
    Try {
      Await.result(task, 5.seconds).head
    }.toOption
  }

  def orgAddCounsellor() = Action.async { implicit request =>
    Future {
      readAuthFromCookie(request) match {
        case Some(OrgRole(email)) =>
          forms.OrgAddCounsellorForm.form.bindFromRequest().fold(
            err => {
              val lang = Lang.defaultLang
              implicit val m = Messages(lang, messagesApi)
              BadRequest(err.errorsAsJson).as("application/JSON")
            },
            ok => {

              def addCounsellor(name: String) = {
                val expr = Tables.OrgCounsellor.map(e => (e.org, e.name)) +=(email, name)
                val task = db.run(expr)
                task.onComplete(dbioReport)
                task
              }

              val maybeUser = findUserByName(ok.name)

              try {
                maybeUser match {
                  case Some(u: Tables.UserRow) =>
                    if (findUserInCounsellor(email, ok.name).isDefined) {
                      throw new Exception("User added")
                    } else {
                      addCounsellor(u.loginName)
                    }
                  case None =>
                    throw new Exception("User not found")
                }
                Ok
              } catch {
                case t: Throwable =>
                  LOG.error(t.getMessage)
                  BadRequest
              }
            })


        case _ =>
          Redirect(routes.ApplicationController.signIn())
      }
    }

  }

  def getEvents(email: String, limit: Int = 50): Option[Seq[Tables.EventRow]] = {

    val cacheKey = s"org.$email.events"

    def queryInDB() = {
      val queryExpr = Tables.Event.filter(e => e.org === email).take(limit)
      val task = db.run(queryExpr.result).map { v => if (v.isEmpty) None else Some(v) }
      task.onComplete {
        case Success(optSeq: Option[Seq[Tables.EventRow]]) =>
          LOG.info(s"find venues ${email} events")
          optSeq foreach (seq => cache.set(cacheKey, seq, 10.seconds))
        case Failure(t) =>
          LOG.error(s"venues ${email} events not found")
      }

      Try {
        Await.result(task, 5.seconds)
      }.toOption.getOrElse(None)
    }

    cache.get[Seq[Tables.EventRow]](cacheKey).orElse(queryInDB())
  }

  def getPublishedEvent(email: String, limit: Int = 50, expiration: Option[Duration] = None) = {

    val cacheKey = s"org.$email,published_event"

    cache.get[Seq[Tables.PublishedEventRow]](cacheKey).orElse {

      val queryExpr = Tables.PublishedEvent.filter(e => e.org === email).take(limit)
      val task = db.run(queryExpr.result) map { seq => if (seq.isEmpty) None else Some(seq) }

      task.onComplete {
        case Success(optSeq: Option[Seq[Tables.PublishedEventRow]]) =>
          LOG.info(s"found venues $email published events")
          optSeq foreach { seq => cache.set(cacheKey, seq, expiration.getOrElse(10.seconds)) }
        case Failure(t) =>
          LOG.error(s"venues $email published events not found ${t.getMessage}")
      }

      val result: Option[Seq[Tables.PublishedEventRow]] = Try {
        Await.result(task, 5.seconds)
      }.toOption.getOrElse(None)

      result
    }
  }

  private def findAllEvent(email: String): Option[Seq[Tables.EventRow]] = {
    val expr = Tables.Event.filter(e => e.org === email)
    val task = db.run(expr.result)

    Try {
      Await.result(task, 5.seconds)
    }.toOption
  }

  private def findAllPublishedEvent(email: String): Option[Seq[Tables.PublishedEventRow]] = {
    val expr = Tables.PublishedEvent.filter(e => e.org === email)
    val task = db.run(expr.result)
    Try {
      Await.result(task, 5.seconds)
    }.toOption
  }

  def deleteEvent(name: String) = Action { request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        db.run(Tables.Event.filter(e => e.org === email && e.name === name).delete)
        Ok
      case _ =>
        NonAuthoritativeInformation
    }
  }

  def orgAdminEvents(edit: String) = Action {
    implicit request =>

      readAuthFromCookie(request) match {

        case Some(OrgRole(email)) =>

          try {

            val events = findAllEvent(email)
            val venues = findVenues(email)
            val publishedEvents = findAllPublishedEvent(email)
            val strategies = findAllStrategys(email);
            Ok(views.html.org.orgAdminEvent(email, events, publishedEvents, venues, strategies))
            //            LOG.debug(s"orgAdminActivity: $templates $pubed")
            //            if (edit != null) {
            //              LOG.debug("edit mode")
            //              val e: Tables.EventRow = Await.result(
            //                eventService.findTempleteByName(edit),
            //                Duration(3, TimeUnit.SECONDS))
            //
            //              Ok(views.html.org.orgAdminEvent(email, Some(templates), Some(pubed), venues, Some(e)))
            //            } else {
            //              Ok(views.html.org.orgAdminEvent(email, Some(templates), Some(pubed), venues, None))
            //            }


          } catch {
            case t: Throwable =>
              BadRequest(views.html.org.orgAdminEvent(email, None, None, None, None))
          }
        case _ =>
          Redirect(routes.ApplicationController.signIn()).flashing("error" -> "please re-signin")
      }
  }


  def orgAdminStatistics() = Action {
    implicit request =>
      readAuthFromCookie(request) match {
        case Some(OrgRole(email)) =>
          Ok(views.html.org.orgAdminStatistics(email))
        case _ =>
          Redirect(routes.ApplicationController.signIn()).flashing("error" -> "please re-signin")
      }
  }

  def orgAdminStatisticsMore() = Action {
    implicit request =>
      readAuthFromCookie(request) match {
        case Some(OrgRole(email)) =>
          Ok(views.html.org.orgAdminStatisticsAll(email))
        case _ =>
          Redirect(routes.ApplicationController.signIn())
      }
  }

  def schoolBy(id: Int) = play.mvc.Results.TODO

  def schoolAdmin() = play.mvc.Results.TODO

  def venues() = play.mvc.Results.TODO

  def venuesBy(id: Int) = play.mvc.Results.TODO

  def userFavorites() = Action.async {
    implicit request =>
      Future {
        val role: Option[Role] = readAuthFromCookie(request)
        role.map {
          case u@UserRole(name) => Ok(views.html.users.useradminFavorites())
          case e@_ => Unauthorized
        }.getOrElse(Redirect(routes.ApplicationController.signIn()))
      }
  }

  def userEvent() = Action.async {
    implicit request =>
      Future {
        val user: Option[Role] = readAuthFromCookie(request)
        user.map {
          case UserRole(userName) => Ok(views.html.users.userAdminEvent())
          case OrgRole(email) => Ok(views.html.login(None, None)).discardingCookies(DiscardingCookie("token"))
        }.getOrElse(Redirect(routes.ApplicationController.signIn()))
      }
  }

  def userEventInstructor() = Action.async {
    implicit request =>
      Future {
        val user: Option[Role] = readAuthFromCookie(request)
        user.map {
          case UserRole(userName) => Ok(views.html.users.userAdminEventInstructor())
          case OrgRole(iemail) => Ok(views.html.users.userAdminEventInstructor())
        }.getOrElse(Redirect(routes.ApplicationController.signIn()))
      }
  }

  def userEventInstructorTable() = Action.async {
    implicit request =>
      Future {
        val user: Option[Role] = readAuthFromCookie(request)
        user.map {
          case UserRole(userName) => Ok(views.html.users.userAdminEventInstructorTable())
          case OrgRole(iemail) => Ok(views.html.users.userAdminEventInstructorTable())
        }.getOrElse(Redirect(routes.ApplicationController.signIn()))
      }
  }

  def userFootprint() = Action.async {
    implicit request =>
      Future {
        readAuthFromCookie(request).map {
          case UserRole(userName) => Ok(views.html.users.userAdminFootprint())
          case OrgRole(email) => Ok(views.html.users.userAdminFootprint())
        }.getOrElse(Redirect(routes.ApplicationController.signIn()))
      }
  }

  def userFriends() = Action.async {
    implicit request =>
      Future {
        val user: Option[Role] = readAuthFromCookie(request)
        user.map {
          case UserRole(userName) => Ok(views.html.users.userAdminFriends())
          case OrgRole(email) => Ok(views.html.users.userAdminFriends())
        }.getOrElse(Redirect(routes.ApplicationController.signIn()))
      }
  }

  def userLetter() = Action {
    implicit request =>
      Ok(views.html.users.useradminLetter())
  }

  def userTask() = Action.async {
    implicit request =>
      Future {
        readAuthFromCookie(request).map {
          case UserRole(userName) => Ok(views.html.users.useradminTask())
          case OrgRole(email) => Ok(views.html.users.useradminTask())
        }.getOrElse(Redirect(routes.ApplicationController.signIn()))
      }
  }

  def userSet() = Action.async {
    implicit request =>
      Future {
        readAuthFromCookie(request).map {
          case UserRole(userName) => Ok(views.html.users.userAdminSetting())
          case OrgRole(email) => Ok(views.html.users.userAdminSetting())
        }.getOrElse(Redirect(routes.ApplicationController.signIn()))
      }
  }


  def userView(id: Int) = Action {
    implicit request =>

      Ok(views.html.user(id.toString))

  }


  def userHome() = Action.async {
    implicit request =>
      Future {
        readAuthFromCookie(request).map {
          case UserRole(userName) => Ok(views.html.users.useradmin())
          case OrgRole(email) => Ok(views.html.users.useradmin())
        }.getOrElse(Redirect(routes.ApplicationController.signIn()))
      }
  }


  def userCert() = Action.async {
    implicit request =>
      Future {
        readAuthFromCookie(request).map {
          case UserRole(userName) => Ok(views.html.users.useradminAuth())
          case OrgRole(email) => Ok(views.html.users.useradminAuth())
        }.getOrElse(Redirect(routes.ApplicationController.signIn()))
      }
  }

  def captcha() = Action.async {
    request =>
      Future {
        val text = service.CaptchaService.generateText()
        val image: Array[Byte] = service.CaptchaService.generateImage(text)
        Ok(image)
          .withHeaders("Content-Type" -> "image/PNG")
          .withSession(request.session +("captcha", text))
      }
  }

  //----------------------------------------------
  // ?
  // GET /forgotPasswordUser
  def forgotPasswordUserPage() = Action.async {
    implicit request =>
      Future {
        Ok(views.html.forgetPasswordUser())
      }
  }

  // POST /forgotPasswordUser
  def forgotPasswordUser() = TODO

  //----------------------------------------------
  // 机构忘记密码
  // GET forgotPasswordOrg
  def forgotPasswordOrgPage() = Action.async {
    implicit request =>
      Future {
        Ok(views.html.forgetPasswordOrg())
      }
  }

  def userAdminSetting() = Action {
    implicit request =>
      Ok(views.html.users.userAdminSetting())
  }

  // POST /forgotPasswordOrg
  def forgotPasswordOrg() = Action {

    Ok("TODO")

  }

  //----------------------------------------------
  // 活动攻略

  def strategy() = Action.async {
    implicit request =>
      Future {
        Ok(views.html.strategy())
      }
  }

  def strategyBy(id: Int) = Action.async {
    implicit request =>
      Future {
        Ok(views.html.strategyView())
      }
  }

  //----------------------------------------------
  // ?


  def grow(id: Int) = Action.async {
    implicit request =>
      Future {
        Ok(views.html.grow())
      }
  }

  def go(id: Int) = Action {
    implicit request =>
      Ok(views.html.org.orgAdminStatisticsDetail("demo"))
  }

  def test() = Action {
    implicit request =>
      Ok("TODO")
  }

  private def findAllStrategys(email: String): Option[Seq[Tables.StrategyRow]] = {
    val expr = Tables.Strategy.filter(e => e.org === email)
    val task = db.run(expr.result)
    Try {
      Await.result(task, 5.seconds)
    }.toOption
  }

  /**
   * ?
   * POST /strategy/pub
   * @return Result
   */
  def pubStrategy() = Action.async { implicit request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        try {
          eventService
            .findPublishedEventByOrg(email)
            .map(e => Ok(views.html.org.orgAdmin(email, e)))
        } catch {
          case t: Throwable =>
            LOG.error("find published event errors", t)
            Future.successful(BadRequest)
        }

      case _ =>
        Future.successful(
          Redirect(routes.ApplicationController.signUp()))
    }
  }
}

