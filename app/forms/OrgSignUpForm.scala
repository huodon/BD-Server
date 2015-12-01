package forms

import play.api.data.Form
import play.api.data.Forms._

/**
 * Created by huodong on 15/7/20.
 */

/**
* 机构注册表单
*/
case class OrgSignUpForm(email: String, isSchool: Int, password: String, passwordAgain: String, captcha: String)

object OrgSignUpForm {
  val form = Form[OrgSignUpForm](
    mapping(
      "email" -> email,
      "isSchool" -> number,
      "password" -> nonEmptyText(8, 16),
      "passwordAgain" -> nonEmptyText(8, 18),
      "captcha" -> nonEmptyText(4, 4)
    )(OrgSignUpForm.apply)(OrgSignUpForm.unapply)
    .verifying(data => data.passwordAgain == data.password)
  )

}
