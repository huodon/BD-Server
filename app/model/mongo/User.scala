package model.mongo

import reactivemongo.bson._

/**
 * author: huodon@gmail.com
 * created: 15/7/23
 */
case class User(id: BSONObjectID, name: BSONString, password: BSONString, mobile: BSONString)

object UserReader extends BSONReader[BSONDocument, User] {
  override def read(bson: BSONDocument) = {
    val opt = for (
      name <- bson.getAs[BSONString]("name");
      password <- bson.getAs[BSONString]("password");
      mobile <- bson.getAs[BSONString]("mobile");
      id <- bson.getAs[BSONObjectID]("_id")
    ) yield User(id, name, password, mobile)

    opt.get
  }
}