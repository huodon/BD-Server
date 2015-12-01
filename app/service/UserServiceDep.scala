package service

/**
 * author: huodon@gmail.com
 * created: 15/7/20
 */


import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Singleton}

import model.mongo.{UserReader, User}
import model.mongo.UserReader
import play.api.Logger.{logger => LOG}
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID, BSONValue}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

@Singleton
class UserServiceDep @Inject()(val reactiveMongoApi: ReactiveMongoApi) {

  import forms._
  import reactivemongo.core.errors.ReactiveMongoException

  import concurrent.ExecutionContext.Implicits.global
  import concurrent.Future



  lazy val collection = reactiveMongoApi.db.collection[BSONCollection]("person")

  def save(data: UserSignUpForm) = {

    val find: Future[Option[BSONDocument]] = Future.firstCompletedOf(Seq(
      collection.find(BSONDocument("name" -> data.name)).cursor[BSONDocument]().headOption,
      collection.find(BSONDocument("mobile" -> data.mobile)).cursor[BSONDocument]().headOption
    ))

    val result: Option[BSONDocument] = Await.result(find, Duration(5, TimeUnit.SECONDS))

    def save() = {
      val json = Json.obj(
        "name" -> data.name,
        "mobile" -> data.mobile,
        "password" -> data.password
      )
      collection.insert(json)
    }

    result match {
      case Some(user) =>
        LOG.debug("保存失败, 用户已经存在!")
        throw ReactiveMongoException("保存失败, 用户已经存在!!")
      case None =>
        save()
    }
  }

  def find(signInUser: UserSignInForm): Option[User] = {
    val LOG = play.api.Logger.logger
    val query = BSONDocument("name" -> signInUser.name, "password" -> signInUser.password)
    val future: Future[Option[BSONDocument]] = collection.find(query).cursor[BSONDocument]().headOption

    val optBson: Option[BSONDocument] = Try {
      val result: Option[BSONDocument] = Await.result(future, Duration(4, TimeUnit.SECONDS))
      LOG.debug(s"find ${signInUser.name}: ${result.isDefined.toString}")
      result
    }.toOption.getOrElse(None)

    val user: Option[User] = optBson.map { bson =>

      val name: Option[BSONValue] = bson.get("name")
      val password = bson.get("password")
      val mobile = bson.get("mobile")
      val id = bson.get("_id")
      LOG.debug(s"$name, $password, $mobile, $id")
      implicit val reader = UserReader
      bson.as[User]
    }
    user
  }
}