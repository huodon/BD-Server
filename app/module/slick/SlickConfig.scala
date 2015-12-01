package module.slick

import javax.inject.Inject
import javax.inject.Singleton

import play.api.db.slick.{DatabaseConfigProvider, DbName, SlickApi}
import slick.backend.DatabaseConfig
import slick.driver.{JdbcProfile, JdbcDriver}

/**
 * author: huodon@gmail.com
 * created: 15/7/20
 */

/**
* TODO Config  PostgREST https://github.com/begriffs/postgrest/wiki/Routing
*
*/
object SlickConfig  {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile]("default")(play.api.Play.current)
}



