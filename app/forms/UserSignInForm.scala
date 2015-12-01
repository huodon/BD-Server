package forms


import play.api.data.Form
import play.api.data.Forms._


/**
* 用户注册表单
*/
case class UserSignInForm(
  name: String,
  password: String,
  captcha: String)

object UserSignInForm {
  val form = Form[UserSignInForm](
    mapping(
      "name" -> nonEmptyText,
      "password" -> nonEmptyText,
    "captcha" -> nonEmptyText(4,6))(UserSignInForm.apply)(UserSignInForm.unapply))
}
