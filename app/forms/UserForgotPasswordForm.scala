package forms

import play.api.data.Form
import play.api.data.Forms._

/**
*  author: huodon@gmail.com
*  created: 15/7/19
*/

/**
*
*/
case class UserForgotPasswordForm(name: String, mobile: String, captcha: String)

object UserForgotPasswordForm {
  val form = Form[UserForgotPasswordForm](
    mapping(
      "name" -> nonEmptyText(3, 18),
      "mobile" -> nonEmptyText(11, 11),
      "captcha" -> nonEmptyText(4, 4)
    )(UserForgotPasswordForm.apply)(UserForgotPasswordForm.unapply)
  )
}


