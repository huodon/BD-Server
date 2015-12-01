package forms

/**
 * Created by huodong on 15/7/29.
 */

import play.api.data.Form
import play.api.data.Forms._

case class OrgPostNoticeForm(content: String)

object OrgPostNoticeForm {
  val form = Form[OrgPostNoticeForm](mapping(
    "content" -> text
  )(OrgPostNoticeForm.apply)(OrgPostNoticeForm.unapply))
}


