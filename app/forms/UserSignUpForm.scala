package forms

import play.api.data.{Form, Forms}

/**
 * author: huodon@gmail.com
 * created: 15/7/5
 */

case class UserSignUpForm(
  name: String,
  password: String,
  passwordAgain: String,
  mobile: String,
  captcha: String)

object UserSignUpForm {
  val form = Form[UserSignUpForm](
    Forms.mapping(
      "name" -> Forms.nonEmptyText(3, 16),
      "password" -> Forms.nonEmptyText(8, 24),
      "passwordAgain" -> Forms.nonEmptyText(8, 24),
      "mobile" -> Forms.nonEmptyText(11, 11),
      "captcha" -> Forms.nonEmptyText(4, 4)
    )(UserSignUpForm.apply)(UserSignUpForm.unapply)
    .verifying(info => info.password == info.password))

}




