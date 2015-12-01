package module.slick

import slick.lifted.ProvenShape

/**
*  Created by huodong on 15/7/25.
*/
object PostgresTables extends {
  val profile = SlickPostgresDriver
} with PostgresTables

trait PostgresTables {
  val profile: slick.driver.JdbcProfile

  import profile.api._
  import slick.model.ForeignKeyAction
  import slick.collection.heterogeneous._
  import slick.collection.heterogeneous.syntax._

  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.

  import slick.jdbc.{GetResult => GR}

  case class OrgRow(id: Int,
                    email: String,
                    password: String,
                    isSchool: Boolean,
                    eventTemplate: List[Int])

 // FIXME compile error
/*  class Org(tag: Tag) extends Table[OrgRow](tag, "org") {

    def * : ProvenShape[OrgRow] = (id,email,password,isSchool,eventTemplate) <> (OrgRow.tupled,OrgRow.unapply)

    val id: Rep[Int] = column[Int]("id",O.AutoInc,O.PrimaryKey)
    val email: Rep[String] = column[String]("email", O.PrimaryKey)
    val password: Rep[String] = column[String]("password")
    val isSchool: Rep[Boolean] = column[Boolean]("is_school")
    val eventTemplate: Rep[List[Int]] = column[List[Int]]("event_template")
  }*/
}
