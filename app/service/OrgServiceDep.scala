package service

import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Singleton}

import forms._
import model.mongo.EventTemplateWriter
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONArray, BSONBoolean, BSONString, BSONDocument}
import play.api.Logger.{logger => LOG}
import reactivemongo.api.commands.UpdateCommand
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Try, Success, Failure}

/**
 * author: huodon@gmail.com
 * created: 15/7/20
 */

@Singleton
class OrgServiceDep @Inject()(reactiveMongoApi: ReactiveMongoApi) {

  lazy val orgCollection = reactiveMongoApi.db.collection[BSONCollection]("org")
  lazy val eventTemplate = reactiveMongoApi.db.collection[BSONCollection]("event_template")


  def save(signUpOrg: OrgSignUpForm) = {

    def save() = {
      val bson = BSONDocument(
        "email" -> BSONString(signUpOrg.email),
        "isSchool" -> BSONBoolean(signUpOrg.isSchool == 0),
        "password" -> BSONString(signUpOrg.password),
        "eventTemplate" -> BSONArray.empty
      )
      Await.result(orgCollection.insert(bson), Duration(5, TimeUnit.SECONDS))
    }

    val r: Future[WriteResult] = orgCollection.find(BSONDocument("email" -> signUpOrg.email)).cursor[BSONDocument]().headOption.transform(
      (opt: Option[BSONDocument]) => {
        opt match {
          case None => save()
          case _ => throw new Exception(s"org ${signUpOrg.email} exists!")
        }
      },
      t => t
    )

    Try {
      Await.result(r, Duration(3, TimeUnit.SECONDS))
    }
  }

  def find(signInOrg: OrgSignInForm) = {
    val first: Future[Option[BSONDocument]] = orgCollection
      .find(BSONDocument("email" -> signInOrg.email, "password" -> signInOrg.password))
      .cursor[BSONDocument]().headOption
    val result = Await.result(first, Duration(5, TimeUnit.SECONDS))
    LOG.debug(s"find org: {result.toString}")
    result
  }


  def saveEventTemplate(publishEventForm: OrgAddEventTemplateForm) = {
    def saveTemplate(): WriteResult = {
      val data = BSONDocument(
        "name" -> publishEventForm.name,
        "classify" -> publishEventForm.classify,
        "form" -> publishEventForm.form,
        "tag" -> publishEventForm.tag,
        "summary" -> publishEventForm.summary,
        "ageLimitMin" -> publishEventForm.ageLimitMin,
        "ageLimitMax" -> publishEventForm.ageLimitMax,
        "timeSpan" -> publishEventForm.timeSpan,
        "allowParentsJoin" -> publishEventForm.allowParentsJoin,
        "needManager" -> publishEventForm.needManager,
        "notice" -> publishEventForm.notice,
        "safeInfo" -> publishEventForm.safeInfo
      )
      Await.result(eventTemplate.insert(data), Duration(3, TimeUnit.SECONDS))
    }

    val findAction = eventTemplate.find(BSONDocument("name" -> publishEventForm.name)).cursor[BSONDocument]().headOption
    val find: Option[BSONDocument] = Await.result(findAction, Duration(3, TimeUnit.SECONDS))

    find match {
      case Some(_) => throw new Exception("template exists!")
      case None => saveTemplate()
    }
  }

  def findEventTemplate(orgEmail: String): Try[List[BSONDocument]] = {

    val findAction: Future[List[BSONDocument]] = eventTemplate.find(BSONDocument("email" -> orgEmail)).cursor[BSONDocument]().collect[List](100)
    Try {
      Await.result(findAction, Duration(3, TimeUnit.SECONDS))
    }
  }

}
