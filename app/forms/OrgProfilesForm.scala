package forms

import play.api.data.Form
import play.api.data.Forms._

/**
 * Created by huodong on 15/8/3.
 */
case class OrgProfilesForm(name: String,
                           address: String,
                           tel: String,
                           province: String,
                            nature: String,
                           city: String,
                           orgType: String,
                            classify: String,
                           code: String,
                           leaderName: String,
                           leaderId: String,
                           personnelCount: Int,
                           tags: String,
                           safe: String,
                           parent: Option[String],
                           image: String,
                           summary: String)

object OrgProfilesForm {
  val form = Form[OrgProfilesForm](
    mapping(
      "name" -> nonEmptyText,
      "address" -> nonEmptyText,
      "tel" -> nonEmptyText(6, 32),
      "province" -> nonEmptyText(1, 64),
      "city" -> nonEmptyText,
      "nature" -> ignored("nature"),
      "orgType" -> nonEmptyText,
      "classify" -> nonEmptyText,
      "code" -> nonEmptyText,
      "leaderName" -> nonEmptyText,
      "leaderId" -> nonEmptyText,
      "personnelCount" -> number,
      "tags" -> text,
      "safe" -> text,
      "parent" -> optional(nonEmptyText),
      "image" -> ignored[String]("image"),
      "summary" -> text
    )(OrgProfilesForm.apply)(OrgProfilesForm.unapply)
  )
}