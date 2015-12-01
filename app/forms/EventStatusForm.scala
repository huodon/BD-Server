package forms

import play.api.data.Form
import play.api.data.Forms._
/**
 * Created by huodong on 15/8/9.
 */
case class EventStatusForm (status: String, cause: String)
object EventStatusForm {
  val form = Form[EventStatusForm](
    mapping(
      "status" -> nonEmptyText,
      "cause" -> nonEmptyText
    )(EventStatusForm.apply)(EventStatusForm.unapply)
  )
}