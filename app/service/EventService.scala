package service

/**
 * author: huodon@gmail.com
 * created: 15/7/27
 */

import javax.inject._

import forms._
import model.slick.Tables
import module.slick.SlickConfig
import play.api.Logger.{logger => LOG}
import slick.dbio.Effect.Write
import slick.profile.FixedSqlAction

import scala.concurrent.Future

@Singleton
class EventService {

  val db = SlickConfig.dbConfig.db
  import slick.driver.PostgresDriver.api._

  def saveTemplate(orgEmail: String, imageName: String, event: OrgAddEventTemplateForm): Future[Int] = {

    val q: FixedSqlAction[Int, NoStream, Write] = Tables.Event.map( (e: Tables.Event) => (
      e.name,
      e.org,
      e.image,
      e.classify,
      e.form,
      e.tag,
      e.summary,
      e.ageLimitMin,
      e.ageLimitMax,
      e.timeSpan,
      e.allowParentsJoin,
      e.needManager,
      e.notice,
      e.safeinfo)) += (
        event.name,
      orgEmail,
        imageName,
        event.classify,
        event.form,
        Some(event.tag),
        Some(event.summary),
        Some(event.ageLimitMin),
        Some(event.ageLimitMax),
        Some(event.timeSpan),
        Some(event.allowParentsJoin == "on"),
        Some(event.needManager == "on"),
        event.notice,
        event.safeInfo
      )
    db.run(q)
  }

  def publishEvent(orgEmail: String, event: PubEventForm) = {
    val q = Tables.PublishedEvent.map(e => (
      e.org,
      e.template,
      e.venues,
      e.startTime,
      e.endTime,
      e.peopleLimit,
      e.counsellor,
      e.originPrice,
      e.currentPrice,
      e.insurancePrice,
      e.priceExplain
      )) += (
      orgEmail,
      event.template,
      event.venues,
      event.startTime,
      event.endTime,
      event.peopleLimit,
      Some(event.counsellor),
      event.originPrice,
      event.currentPrice,
      event.insurancePrice,
      event.priceExplain
      )
    db.run(q)
  }
  
  def listTemplate(limit: Int = 200): Future[Seq[Tables.EventRow]] = {
    db.run(Tables.Event.map(e => e).take(limit).result)
  }

  def findTempleteByName(name: String): Future[Tables.EventRow] = {
    db.run(Tables.Event.filter(e => e.name === name).result.head)
  }

  def findTempleteByOrg(email: String): Future[Seq[Tables.EventRow]] = {
    val a = Tables.Event.filter(e => e.org === email).result
    db.run(a)
  }


  def listPublishedEvent(limit: Int = 200): Future[Seq[Tables.PublishedEventRow]] = {
    db.run(Tables.PublishedEvent.map(e => e).take(limit).result)
  }

  def findPublishedEventByOrg(email: String, limit: Int = 200): Future[Seq[Tables.PublishedEventRow]] = {
    db.run(Tables.PublishedEvent.filter(pe => pe.org === email).result)
  }

  
  
  def findPubEventById(id: Int): Future[Tables.PublishedEventRow] = {
    val a = Tables.PublishedEvent.filter(e => e.id === id).result.head

    LOG.debug(a.statements.toString)
    db.run(a)
  }

  def findById(id: Int) = {
    val a = Tables.Event.filter(_.id === id).result.head
    db.run(a)
  }

  def removeByName(name: String) = {
    val a = Tables.Event.filter(_.name === name).delete

    db.run(a)
  }


  def removeOneTemplateByOrg(orgEmail: String, templateName: String) = {
    val a = Tables.Event.filter(e => e.org === orgEmail && e.name === templateName).delete
    db.run(a)
  }

  def deleteTemplate(email: String, name: String) = {
    val a = Tables.Event.filter(e => e.name === name && e.org === email).delete
    db.run(a)
  }
}
