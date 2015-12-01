package forms

/**
 * author: huodon@gmail.com
 * created: 15/7/27
 */

import play.api.data.Form
import play.api.data.Forms._

case class PubEventForm(
  template: String,
  name: String,
  venues: String,
  startTime: String,
  endTime: String,
  stopJoinTime: String,
  peopleLimit: Int,
  counsellor: String,
  originPrice: Int,
  currentPrice: Int,
  insurancePrice: Int,
  priceExplain: String
  )

object PubEventForm {
  val form = Form[PubEventForm](
    mapping(
      "template" -> text,
      "name" -> nonEmptyText,
      "venues" -> text,
      "startTime" -> text,
      "endTime" -> text,
      "stopJoinTime" -> text,
      "peopleLimit" -> number,
      "counsellor" -> text,
      "originPrice" -> number,
      "currentPrice" -> number,
      "insurancePrice" -> number,
      "priceExplain" -> text
    )(PubEventForm.apply)(PubEventForm.unapply)
  )
}
