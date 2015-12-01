package controllers

import javax.inject.Singleton

import controllers.Auth._
import model.slick.Tables
import module.slick.SlickConfig.dbConfig
import play.api.Logger.{logger => LOG}
import play.api.http.ContentTypes
import play.api.libs.json.Json
import play.api.mvc.Codec.utf_8
import play.api.mvc._
import slick.jdbc.GetResult._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

@Singleton
class APIController extends Controller {

  private lazy val db = dbConfig.db

  import slick.driver.PostgresDriver.api._
  def findEventByLocation(z: Int, x: Int, y: Int) = Action {

    Ok("[]")
  }

  def enableManager(name: String) = Action { request =>

    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        val action = Tables.OrgManager.filter(e => e.org === email && e.user === name)
          .map(e => e.enabled)
          .update(Some(true))

        val task: Future[Int] = db.run(action)

        try {
          val result = Await.result(task, 5.seconds)
          require(result > 0)

          Ok
        } catch {
          case t: Throwable =>
            LOG.error(t.getMessage)
            NotModified
        }
      case _ =>
        Unauthorized
    }
  }

  def topVenues() = Action {

    val expr = Tables.OrgProfiles.take(35)
    try {
      val task = db.run(expr.result)
      val result: Seq[Tables.OrgProfilesRow] = Await.result(task, 5.seconds)
      val objArr =
        for (i <- result) yield Json.obj(
          "id" -> i.id,
          "org" -> i.org,
          "name" -> i.name,
          "image" -> i.image,
          "address" -> i.address
        )

      Ok(Json.toJson(objArr))
    } catch {
      case t: Throwable =>
        LOG.error(t.getMessage)
        BadRequest
    }
  }

  def changeEventStatus(event: Int) = Action { implicit request =>

    Auth.readAuthFromCookie(request) match {

      case Some(OrgRole(email)) =>

        try {
          forms.EventStatusForm.form.bindFromRequest().fold(
            err => {
              LOG.error("Form: " + err.toString)
              BadRequest
            },
            ok => {
              LOG.debug("Form: " + ok.toString)
              val expr = Tables.PublishedEvent.filter(e => e.id === event)
                .map(e => (e.status, e.changeCause))
                .update(Some(ok.status), Some(ok.cause))

              val task = db.run(expr)
              val result: Int = Await.result(task, 5.seconds)
              require(result > 0)
              Redirect(routes.ApplicationController.orgAdminEvents())
            }
          )

        } catch {
          case t: Throwable =>
            LOG.error(t.getMessage)
            InternalServerError(t.getMessage)
        }
      case _ =>
        NonAuthoritativeInformation
    }
  }

  def getEventStatus(event: Int) = Action { request =>
    val expr = Tables.PublishedEvent.filter(e => e.id === event).map(e => e.status)

    try {
      val task = db.run(expr.result)
      val result: String = Await.result(task, 5.seconds).headOption.getOrElse(None).getOrElse("进行中")

      Ok(result)
    } catch {
      case t: Throwable =>
        LOG.error(t.getMessage)
        BadRequest
    }
  }


  def testSqlJson = Action { request =>
    val db = module.slick.SlickConfig.dbConfig.db

    val expr = {
      sql""" select row_to_json(row) from (select * from "EVENT") row """.as[String]
    }

    val task: Future[Vector[String]] = db.run(expr)
    val result = Await.result(task, 5.seconds)

    Ok(Json.toJson(result))
  }


  def getOrgProfiles = Action { request =>

    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        val expr = sql"""   select row_to_json(p) from "ORG_PROFILES" p where org = '#$email' limit 1;   """.as[String]
        val task = db.run(expr)

        try {
          val result = Await.result(task, 5.seconds).headOption

          result match {
            case Some(profiles: String) =>
              Ok(profiles).as(JSON)

            case None =>
              NotFound
          }

        } catch {
          case t: Throwable =>
            LOG.error(t.getMessage)
            BadRequest(t.getMessage)
        }
      case _ =>
        Unauthorized
    }
  }

  def activeEvent(id: Int, isActive: Boolean) = Action { request =>
    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>

        val action = Tables.Event
          .filter(e => e.org === email && e.id === id)
          .map(e => e.enabled)
          .update(Some(isActive))
        val expr = sql""" update "EVENT" set enabled = #$isActive where org='#$email' and id=#$id """.as[Int]
        LOG.debug(expr.statements.toString())
        val task = db.run(expr)

        try {
          val result = Await.result(task, 5.seconds).head
          LOG.debug(result.toString)
          require(result > 0)

          Ok
        } catch {
          case t: Throwable =>
            LOG.error(t.getMessage)
            NotModified
        }
      case _ =>
        Unauthorized
    }
  }


  def getEvent(id: Int) = Action { request =>

    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>
        val expr = sql""" select row_to_json(r) from "EVENT" r where id = #$id """.as[String]
        LOG.debug(expr.statements.toString())
        val task = db.run(expr)

        try {
          val result = Await.result(task, 5.seconds).headOption

          Ok(result.getOrElse("{}")).as(ContentTypes.JSON)
        } catch {
          case t: Throwable =>
            LOG.error("get event json api error", t)
            InternalServerError
        }
      case _ =>
        Unauthorized
    }
  }

  def getPublishedEvent(id: Int) = Action { request =>

    val expr = sql""" select row_to_json(r) from "PUBLISHED_EVENT" r where id = #$id """.as[String]
    val task = db.run(expr)

    try {
      val result: Vector[String] = Await.result(task, 5.seconds)

      Ok(result.head).as(JSON)

    } catch {
      case t: Throwable =>
        LOG.error("get published event error", t)
        InternalServerError
    }
  }

  def getEventReview() = Action { request =>

    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>

        val expr = Tables.EventReview.map(e => e)

        val task: Future[Seq[Tables.EventReviewRow]] = db.run(expr.result)

        try {
          val result: Seq[Tables.EventReviewRow] = Await.result(task, 5.seconds)

          val objSeq =
            for (i <- result) yield Json.obj(
              "id" -> i.id,
              "ref_event" -> i.relationEvent,
              "ref_org" -> email,
              "name" -> i.name,
              "content" -> i.content,
              "image" -> i.image.getOrElse[String]("none"),
              "top" -> i.stayTop
            )

          Ok(Json.toJson(objSeq)).as(JSON)
        } catch {
          case t: Throwable =>
            LOG.error("get event review error", t)
            InternalServerError
        }
      case _ =>
        Unauthorized
    }
  }

  def deleteEventReview(id: Int) = Action { request =>

    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>

        val expr = Tables.EventReview.filter(e => e.relationOrg === email && e.id === id).delete
        val task = db.run(expr)
        try {
          val result = Await.result(task, 5.seconds)
          if (result == 1) {
            Ok
          } else {
            BadRequest
          }
        } catch {
          case t: Throwable =>
            LOG.error("delete event review error", t)
            InternalServerError
        }
      case _ =>
        Unauthorized
    }
  }



  def getEventBy(id: Int) = Action { request =>
    val expr = sql""" select row_to_json(*) from "EVENT","PUBLISHED_EVENT"  where "EVENT".org = "PUBLISHED_EVENT".org and "PUBLISHED_EVENT".id = #$id """.as[String]
    LOG.debug(expr.statements.toString())
    val task = db.run(expr)
    val result = Await.result(task, 5.seconds).headOption
    Ok(result.getOrElse("{}")).as(ContentTypes.JSON)
  }

  def getCounsellor = Action { implicit request =>

    readAuthFromCookie(request) match {
      case Some(OrgRole(email)) =>

        val expr = sql""" select array_to_json(array_agg(r)) from (select id, name, enabled, index, show from "ORG_COUNSELLOR") as r; """.as[String]
        val task = db.run(expr)

        try {
          val result: Vector[String] = Await.result(task, 5.seconds)

          Ok(result.head).as(JSON)
        } catch {
          case t: Throwable =>
            LOG.error("get counsellor error", t)
            InternalServerError
        }

      case _ =>
        Unauthorized
    }
  }

  def getEventView(id: Int) = Action { implicit request =>
    val expr = sql""" with r1 as (select "PUBLISHED_EVENT".id,
                                  "PUBLISHED_EVENT".name as event_name,
                                  "PUBLISHED_EVENT".status,
                                  venues, start_time, end_time, people_limit, counsellor, "PUBLISHED_EVENT".image,
                                  origin_price, current_price, insurance_price, price_explain, status, change_cause,
                                  classify, form, tag, summary, age_limit_max, age_limit_min,
                                  time_span, allow_parents_join, need_manager, notice, safeinfo,
                                  "EVENT".name as base_name from  "PUBLISHED_EVENT"
                      left join "EVENT"
                        on template = "EVENT".name
                                where "PUBLISHED_EVENT".id  = #$id)
                    select row_to_json(r2) from r1 as r2;""".as[String]

    LOG.debug(expr.statements.head)
    val task = db.run(expr)
    try {
      val resultSet: Vector[String] = Await.result(task, 5.seconds)
      val result = resultSet.headOption
      result match  {
        case Some(r: String) =>
          Ok(r).as(JSON)
        case None =>
          NotFound
      }
    } catch {
      case t: Throwable =>
        LOG.error(s"get event view ($id) error", t)
        InternalServerError
    }
  }

  def joinEvent(id: Int) = Action { implicit request =>
    Ok
  }

}