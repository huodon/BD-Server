package model.mongo

import reactivemongo.bson._

/**
 * Created by huodong on 15/7/23.
 */
case class Org(id: BSONObjectID, isSchool: BSONBoolean, email: BSONString, password: BSONString)

object OrgReader extends BSONReader[BSONDocument, Org] {
  override def read(bson: BSONDocument): Org = {
    val id: Option[BSONObjectID] = bson.getAs[BSONObjectID]("_id")
    val isSchool: Option[BSONBoolean] = bson.getAs[BSONBoolean]("isSchool")
    val email = bson.getAs[BSONString]("email")
    val password = bson.getAs[BSONString]("password")

    Org(
      id.get,
      isSchool.get,
      email.get,
      password.get
    )
  }
}