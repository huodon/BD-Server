package forms

import play.api.data.Form
import play.api.data.Forms._


/**
 * author: huodon@gmail.com
 * created: 15/8/9
 */
case class OrgAddEventReviewForm(
  name: String,
  relationEvent: String,
  stayTop: Boolean,
  content: String,
  image: String
  )

object OrgAddEventReviewForm {
  val form = Form[OrgAddEventReviewForm](
    mapping(
      "name" -> nonEmptyText,
      "relationEvent" -> nonEmptyText,
      "stayTop" -> boolean,
      "content" -> nonEmptyText,
      "image" -> ignored("image")
    )(OrgAddEventReviewForm.apply)(OrgAddEventReviewForm.unapply)
  )
}
