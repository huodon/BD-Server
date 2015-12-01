package module

import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.Ficus._

/**
 * author: huodon@gmail.com
 * created: 15/8/6
 */
object Config {

  lazy val reader = ConfigFactory.load()

  object  YunTongXun {
    def accountSID = reader.as[String]("yuntongxun.ACCOUNT_SID")
    def authToken = reader.as[String]("yuntongxun.AUTH_TOKEN")
    def appId = reader.as[String]("yuntongxun.APP_ID")
    def appToken = reader.as[String]("yuntongxun.APP_TOKEN")
    def devUrl = reader.as[String]("yuntongxun.DEV_URL")
    def prodUrl = reader.as[String]("yuntongxun.PROD_URL")
  }

}
