package module.slick

/**
 * Created by huodong on 15/7/25.
 */

import com.github.tminglei.slickpg._
import play.api.libs.json._

trait SlickPostgresDriver extends ExPostgresDriver
with PgArraySupport
with PgDateSupport
with PgRangeSupport
with PgPlayJsonSupport
with PgSearchSupport
with PgNetSupport
with PgPostGISSupport
with PgLTreeSupport {

  def pgjson = "jsonb"

  override val api = SlickPostgresAPI

  object SlickPostgresAPI extends API
  with ArrayImplicits
  with DateTimeImplicits
  with JsonImplicits
  with NetImplicits
  with LTreeImplicits
  with PostGISImplicits
  with RangeImplicits
  with SearchImplicits
  with SearchAssistants {

//    implicit val strListTypeMapper = new SimpleArrayJdbcType[String]("text").to(_.toList)
    implicit val playJsonArrayTypeMapper =
      new AdvancedArrayJdbcType[JsValue](pgjson,
        (s) => utils.SimpleArrayUtils.fromString[JsValue](Json.parse)(s).orNull,
        (v) => utils.SimpleArrayUtils.mkString[JsValue](_.toString())(v)
      ).to(_.toList)
  }
}

object SlickPostgresDriver extends SlickPostgresDriver
