package forms

/**
 * author: huodon@gmail.com
 * created: 15/7/20
 */

import play.api.data.Form
import play.api.data.Forms._


case class OrgSignInForm(email: String, password: String, captcha: String)

object OrgSignInForm {
  val form = Form[OrgSignInForm](mapping(
    "email" -> email,
    "password" -> nonEmptyText(8, 16),
    "captcha" -> nonEmptyText(4,6)
  )(OrgSignInForm.apply)(OrgSignInForm.unapply)
  )
}