package service

/**
 * Created by huodong on 15/7/27.
 */

import javax.inject._
import forms._
import model.slick.Tables

import module.slick.SlickConfig

import scala.concurrent.Future

@Singleton
class OrgService  {

  lazy val db = SlickConfig.dbConfig.db
  import slick.driver.PostgresDriver.api._

  def save(signUpOrg: OrgSignUpForm) = {
    val a = Tables.Org.map(o => (o.email, o.isSchool, o.hashedPassword)) +=(signUpOrg.email, signUpOrg.isSchool == 0, signUpOrg.password)
    db.run(a)
  }

  def findByEmail(email: String): Future[Tables.OrgRow] = {
    val a = Tables.Org.filter(o => o.email === email).result.head
    db.run(a)
  }

  def findIdByEmail(email: String): Future[Int] = {
    val a = Tables.Org.filter(o => o.email === email).map(_.id).result.head
    db.run(a)
  }

  def orgClassify(): Future[Seq[String]] = {
    val a = Tables.DictOrgClassify.map(c => c.value).result
    db.run(a)
  }

  def schoolLevel(): Future[Seq[String]] = {
    val a = Tables.DictSchoolLevel.map(c => c.value).result
    db.run(a)
  }

}
