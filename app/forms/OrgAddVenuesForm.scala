package forms

/**
 * Created by huodong on 15/7/29.
 */

import play.api.data.Form
import play.api.data.Forms._

case class OrgAddVenuesForm(belong: String,
  name: String,
  area: Int,
  capability: Int,
  location: String,
  image: String)

object OrgAddVenuesForm {
  val form = Form[OrgAddVenuesForm](mapping(
    "belong" -> nonEmptyText,
    "name" -> nonEmptyText,
    "area" -> number,
    "capability" -> number,
    "location" -> nonEmptyText,
    "image" -> ignored("image")
  )(OrgAddVenuesForm.apply)(OrgAddVenuesForm.unapply))
}