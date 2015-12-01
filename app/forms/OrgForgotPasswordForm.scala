package forms

import play.api.data.Form
import play.api.data.Forms._

/**
 * author: huodon@gmail.com
 * created: 15/7/20
 */

case class OrgForgotPasswordForm(email: String, mobile: String, captcha: String)

object OrgForgotPasswordForm {
  val form = Form[UserForgotPasswordForm](
    mapping(
      "email" -> nonEmptyText(3, 18),
      "mobile" -> nonEmptyText(11, 11),
      "captcha" -> nonEmptyText(4, 4)
    )(UserForgotPasswordForm.apply)(UserForgotPasswordForm.unapply)
  )
}
