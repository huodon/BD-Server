package model.mongo

import reactivemongo.bson.{BSONDateTime, BSONWriter, BSONReader, BSONDocument}

/**
 * Created by huodong on 15/7/25.
 */
case class EventTemplate(name: String,
                         classify: String,
                         form: String,
                         tag: String,
                         summary: String,
                         ageLimitMin: Int,
                         ageLimitMax: Int,
                         timeSpan: String,
                         allowParentsJoin: Boolean,
                         needManager: Boolean,
                         notice: Option[String],
                         safeInfo: Option[String])

object EventTemplateReader extends BSONReader[BSONDocument, EventTemplate] {
  override def read(bson: BSONDocument): EventTemplate = {
    EventTemplate(
      name = bson.getAs[String]("name").get,
      classify = bson.getAs[String]("classify").get,
      form = bson.getAs[String]("form").get,
      tag = bson.getAs[String]("tag").get,
      summary = bson.getAs[String]("summary").get,
      ageLimitMin = bson.getAs[Int]("ageLimitMin").get,
      ageLimitMax = bson.getAs[Int]("ageLimitMax").get,
      timeSpan = bson.getAs[String]("timeSpan").get,
      allowParentsJoin = bson.getAs[String]("allowParentsJoin").get == "on",
      needManager = bson.getAs[String]("needManager").get == "on",
      notice = bson.getAs[String]("notice"),
      safeInfo = bson.getAs[String]("safeInfo")
    )
  }
}

object EventTemplateWriter extends BSONWriter[EventTemplate, BSONDocument] {
  override def write(t: EventTemplate): BSONDocument = {
    BSONDocument(
      "name" -> t.name,
      "classify" -> t.classify,
      "form" -> t.form,
      "tag" -> t.tag,
      "summary" -> t.summary,
      "ageLimitMin" -> t.ageLimitMin,
      "ageLimitMax" -> t.ageLimitMax,
      "timeSpan" -> t.timeSpan,
      "allowParentsJoin" -> t.allowParentsJoin,
      "needManager" -> t.needManager,
      "notice" -> t.notice,
      "safeInfo" -> t.safeInfo
    )
  }
}