package forms

import play.api.data.Form
import play.api.data.Forms._

/**
 * Created by huodong on 15/8/9.
 */
case class OrgPasswordForm(oldPassword: String,
                           newPassword: String,
                           newPasswordAgain: String,
                           captcha: String)


object OrgPasswordForm {
  val form = Form[OrgPasswordForm](
    mapping(
      "oldPassword" -> nonEmptyText(8, 32),
      "newPassword" -> nonEmptyText(8, 32),
      "newPasswordAgain" -> nonEmptyText(8, 32),
      "captcha" -> nonEmptyText(8, 32)
    )(OrgPasswordForm.apply)(OrgPasswordForm.unapply)
      .verifying(c => c.newPassword == c.newPasswordAgain)
  )
}