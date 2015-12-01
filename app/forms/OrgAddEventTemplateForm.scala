package forms

import java.util.Date

import play.api.data.Form
import play.api.data.Forms._

/**
*  Created by huodong on 15/7/25.
*/

case class OrgAddEventTemplateForm(name: String,
                            classify: String,
                            form: String,
                            tag: String,
                            image: String,
                            summary: String,
                            ageLimitMin: Int,
                            ageLimitMax: Int,
                            timeSpan: String,
                            allowParentsJoin: String,
                            needManager: String,
                            notice: Option[String],
                            safeInfo: Option[String])

object OrgAddEventTemplateForm {
  val form = Form[OrgAddEventTemplateForm](
    mapping(
      "name" -> nonEmptyText,
      "classify" -> nonEmptyText,
      "form" -> nonEmptyText,
      "tag" -> nonEmptyText,
      // FIXME
      "image" -> ignored[String]("ignored"),
      "summary" -> nonEmptyText,
      "ageLimitMin" -> number(1,110),
      "ageLimitMax" -> number(1,110),
      "timeSpan" -> nonEmptyText,
      "allowParentsJoin" -> nonEmptyText,
      "needManager" -> nonEmptyText,
      "notice" -> optional(text),
      "safeInfo" -> optional(text)
    )(OrgAddEventTemplateForm.apply)(OrgAddEventTemplateForm.unapply).verifying(v => v.ageLimitMax >= v.ageLimitMin)
  )
}