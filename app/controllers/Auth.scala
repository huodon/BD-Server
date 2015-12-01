package controllers

/**
 * author: huodon@gmail.com
 * created: 15/7/21
 */


import javassist.tools.web.BadHttpRequest

import play.api.libs.Crypto
import play.api.mvc.{Cookie, RequestHeader, Result}
import play.api.Logger.{logger => LOG}
import scala.util.Try

sealed trait Role

case class UserRole(name: String) extends Role

case class OrgRole(email: String) extends Role

/**
* TODO read JWT doc! http://blog.rainy.im/2015/06/10/react-jwt-pretty-good-practice/
*/
object Auth {

  // 将认证写入加密 cookie
  def authCookie(role: Role): Cookie = {
    role match {
      case UserRole(name) =>
        Cookie("token", Crypto.encryptAES(s"user:$name"))
      case OrgRole(email) =>
        Cookie("token", Crypto.encryptAES(s"org:$email"))
    }
  }

  def authFlush(role: String, value: String) = {
    Array("role" -> role, "userName" -> value)
  }

  // Read users from cookies
  def readAuthFromCookie(request: RequestHeader): Option[Role] = {
    val token = request.cookies.get("token")
    val tryGet: Option[Option[Role]] = token.map { sid =>
      val realVal = Crypto.decryptAES(sid.value)
      Try[Role] {
        val Array(key, value) = realVal.split(':')
        key match {
          case "user" => UserRole(value)
          case "org" => OrgRole(value)
        }
      }.toOption
    }
    tryGet.getOrElse(None)
  }
}