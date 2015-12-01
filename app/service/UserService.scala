package service

import javax.inject._

import forms._
import model.slick.Tables
import module.slick.SlickConfig
import play.api.Logger.{logger => LOG}
import slick.dbio.Effect.Write
import slick.profile.FixedSqlAction

import scala.concurrent.Future

/**
 * Created by huodong on 15/7/27.
 */

@Singleton
class UserService {

  import slick.driver.PostgresDriver.api._

  lazy val db = SlickConfig.dbConfig.db


  def save(signUpUser: UserSignUpForm): Future[Int] = {
    val a: FixedSqlAction[Int, NoStream, Write] =
      Tables.User.map(u => (u.loginName, u.mobile, u.hashedPassword)) +=(
        signUpUser.name, signUpUser.mobile, signUpUser.password)
    db.run(a)
  }

  def findByName(name: String): Future[Tables.UserRow] = {
    db.run(
      Tables.User.filter(_.loginName === name).result.head)
  }

  def findIdByName(name: String): Future[Int] = {
    db.run(
      Tables.User.filter(u => u.loginName === name).map(u => u.id).result.head
    )
  }
}
