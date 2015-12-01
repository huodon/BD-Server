package forms

import play.api.data.Form
import play.api.data.Forms._

/**
 * Created by huodong on 15/8/4.
 */


case class OrgAddCounsellorForm(name: String)

object OrgAddCounsellorForm {
  val form = Form[OrgAddCounsellorForm](
    mapping(
      "name" -> nonEmptyText
    )(OrgAddCounsellorForm.apply)(OrgAddCounsellorForm.unapply))
}