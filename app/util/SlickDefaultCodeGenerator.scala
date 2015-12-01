package util

import slick.codegen.SourceCodeGenerator
import scala.util.{Try, Success, Failure}
import play.api.Logger.{logger => LOG}
/**
 * Created by huodong on 15/7/27.
 */
object SlickDefaultCodeGenerator {

  private val config = Array("slick.driver.PostgresDriver",
    "org.postgresql.Driver",
    "jdbc:postgresql://120.25.252.244:5432/bandou",
    "app",
    "model.slick",
    "bandou" )

  def run(password: String) = {
    LOG.info("==== start ====")
    Try {
      SourceCodeGenerator.main(config :+ password)
    } match {
      case Success(_) => LOG.info("success!")
      case Failure(t) => LOG.error(t.getMessage)
    }
    LOG.info("==== end ====")
  }
}
