package service

/**
 * Created by huodong on 15/7/27.
 */

import javax.inject._
import model.slick.Tables
import module.slick.SlickConfig
import play.api.Logger.{logger => LOG}
import reactivemongo.api.gridfs.{ReadFile, DefaultFileToSave, GridFS}
import scala.util.{Try, Success, Failure}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class SeedService  {

  import slick.driver.PostgresDriver.api._

  lazy val db = SlickConfig.dbConfig.db

  def echoOrg() = {
    val a = Tables.Org.map(o => o.events).result

    db.run(a).onComplete {
      case Success(r) =>
        LOG.debug(r.toString())
      case Failure(t) => LOG.error(t.getMessage)
    }
  }

  def plainSQL() = {
    val q = sql"select array_append(ARRAY[1,2], 3);".as[String].head
    db.run(q)
  }

  def gridFS() = {
    // TODO read GridFS doc!
    // http://stephane.godbillon.com/2012/11/28/writing-a-simple-app-with-reactivemongo-and-play-framework-pt-3-gridfs.html

  }
}
