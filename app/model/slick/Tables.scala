package model.slick
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.PostgresDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema = Array(Address.schema, DictEventCost.schema, DictEventProcess.schema, DictEventStrategy.schema, DictEventStrategyStatus.schema, DictEventType.schema, DictEventVerifyStatus.schema, DictOrgClassify.schema, DictOrgType.schema, DictSchoolLevel.schema, DictSchoolNature.schema, Event.schema, EventClassify.schema, EventReview.schema, ImagesStore.schema, InfoMeta.schema, Org.schema, OrgCert.schema, OrgCounsellor.schema, OrgFuction.schema, OrgLetter.schema, OrgManager.schema, OrgNotice.schema, OrgProfiles.schema, Playground.schema, PublishedEvent.schema, Strategy.schema, Test.schema, User.schema, UserEvent.schema, UserEventLittle.schema, UserEvolution.schema, UserIdentityInfo.schema, UserTrackInfo.schema, Venues.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Address
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param city Database column city SqlType(varchar), Length(255,true), Default(None)
   *  @param area Database column area SqlType(int2), Default(None)
   *  @param street Database column street SqlType(varchar), Length(255,true), Default(None) */
  case class AddressRow(id: Int, city: Option[String] = None, area: Option[Short] = None, street: Option[String] = None)
  /** GetResult implicit for fetching AddressRow objects using plain SQL queries */
  implicit def GetResultAddressRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[Option[Short]]): GR[AddressRow] = GR{
    prs => import prs._
    AddressRow.tupled((<<[Int], <<?[String], <<?[Short], <<?[String]))
  }
  /** Table description of table ADDRESS. Objects of this class serve as prototypes for rows in queries. */
  class Address(_tableTag: Tag) extends Table[AddressRow](_tableTag, "ADDRESS") {
    def * = (id, city, area, street) <> (AddressRow.tupled, AddressRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), city, area, street).shaped.<>({r=>import r._; _1.map(_=> AddressRow.tupled((_1.get, _2, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column city SqlType(varchar), Length(255,true), Default(None) */
    val city: Rep[Option[String]] = column[Option[String]]("city", O.Length(255,varying=true), O.Default(None))
    /** Database column area SqlType(int2), Default(None) */
    val area: Rep[Option[Short]] = column[Option[Short]]("area", O.Default(None))
    /** Database column street SqlType(varchar), Length(255,true), Default(None) */
    val street: Rep[Option[String]] = column[Option[String]]("street", O.Length(255,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Address */
  lazy val Address = new TableQuery(tag => new Address(tag))

  /** Entity class storing rows of table DictEventCost
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param value Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
  case class DictEventCostRow(id: Int, value: String)
  /** GetResult implicit for fetching DictEventCostRow objects using plain SQL queries */
  implicit def GetResultDictEventCostRow(implicit e0: GR[Int], e1: GR[String]): GR[DictEventCostRow] = GR{
    prs => import prs._
    DictEventCostRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table DICT_EVENT_COST. Objects of this class serve as prototypes for rows in queries. */
  class DictEventCost(_tableTag: Tag) extends Table[DictEventCostRow](_tableTag, "DICT_EVENT_COST") {
    def * = (id, value) <> (DictEventCostRow.tupled, DictEventCostRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(value)).shaped.<>({r=>import r._; _1.map(_=> DictEventCostRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
    val value: Rep[String] = column[String]("value", O.PrimaryKey, O.Length(32,varying=true))
  }
  /** Collection-like TableQuery object for table DictEventCost */
  lazy val DictEventCost = new TableQuery(tag => new DictEventCost(tag))

  /** Entity class storing rows of table DictEventProcess
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param value Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
  case class DictEventProcessRow(id: Int, value: String)
  /** GetResult implicit for fetching DictEventProcessRow objects using plain SQL queries */
  implicit def GetResultDictEventProcessRow(implicit e0: GR[Int], e1: GR[String]): GR[DictEventProcessRow] = GR{
    prs => import prs._
    DictEventProcessRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table DICT_EVENT_PROCESS. Objects of this class serve as prototypes for rows in queries. */
  class DictEventProcess(_tableTag: Tag) extends Table[DictEventProcessRow](_tableTag, "DICT_EVENT_PROCESS") {
    def * = (id, value) <> (DictEventProcessRow.tupled, DictEventProcessRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(value)).shaped.<>({r=>import r._; _1.map(_=> DictEventProcessRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
    val value: Rep[String] = column[String]("value", O.PrimaryKey, O.Length(32,varying=true))
  }
  /** Collection-like TableQuery object for table DictEventProcess */
  lazy val DictEventProcess = new TableQuery(tag => new DictEventProcess(tag))

  /** Entity class storing rows of table DictEventStrategy
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param value Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
  case class DictEventStrategyRow(id: Int, value: String)
  /** GetResult implicit for fetching DictEventStrategyRow objects using plain SQL queries */
  implicit def GetResultDictEventStrategyRow(implicit e0: GR[Int], e1: GR[String]): GR[DictEventStrategyRow] = GR{
    prs => import prs._
    DictEventStrategyRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table DICT_EVENT_STRATEGY. Objects of this class serve as prototypes for rows in queries. */
  class DictEventStrategy(_tableTag: Tag) extends Table[DictEventStrategyRow](_tableTag, "DICT_EVENT_STRATEGY") {
    def * = (id, value) <> (DictEventStrategyRow.tupled, DictEventStrategyRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(value)).shaped.<>({r=>import r._; _1.map(_=> DictEventStrategyRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
    val value: Rep[String] = column[String]("value", O.PrimaryKey, O.Length(32,varying=true))
  }
  /** Collection-like TableQuery object for table DictEventStrategy */
  lazy val DictEventStrategy = new TableQuery(tag => new DictEventStrategy(tag))

  /** Entity class storing rows of table DictEventStrategyStatus
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param value Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
  case class DictEventStrategyStatusRow(id: Int, value: String)
  /** GetResult implicit for fetching DictEventStrategyStatusRow objects using plain SQL queries */
  implicit def GetResultDictEventStrategyStatusRow(implicit e0: GR[Int], e1: GR[String]): GR[DictEventStrategyStatusRow] = GR{
    prs => import prs._
    DictEventStrategyStatusRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table DICT_EVENT_STRATEGY_STATUS. Objects of this class serve as prototypes for rows in queries. */
  class DictEventStrategyStatus(_tableTag: Tag) extends Table[DictEventStrategyStatusRow](_tableTag, "DICT_EVENT_STRATEGY_STATUS") {
    def * = (id, value) <> (DictEventStrategyStatusRow.tupled, DictEventStrategyStatusRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(value)).shaped.<>({r=>import r._; _1.map(_=> DictEventStrategyStatusRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
    val value: Rep[String] = column[String]("value", O.PrimaryKey, O.Length(32,varying=true))
  }
  /** Collection-like TableQuery object for table DictEventStrategyStatus */
  lazy val DictEventStrategyStatus = new TableQuery(tag => new DictEventStrategyStatus(tag))

  /** Entity class storing rows of table DictEventType
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param value Database column value SqlType(varchar), PrimaryKey, Length(64,true) */
  case class DictEventTypeRow(id: Int, value: String)
  /** GetResult implicit for fetching DictEventTypeRow objects using plain SQL queries */
  implicit def GetResultDictEventTypeRow(implicit e0: GR[Int], e1: GR[String]): GR[DictEventTypeRow] = GR{
    prs => import prs._
    DictEventTypeRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table DICT_EVENT_TYPE. Objects of this class serve as prototypes for rows in queries. */
  class DictEventType(_tableTag: Tag) extends Table[DictEventTypeRow](_tableTag, "DICT_EVENT_TYPE") {
    def * = (id, value) <> (DictEventTypeRow.tupled, DictEventTypeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(value)).shaped.<>({r=>import r._; _1.map(_=> DictEventTypeRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column value SqlType(varchar), PrimaryKey, Length(64,true) */
    val value: Rep[String] = column[String]("value", O.PrimaryKey, O.Length(64,varying=true))
  }
  /** Collection-like TableQuery object for table DictEventType */
  lazy val DictEventType = new TableQuery(tag => new DictEventType(tag))

  /** Entity class storing rows of table DictEventVerifyStatus
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param value Database column value SqlType(varchar), PrimaryKey, Length(64,true) */
  case class DictEventVerifyStatusRow(id: Int, value: String)
  /** GetResult implicit for fetching DictEventVerifyStatusRow objects using plain SQL queries */
  implicit def GetResultDictEventVerifyStatusRow(implicit e0: GR[Int], e1: GR[String]): GR[DictEventVerifyStatusRow] = GR{
    prs => import prs._
    DictEventVerifyStatusRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table DICT_EVENT_VERIFY_STATUS. Objects of this class serve as prototypes for rows in queries. */
  class DictEventVerifyStatus(_tableTag: Tag) extends Table[DictEventVerifyStatusRow](_tableTag, "DICT_EVENT_VERIFY_STATUS") {
    def * = (id, value) <> (DictEventVerifyStatusRow.tupled, DictEventVerifyStatusRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(value)).shaped.<>({r=>import r._; _1.map(_=> DictEventVerifyStatusRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column value SqlType(varchar), PrimaryKey, Length(64,true) */
    val value: Rep[String] = column[String]("value", O.PrimaryKey, O.Length(64,varying=true))
  }
  /** Collection-like TableQuery object for table DictEventVerifyStatus */
  lazy val DictEventVerifyStatus = new TableQuery(tag => new DictEventVerifyStatus(tag))

  /** Entity class storing rows of table DictOrgClassify
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param value Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
  case class DictOrgClassifyRow(id: Int, value: String)
  /** GetResult implicit for fetching DictOrgClassifyRow objects using plain SQL queries */
  implicit def GetResultDictOrgClassifyRow(implicit e0: GR[Int], e1: GR[String]): GR[DictOrgClassifyRow] = GR{
    prs => import prs._
    DictOrgClassifyRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table DICT_ORG_CLASSIFY. Objects of this class serve as prototypes for rows in queries. */
  class DictOrgClassify(_tableTag: Tag) extends Table[DictOrgClassifyRow](_tableTag, "DICT_ORG_CLASSIFY") {
    def * = (id, value) <> (DictOrgClassifyRow.tupled, DictOrgClassifyRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(value)).shaped.<>({r=>import r._; _1.map(_=> DictOrgClassifyRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
    val value: Rep[String] = column[String]("value", O.PrimaryKey, O.Length(32,varying=true))
  }
  /** Collection-like TableQuery object for table DictOrgClassify */
  lazy val DictOrgClassify = new TableQuery(tag => new DictOrgClassify(tag))

  /** Entity class storing rows of table DictOrgType
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param value Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
  case class DictOrgTypeRow(id: Int, value: String)
  /** GetResult implicit for fetching DictOrgTypeRow objects using plain SQL queries */
  implicit def GetResultDictOrgTypeRow(implicit e0: GR[Int], e1: GR[String]): GR[DictOrgTypeRow] = GR{
    prs => import prs._
    DictOrgTypeRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table DICT_ORG_TYPE. Objects of this class serve as prototypes for rows in queries. */
  class DictOrgType(_tableTag: Tag) extends Table[DictOrgTypeRow](_tableTag, "DICT_ORG_TYPE") {
    def * = (id, value) <> (DictOrgTypeRow.tupled, DictOrgTypeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(value)).shaped.<>({r=>import r._; _1.map(_=> DictOrgTypeRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
    val value: Rep[String] = column[String]("value", O.PrimaryKey, O.Length(32,varying=true))
  }
  /** Collection-like TableQuery object for table DictOrgType */
  lazy val DictOrgType = new TableQuery(tag => new DictOrgType(tag))

  /** Entity class storing rows of table DictSchoolLevel
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param value Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
  case class DictSchoolLevelRow(id: Int, value: String)
  /** GetResult implicit for fetching DictSchoolLevelRow objects using plain SQL queries */
  implicit def GetResultDictSchoolLevelRow(implicit e0: GR[Int], e1: GR[String]): GR[DictSchoolLevelRow] = GR{
    prs => import prs._
    DictSchoolLevelRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table DICT_SCHOOL_LEVEL. Objects of this class serve as prototypes for rows in queries. */
  class DictSchoolLevel(_tableTag: Tag) extends Table[DictSchoolLevelRow](_tableTag, "DICT_SCHOOL_LEVEL") {
    def * = (id, value) <> (DictSchoolLevelRow.tupled, DictSchoolLevelRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(value)).shaped.<>({r=>import r._; _1.map(_=> DictSchoolLevelRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
    val value: Rep[String] = column[String]("value", O.PrimaryKey, O.Length(32,varying=true))
  }
  /** Collection-like TableQuery object for table DictSchoolLevel */
  lazy val DictSchoolLevel = new TableQuery(tag => new DictSchoolLevel(tag))

  /** Entity class storing rows of table DictSchoolNature
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param value Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
  case class DictSchoolNatureRow(id: Int, value: String)
  /** GetResult implicit for fetching DictSchoolNatureRow objects using plain SQL queries */
  implicit def GetResultDictSchoolNatureRow(implicit e0: GR[Int], e1: GR[String]): GR[DictSchoolNatureRow] = GR{
    prs => import prs._
    DictSchoolNatureRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table DICT_SCHOOL_NATURE. Objects of this class serve as prototypes for rows in queries. */
  class DictSchoolNature(_tableTag: Tag) extends Table[DictSchoolNatureRow](_tableTag, "DICT_SCHOOL_NATURE") {
    def * = (id, value) <> (DictSchoolNatureRow.tupled, DictSchoolNatureRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(value)).shaped.<>({r=>import r._; _1.map(_=> DictSchoolNatureRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column value SqlType(varchar), PrimaryKey, Length(32,true) */
    val value: Rep[String] = column[String]("value", O.PrimaryKey, O.Length(32,varying=true))
  }
  /** Collection-like TableQuery object for table DictSchoolNature */
  lazy val DictSchoolNature = new TableQuery(tag => new DictSchoolNature(tag))

  /** Entity class storing rows of table Event
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param org Database column org SqlType(varchar), Length(128,true)
   *  @param name Database column name SqlType(varchar), PrimaryKey, Length(255,true)
   *  @param classify Database column classify SqlType(varchar), Length(64,true)
   *  @param form Database column form SqlType(varchar), Length(64,true)
   *  @param tag Database column tag SqlType(varchar), Length(64,true), Default(None)
   *  @param image Database column image SqlType(varchar), Length(64,true)
   *  @param summary Database column summary SqlType(text), Default(None)
   *  @param ageLimitMin Database column age_limit_min SqlType(int4), Default(None)
   *  @param ageLimitMax Database column age_limit_max SqlType(int4), Default(None)
   *  @param timeSpan Database column time_span SqlType(varchar), Length(64,true), Default(None)
   *  @param allowParentsJoin Database column allow_parents_join SqlType(bool), Default(None)
   *  @param needManager Database column need_manager SqlType(bool), Default(None)
   *  @param notice Database column notice SqlType(text), Default(None)
   *  @param safeinfo Database column safeinfo SqlType(text), Default(None)
   *  @param publishCount Database column publish_count SqlType(int4), Default(Some(0))
   *  @param enabled Database column enabled SqlType(bool), Default(None) */
  case class EventRow(id: Int, org: String, name: String, classify: String, form: String, tag: Option[String] = None, image: String, summary: Option[String] = None, ageLimitMin: Option[Int] = None, ageLimitMax: Option[Int] = None, timeSpan: Option[String] = None, allowParentsJoin: Option[Boolean] = None, needManager: Option[Boolean] = None, notice: Option[String] = None, safeinfo: Option[String] = None, publishCount: Option[Int] = Some(0), enabled: Option[Boolean] = None)
  /** GetResult implicit for fetching EventRow objects using plain SQL queries */
  implicit def GetResultEventRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[Int]], e4: GR[Option[Boolean]]): GR[EventRow] = GR{
    prs => import prs._
    EventRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[String], <<?[String], <<[String], <<?[String], <<?[Int], <<?[Int], <<?[String], <<?[Boolean], <<?[Boolean], <<?[String], <<?[String], <<?[Int], <<?[Boolean]))
  }
  /** Table description of table EVENT. Objects of this class serve as prototypes for rows in queries. */
  class Event(_tableTag: Tag) extends Table[EventRow](_tableTag, "EVENT") {
    def * = (id, org, name, classify, form, tag, image, summary, ageLimitMin, ageLimitMax, timeSpan, allowParentsJoin, needManager, notice, safeinfo, publishCount, enabled) <> (EventRow.tupled, EventRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(org), Rep.Some(name), Rep.Some(classify), Rep.Some(form), tag, Rep.Some(image), summary, ageLimitMin, ageLimitMax, timeSpan, allowParentsJoin, needManager, notice, safeinfo, publishCount, enabled).shaped.<>({r=>import r._; _1.map(_=> EventRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7.get, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column org SqlType(varchar), Length(128,true) */
    val org: Rep[String] = column[String]("org", O.Length(128,varying=true))
    /** Database column name SqlType(varchar), PrimaryKey, Length(255,true) */
    val name: Rep[String] = column[String]("name", O.PrimaryKey, O.Length(255,varying=true))
    /** Database column classify SqlType(varchar), Length(64,true) */
    val classify: Rep[String] = column[String]("classify", O.Length(64,varying=true))
    /** Database column form SqlType(varchar), Length(64,true) */
    val form: Rep[String] = column[String]("form", O.Length(64,varying=true))
    /** Database column tag SqlType(varchar), Length(64,true), Default(None) */
    val tag: Rep[Option[String]] = column[Option[String]]("tag", O.Length(64,varying=true), O.Default(None))
    /** Database column image SqlType(varchar), Length(64,true) */
    val image: Rep[String] = column[String]("image", O.Length(64,varying=true))
    /** Database column summary SqlType(text), Default(None) */
    val summary: Rep[Option[String]] = column[Option[String]]("summary", O.Default(None))
    /** Database column age_limit_min SqlType(int4), Default(None) */
    val ageLimitMin: Rep[Option[Int]] = column[Option[Int]]("age_limit_min", O.Default(None))
    /** Database column age_limit_max SqlType(int4), Default(None) */
    val ageLimitMax: Rep[Option[Int]] = column[Option[Int]]("age_limit_max", O.Default(None))
    /** Database column time_span SqlType(varchar), Length(64,true), Default(None) */
    val timeSpan: Rep[Option[String]] = column[Option[String]]("time_span", O.Length(64,varying=true), O.Default(None))
    /** Database column allow_parents_join SqlType(bool), Default(None) */
    val allowParentsJoin: Rep[Option[Boolean]] = column[Option[Boolean]]("allow_parents_join", O.Default(None))
    /** Database column need_manager SqlType(bool), Default(None) */
    val needManager: Rep[Option[Boolean]] = column[Option[Boolean]]("need_manager", O.Default(None))
    /** Database column notice SqlType(text), Default(None) */
    val notice: Rep[Option[String]] = column[Option[String]]("notice", O.Default(None))
    /** Database column safeinfo SqlType(text), Default(None) */
    val safeinfo: Rep[Option[String]] = column[Option[String]]("safeinfo", O.Default(None))
    /** Database column publish_count SqlType(int4), Default(Some(0)) */
    val publishCount: Rep[Option[Int]] = column[Option[Int]]("publish_count", O.Default(Some(0)))
    /** Database column enabled SqlType(bool), Default(None) */
    val enabled: Rep[Option[Boolean]] = column[Option[Boolean]]("enabled", O.Default(None))
  }
  /** Collection-like TableQuery object for table Event */
  lazy val Event = new TableQuery(tag => new Event(tag))

  /** Entity class storing rows of table EventClassify
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param value Database column value SqlType(varchar), PrimaryKey, Length(64,true) */
  case class EventClassifyRow(id: Int, value: String)
  /** GetResult implicit for fetching EventClassifyRow objects using plain SQL queries */
  implicit def GetResultEventClassifyRow(implicit e0: GR[Int], e1: GR[String]): GR[EventClassifyRow] = GR{
    prs => import prs._
    EventClassifyRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table EVENT_CLASSIFY. Objects of this class serve as prototypes for rows in queries. */
  class EventClassify(_tableTag: Tag) extends Table[EventClassifyRow](_tableTag, "EVENT_CLASSIFY") {
    def * = (id, value) <> (EventClassifyRow.tupled, EventClassifyRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(value)).shaped.<>({r=>import r._; _1.map(_=> EventClassifyRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column value SqlType(varchar), PrimaryKey, Length(64,true) */
    val value: Rep[String] = column[String]("value", O.PrimaryKey, O.Length(64,varying=true))
  }
  /** Collection-like TableQuery object for table EventClassify */
  lazy val EventClassify = new TableQuery(tag => new EventClassify(tag))

  /** Entity class storing rows of table EventReview
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param name Database column name SqlType(varchar), PrimaryKey, Length(255,true)
   *  @param relationEvent Database column relation_event SqlType(varchar), Length(255,true)
   *  @param relationOrg Database column relation_org SqlType(varchar), Length(128,true)
   *  @param stayTop Database column stay_top SqlType(bool), Default(false)
   *  @param content Database column content SqlType(text)
   *  @param image Database column image SqlType(varchar), Length(255,true), Default(None) */
  case class EventReviewRow(id: Int, name: String, relationEvent: String, relationOrg: String, stayTop: Boolean = false, content: String, image: Option[String] = None)
  /** GetResult implicit for fetching EventReviewRow objects using plain SQL queries */
  implicit def GetResultEventReviewRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Boolean], e3: GR[Option[String]]): GR[EventReviewRow] = GR{
    prs => import prs._
    EventReviewRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[Boolean], <<[String], <<?[String]))
  }
  /** Table description of table EVENT_REVIEW. Objects of this class serve as prototypes for rows in queries. */
  class EventReview(_tableTag: Tag) extends Table[EventReviewRow](_tableTag, "EVENT_REVIEW") {
    def * = (id, name, relationEvent, relationOrg, stayTop, content, image) <> (EventReviewRow.tupled, EventReviewRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(relationEvent), Rep.Some(relationOrg), Rep.Some(stayTop), Rep.Some(content), image).shaped.<>({r=>import r._; _1.map(_=> EventReviewRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column name SqlType(varchar), PrimaryKey, Length(255,true) */
    val name: Rep[String] = column[String]("name", O.PrimaryKey, O.Length(255,varying=true))
    /** Database column relation_event SqlType(varchar), Length(255,true) */
    val relationEvent: Rep[String] = column[String]("relation_event", O.Length(255,varying=true))
    /** Database column relation_org SqlType(varchar), Length(128,true) */
    val relationOrg: Rep[String] = column[String]("relation_org", O.Length(128,varying=true))
    /** Database column stay_top SqlType(bool), Default(false) */
    val stayTop: Rep[Boolean] = column[Boolean]("stay_top", O.Default(false))
    /** Database column content SqlType(text) */
    val content: Rep[String] = column[String]("content")
    /** Database column image SqlType(varchar), Length(255,true), Default(None) */
    val image: Rep[Option[String]] = column[Option[String]]("image", O.Length(255,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table EventReview */
  lazy val EventReview = new TableQuery(tag => new EventReview(tag))

  /** Entity class storing rows of table ImagesStore
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param name Database column name SqlType(varchar), PrimaryKey, Length(64,true)
   *  @param meta Database column meta SqlType(varchar), Length(64,true), Default(None) */
  case class ImagesStoreRow(id: Int, name: String, meta: Option[String] = None)
  /** GetResult implicit for fetching ImagesStoreRow objects using plain SQL queries */
  implicit def GetResultImagesStoreRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[ImagesStoreRow] = GR{
    prs => import prs._
    ImagesStoreRow.tupled((<<[Int], <<[String], <<?[String]))
  }
  /** Table description of table IMAGES_STORE. Objects of this class serve as prototypes for rows in queries. */
  class ImagesStore(_tableTag: Tag) extends Table[ImagesStoreRow](_tableTag, "IMAGES_STORE") {
    def * = (id, name, meta) <> (ImagesStoreRow.tupled, ImagesStoreRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), meta).shaped.<>({r=>import r._; _1.map(_=> ImagesStoreRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column name SqlType(varchar), PrimaryKey, Length(64,true) */
    val name: Rep[String] = column[String]("name", O.PrimaryKey, O.Length(64,varying=true))
    /** Database column meta SqlType(varchar), Length(64,true), Default(None) */
    val meta: Rep[Option[String]] = column[Option[String]]("meta", O.Length(64,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table ImagesStore */
  lazy val ImagesStore = new TableQuery(tag => new ImagesStore(tag))

  /** Entity class storing rows of table InfoMeta
   *  @param name Database column name SqlType(varchar), PrimaryKey, Length(256,true)
   *  @param obj Database column obj SqlType(varchar), Length(128,true), Default(None)
   *  @param ext Database column ext SqlType(varchar), Length(256,true), Default(None)
   *  @param ext2 Database column ext2 SqlType(varchar), Length(256,true), Default(None) */
  case class InfoMetaRow(name: String, obj: Option[String] = None, ext: Option[String] = None, ext2: Option[String] = None)
  /** GetResult implicit for fetching InfoMetaRow objects using plain SQL queries */
  implicit def GetResultInfoMetaRow(implicit e0: GR[String], e1: GR[Option[String]]): GR[InfoMetaRow] = GR{
    prs => import prs._
    InfoMetaRow.tupled((<<[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table INFO_META. Objects of this class serve as prototypes for rows in queries. */
  class InfoMeta(_tableTag: Tag) extends Table[InfoMetaRow](_tableTag, "INFO_META") {
    def * = (name, obj, ext, ext2) <> (InfoMetaRow.tupled, InfoMetaRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(name), obj, ext, ext2).shaped.<>({r=>import r._; _1.map(_=> InfoMetaRow.tupled((_1.get, _2, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column name SqlType(varchar), PrimaryKey, Length(256,true) */
    val name: Rep[String] = column[String]("name", O.PrimaryKey, O.Length(256,varying=true))
    /** Database column obj SqlType(varchar), Length(128,true), Default(None) */
    val obj: Rep[Option[String]] = column[Option[String]]("obj", O.Length(128,varying=true), O.Default(None))
    /** Database column ext SqlType(varchar), Length(256,true), Default(None) */
    val ext: Rep[Option[String]] = column[Option[String]]("ext", O.Length(256,varying=true), O.Default(None))
    /** Database column ext2 SqlType(varchar), Length(256,true), Default(None) */
    val ext2: Rep[Option[String]] = column[Option[String]]("ext2", O.Length(256,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table InfoMeta */
  lazy val InfoMeta = new TableQuery(tag => new InfoMeta(tag))

  /** Entity class storing rows of table Org
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param isSchool Database column is_school SqlType(bool)
   *  @param email Database column email SqlType(varchar), PrimaryKey, Length(128,true)
   *  @param hashedPassword Database column hashed_password SqlType(varchar), Length(255,true)
   *  @param profiles Database column profiles SqlType(int4), Default(None)
   *  @param events Database column events SqlType(_int4), Length(10,false), Default(None) */
  case class OrgRow(id: Int, isSchool: Boolean, email: String, hashedPassword: String, profiles: Option[Int] = None, events: Option[String] = None)
  /** GetResult implicit for fetching OrgRow objects using plain SQL queries */
  implicit def GetResultOrgRow(implicit e0: GR[Int], e1: GR[Boolean], e2: GR[String], e3: GR[Option[Int]], e4: GR[Option[String]]): GR[OrgRow] = GR{
    prs => import prs._
    OrgRow.tupled((<<[Int], <<[Boolean], <<[String], <<[String], <<?[Int], <<?[String]))
  }
  /** Table description of table ORG. Objects of this class serve as prototypes for rows in queries. */
  class Org(_tableTag: Tag) extends Table[OrgRow](_tableTag, "ORG") {
    def * = (id, isSchool, email, hashedPassword, profiles, events) <> (OrgRow.tupled, OrgRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(isSchool), Rep.Some(email), Rep.Some(hashedPassword), profiles, events).shaped.<>({r=>import r._; _1.map(_=> OrgRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column is_school SqlType(bool) */
    val isSchool: Rep[Boolean] = column[Boolean]("is_school")
    /** Database column email SqlType(varchar), PrimaryKey, Length(128,true) */
    val email: Rep[String] = column[String]("email", O.PrimaryKey, O.Length(128,varying=true))
    /** Database column hashed_password SqlType(varchar), Length(255,true) */
    val hashedPassword: Rep[String] = column[String]("hashed_password", O.Length(255,varying=true))
    /** Database column profiles SqlType(int4), Default(None) */
    val profiles: Rep[Option[Int]] = column[Option[Int]]("profiles", O.Default(None))
    /** Database column events SqlType(_int4), Length(10,false), Default(None) */
    val events: Rep[Option[String]] = column[Option[String]]("events", O.Length(10,varying=false), O.Default(None))
  }
  /** Collection-like TableQuery object for table Org */
  lazy val Org = new TableQuery(tag => new Org(tag))

  /** Entity class storing rows of table OrgCert
   *  @param org Database column org SqlType(varchar), Length(128,true), Default(None)
   *  @param image Database column image SqlType(varchar), PrimaryKey, Length(256,true) */
  case class OrgCertRow(org: Option[String] = None, image: String)
  /** GetResult implicit for fetching OrgCertRow objects using plain SQL queries */
  implicit def GetResultOrgCertRow(implicit e0: GR[Option[String]], e1: GR[String]): GR[OrgCertRow] = GR{
    prs => import prs._
    OrgCertRow.tupled((<<?[String], <<[String]))
  }
  /** Table description of table ORG_CERT. Objects of this class serve as prototypes for rows in queries. */
  class OrgCert(_tableTag: Tag) extends Table[OrgCertRow](_tableTag, "ORG_CERT") {
    def * = (org, image) <> (OrgCertRow.tupled, OrgCertRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (org, Rep.Some(image)).shaped.<>({r=>import r._; _2.map(_=> OrgCertRow.tupled((_1, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column org SqlType(varchar), Length(128,true), Default(None) */
    val org: Rep[Option[String]] = column[Option[String]]("org", O.Length(128,varying=true), O.Default(None))
    /** Database column image SqlType(varchar), PrimaryKey, Length(256,true) */
    val image: Rep[String] = column[String]("image", O.PrimaryKey, O.Length(256,varying=true))
  }
  /** Collection-like TableQuery object for table OrgCert */
  lazy val OrgCert = new TableQuery(tag => new OrgCert(tag))

  /** Entity class storing rows of table OrgCounsellor
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param org Database column org SqlType(varchar), Length(128,true)
   *  @param name Database column name SqlType(varchar), Length(128,true)
   *  @param enabled Database column enabled SqlType(bool), Default(None)
   *  @param index Database column index SqlType(int4), Default(Some(10))
   *  @param show Database column show SqlType(bool), Default(Some(false)) */
  case class OrgCounsellorRow(id: Int, org: String, name: String, enabled: Option[Boolean] = None, index: Option[Int] = Some(10), show: Option[Boolean] = Some(false))
  /** GetResult implicit for fetching OrgCounsellorRow objects using plain SQL queries */
  implicit def GetResultOrgCounsellorRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Boolean]], e3: GR[Option[Int]]): GR[OrgCounsellorRow] = GR{
    prs => import prs._
    OrgCounsellorRow.tupled((<<[Int], <<[String], <<[String], <<?[Boolean], <<?[Int], <<?[Boolean]))
  }
  /** Table description of table ORG_COUNSELLOR. Objects of this class serve as prototypes for rows in queries. */
  class OrgCounsellor(_tableTag: Tag) extends Table[OrgCounsellorRow](_tableTag, "ORG_COUNSELLOR") {
    def * = (id, org, name, enabled, index, show) <> (OrgCounsellorRow.tupled, OrgCounsellorRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(org), Rep.Some(name), enabled, index, show).shaped.<>({r=>import r._; _1.map(_=> OrgCounsellorRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column org SqlType(varchar), Length(128,true) */
    val org: Rep[String] = column[String]("org", O.Length(128,varying=true))
    /** Database column name SqlType(varchar), Length(128,true) */
    val name: Rep[String] = column[String]("name", O.Length(128,varying=true))
    /** Database column enabled SqlType(bool), Default(None) */
    val enabled: Rep[Option[Boolean]] = column[Option[Boolean]]("enabled", O.Default(None))
    /** Database column index SqlType(int4), Default(Some(10)) */
    val index: Rep[Option[Int]] = column[Option[Int]]("index", O.Default(Some(10)))
    /** Database column show SqlType(bool), Default(Some(false)) */
    val show: Rep[Option[Boolean]] = column[Option[Boolean]]("show", O.Default(Some(false)))
  }
  /** Collection-like TableQuery object for table OrgCounsellor */
  lazy val OrgCounsellor = new TableQuery(tag => new OrgCounsellor(tag))

  /** Entity class storing rows of table OrgFuction
   *  @param counsellor Database column counsellor SqlType(jsonb), Length(2147483647,false), Default(None) */
  case class OrgFuctionRow(counsellor: Option[String] = None)
  /** GetResult implicit for fetching OrgFuctionRow objects using plain SQL queries */
  implicit def GetResultOrgFuctionRow(implicit e0: GR[Option[String]]): GR[OrgFuctionRow] = GR{
    prs => import prs._
    OrgFuctionRow(<<?[String])
  }
  /** Table description of table ORG_FUCTION. Objects of this class serve as prototypes for rows in queries. */
  class OrgFuction(_tableTag: Tag) extends Table[OrgFuctionRow](_tableTag, "ORG_FUCTION") {
    def * = counsellor <> (OrgFuctionRow, OrgFuctionRow.unapply)

    /** Database column counsellor SqlType(jsonb), Length(2147483647,false), Default(None) */
    val counsellor: Rep[Option[String]] = column[Option[String]]("counsellor", O.Length(2147483647,varying=false), O.Default(None))
  }
  /** Collection-like TableQuery object for table OrgFuction */
  lazy val OrgFuction = new TableQuery(tag => new OrgFuction(tag))

  /** Entity class storing rows of table OrgLetter
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param isRead Database column is_read SqlType(bool), Default(false)
   *  @param created Database column created SqlType(varchar), Length(64,true), Default(None)
   *  @param title Database column title SqlType(varchar), Length(128,true)
   *  @param content Database column content SqlType(text)
   *  @param to Database column to SqlType(varchar), Length(128,true)
   *  @param from Database column from SqlType(varchar), Length(128,true), Default(None) */
  case class OrgLetterRow(id: Int, isRead: Boolean = false, created: Option[String] = None, title: String, content: String, to: String, from: Option[String] = None)
  /** GetResult implicit for fetching OrgLetterRow objects using plain SQL queries */
  implicit def GetResultOrgLetterRow(implicit e0: GR[Int], e1: GR[Boolean], e2: GR[Option[String]], e3: GR[String]): GR[OrgLetterRow] = GR{
    prs => import prs._
    OrgLetterRow.tupled((<<[Int], <<[Boolean], <<?[String], <<[String], <<[String], <<[String], <<?[String]))
  }
  /** Table description of table ORG_LETTER. Objects of this class serve as prototypes for rows in queries. */
  class OrgLetter(_tableTag: Tag) extends Table[OrgLetterRow](_tableTag, "ORG_LETTER") {
    def * = (id, isRead, created, title, content, to, from) <> (OrgLetterRow.tupled, OrgLetterRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(isRead), created, Rep.Some(title), Rep.Some(content), Rep.Some(to), from).shaped.<>({r=>import r._; _1.map(_=> OrgLetterRow.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column is_read SqlType(bool), Default(false) */
    val isRead: Rep[Boolean] = column[Boolean]("is_read", O.Default(false))
    /** Database column created SqlType(varchar), Length(64,true), Default(None) */
    val created: Rep[Option[String]] = column[Option[String]]("created", O.Length(64,varying=true), O.Default(None))
    /** Database column title SqlType(varchar), Length(128,true) */
    val title: Rep[String] = column[String]("title", O.Length(128,varying=true))
    /** Database column content SqlType(text) */
    val content: Rep[String] = column[String]("content")
    /** Database column to SqlType(varchar), Length(128,true) */
    val to: Rep[String] = column[String]("to", O.Length(128,varying=true))
    /** Database column from SqlType(varchar), Length(128,true), Default(None) */
    val from: Rep[Option[String]] = column[Option[String]]("from", O.Length(128,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table OrgLetter */
  lazy val OrgLetter = new TableQuery(tag => new OrgLetter(tag))

  /** Entity class storing rows of table OrgManager
   *  @param org Database column org SqlType(varchar), Length(128,true), Default(None)
   *  @param user Database column user SqlType(varchar), Length(128,true), Default(None)
   *  @param enabled Database column enabled SqlType(bool), Default(None) */
  case class OrgManagerRow(org: Option[String] = None, user: Option[String] = None, enabled: Option[Boolean] = None)
  /** GetResult implicit for fetching OrgManagerRow objects using plain SQL queries */
  implicit def GetResultOrgManagerRow(implicit e0: GR[Option[String]], e1: GR[Option[Boolean]]): GR[OrgManagerRow] = GR{
    prs => import prs._
    OrgManagerRow.tupled((<<?[String], <<?[String], <<?[Boolean]))
  }
  /** Table description of table ORG_MANAGER. Objects of this class serve as prototypes for rows in queries. */
  class OrgManager(_tableTag: Tag) extends Table[OrgManagerRow](_tableTag, "ORG_MANAGER") {
    def * = (org, user, enabled) <> (OrgManagerRow.tupled, OrgManagerRow.unapply)

    /** Database column org SqlType(varchar), Length(128,true), Default(None) */
    val org: Rep[Option[String]] = column[Option[String]]("org", O.Length(128,varying=true), O.Default(None))
    /** Database column user SqlType(varchar), Length(128,true), Default(None) */
    val user: Rep[Option[String]] = column[Option[String]]("user", O.Length(128,varying=true), O.Default(None))
    /** Database column enabled SqlType(bool), Default(None) */
    val enabled: Rep[Option[Boolean]] = column[Option[Boolean]]("enabled", O.Default(None))
  }
  /** Collection-like TableQuery object for table OrgManager */
  lazy val OrgManager = new TableQuery(tag => new OrgManager(tag))

  /** Entity class storing rows of table OrgNotice
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param org Database column org SqlType(varchar), Length(64,true)
   *  @param content Database column content SqlType(text)
   *  @param created Database column created SqlType(date) */
  case class OrgNoticeRow(id: Int, org: String, content: String, created: java.sql.Date)
  /** GetResult implicit for fetching OrgNoticeRow objects using plain SQL queries */
  implicit def GetResultOrgNoticeRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Date]): GR[OrgNoticeRow] = GR{
    prs => import prs._
    OrgNoticeRow.tupled((<<[Int], <<[String], <<[String], <<[java.sql.Date]))
  }
  /** Table description of table ORG_NOTICE. Objects of this class serve as prototypes for rows in queries. */
  class OrgNotice(_tableTag: Tag) extends Table[OrgNoticeRow](_tableTag, "ORG_NOTICE") {
    def * = (id, org, content, created) <> (OrgNoticeRow.tupled, OrgNoticeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(org), Rep.Some(content), Rep.Some(created)).shaped.<>({r=>import r._; _1.map(_=> OrgNoticeRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column org SqlType(varchar), Length(64,true) */
    val org: Rep[String] = column[String]("org", O.Length(64,varying=true))
    /** Database column content SqlType(text) */
    val content: Rep[String] = column[String]("content")
    /** Database column created SqlType(date) */
    val created: Rep[java.sql.Date] = column[java.sql.Date]("created")
  }
  /** Collection-like TableQuery object for table OrgNotice */
  lazy val OrgNotice = new TableQuery(tag => new OrgNotice(tag))

  /** Entity class storing rows of table OrgProfiles
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param org Database column org SqlType(varchar), Length(128,true)
   *  @param name Database column name SqlType(varchar), Length(64,true)
   *  @param address Database column address SqlType(varchar), Length(255,true), Default(None)
   *  @param tel Database column tel SqlType(varchar), Length(64,true)
   *  @param province Database column province SqlType(varchar), Length(64,true)
   *  @param city Database column city SqlType(varchar), Length(64,true)
   *  @param nature Database column nature SqlType(varchar), Length(64,true), Default(None)
   *  @param code Database column code SqlType(varchar), Length(64,true)
   *  @param leadername Database column leadername SqlType(varchar), Length(64,true)
   *  @param leaderid Database column leaderid SqlType(varchar), Length(64,true)
   *  @param personnelcount Database column personnelcount SqlType(int4), Default(None)
   *  @param tags Database column tags SqlType(varchar), Length(64,true), Default(None)
   *  @param parent Database column parent SqlType(varchar), Length(256,true), Default(None)
   *  @param image Database column image SqlType(varchar), Length(128,true), Default(None)
   *  @param safe Database column safe SqlType(varchar), Length(64,true), Default(None)
   *  @param summary Database column summary SqlType(text), Default(None)
   *  @param orgType Database column org_type SqlType(varchar), Length(128,true), Default(None)
   *  @param classify Database column classify SqlType(varchar), Length(128,true), Default(None) */
  case class OrgProfilesRow(id: Int, org: String, name: String, address: Option[String] = None, tel: String, province: String, city: String, nature: Option[String] = None, code: String, leadername: String, leaderid: String, personnelcount: Option[Int] = None, tags: Option[String] = None, parent: Option[String] = None, image: Option[String] = None, safe: Option[String] = None, summary: Option[String] = None, orgType: String = "None", classify: String = "None")
  /** GetResult implicit for fetching OrgProfilesRow objects using plain SQL queries */
  implicit def GetResultOrgProfilesRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[Int]]): GR[OrgProfilesRow] = GR{
    prs => import prs._
    OrgProfilesRow.tupled((<<[Int], <<[String], <<[String], <<?[String], <<[String], <<[String], <<[String], <<?[String], <<[String], <<[String], <<[String], <<?[Int], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<[String], <<[String]))
  }
  /** Table description of table ORG_PROFILES. Objects of this class serve as prototypes for rows in queries. */
  class OrgProfiles(_tableTag: Tag) extends Table[OrgProfilesRow](_tableTag, "ORG_PROFILES") {
    def * = (id, org, name, address, tel, province, city, nature, code, leadername, leaderid, personnelcount, tags, parent, image, safe, summary, orgType, classify) <> (OrgProfilesRow.tupled, OrgProfilesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(org), Rep.Some(name), address, Rep.Some(tel), Rep.Some(province), Rep.Some(city), nature, Rep.Some(code), Rep.Some(leadername), Rep.Some(leaderid), personnelcount, tags, parent, image, safe, summary, Rep.Some(orgType), Rep.Some(classify)).shaped.<>({r=>import r._; _1.map(_=> OrgProfilesRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6.get, _7.get, _8, _9.get, _10.get, _11.get, _12, _13, _14, _15, _16, _17, _18.get, _19.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column org SqlType(varchar), Length(128,true) */
    val org: Rep[String] = column[String]("org", O.Length(128,varying=true))
    /** Database column name SqlType(varchar), Length(64,true) */
    val name: Rep[String] = column[String]("name", O.Length(64,varying=true))
    /** Database column address SqlType(varchar), Length(255,true), Default(None) */
    val address: Rep[Option[String]] = column[Option[String]]("address", O.Length(255,varying=true), O.Default(None))
    /** Database column tel SqlType(varchar), Length(64,true) */
    val tel: Rep[String] = column[String]("tel", O.Length(64,varying=true))
    /** Database column province SqlType(varchar), Length(64,true) */
    val province: Rep[String] = column[String]("province", O.Length(64,varying=true))
    /** Database column city SqlType(varchar), Length(64,true) */
    val city: Rep[String] = column[String]("city", O.Length(64,varying=true))
    /** Database column nature SqlType(varchar), Length(64,true), Default(None) */
    val nature: Rep[Option[String]] = column[Option[String]]("nature", O.Length(64,varying=true), O.Default(None))
    /** Database column code SqlType(varchar), Length(64,true) */
    val code: Rep[String] = column[String]("code", O.Length(64,varying=true))
    /** Database column leadername SqlType(varchar), Length(64,true) */
    val leadername: Rep[String] = column[String]("leadername", O.Length(64,varying=true))
    /** Database column leaderid SqlType(varchar), Length(64,true) */
    val leaderid: Rep[String] = column[String]("leaderid", O.Length(64,varying=true))
    /** Database column personnelcount SqlType(int4), Default(None) */
    val personnelcount: Rep[Option[Int]] = column[Option[Int]]("personnelcount", O.Default(None))
    /** Database column tags SqlType(varchar), Length(64,true), Default(None) */
    val tags: Rep[Option[String]] = column[Option[String]]("tags", O.Length(64,varying=true), O.Default(None))
    /** Database column parent SqlType(varchar), Length(256,true), Default(None) */
    val parent: Rep[Option[String]] = column[Option[String]]("parent", O.Length(256,varying=true), O.Default(None))
    /** Database column image SqlType(varchar), Length(128,true), Default(None) */
    val image: Rep[Option[String]] = column[Option[String]]("image", O.Length(128,varying=true), O.Default(None))
    /** Database column safe SqlType(varchar), Length(64,true), Default(None) */
    val safe: Rep[Option[String]] = column[Option[String]]("safe", O.Length(64,varying=true), O.Default(None))
    /** Database column summary SqlType(text), Default(None) */
    val summary: Rep[Option[String]] = column[Option[String]]("summary", O.Default(None))
    /** Database column org_type SqlType(varchar), Length(128,true), Default(None) */
    val orgType: Rep[String] = column[String]("org_type", O.Length(128,varying=true), O.Default("None"))
    /** Database column classify SqlType(varchar), Length(128,true), Default(None) */
    val classify: Rep[String] = column[String]("classify", O.Length(128,varying=true), O.Default("None"))
  }
  /** Collection-like TableQuery object for table OrgProfiles */
  lazy val OrgProfiles = new TableQuery(tag => new OrgProfiles(tag))

  /** Entity class storing rows of table Playground
   *  @param js Database column js SqlType(jsonb), Length(2147483647,false), Default(None) */
  case class PlaygroundRow(js: Option[String] = None)
  /** GetResult implicit for fetching PlaygroundRow objects using plain SQL queries */
  implicit def GetResultPlaygroundRow(implicit e0: GR[Option[String]]): GR[PlaygroundRow] = GR{
    prs => import prs._
    PlaygroundRow(<<?[String])
  }
  /** Table description of table PLAYGROUND. Objects of this class serve as prototypes for rows in queries. */
  class Playground(_tableTag: Tag) extends Table[PlaygroundRow](_tableTag, "PLAYGROUND") {
    def * = js <> (PlaygroundRow, PlaygroundRow.unapply)

    /** Database column js SqlType(jsonb), Length(2147483647,false), Default(None) */
    val js: Rep[Option[String]] = column[Option[String]]("js", O.Length(2147483647,varying=false), O.Default(None))
  }
  /** Collection-like TableQuery object for table Playground */
  lazy val Playground = new TableQuery(tag => new Playground(tag))

  /** Entity class storing rows of table PublishedEvent
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param org Database column org SqlType(varchar), Length(128,true)
   *  @param status Database column status SqlType(varchar), Length(32,true), Default(None)
   *  @param template Database column template SqlType(varchar), Length(128,true)
   *  @param venues Database column venues SqlType(varchar), Length(128,true)
   *  @param startTime Database column start_time SqlType(varchar), Length(64,true)
   *  @param endTime Database column end_time SqlType(varchar), Length(64,true)
   *  @param peopleLimit Database column people_limit SqlType(int4)
   *  @param counsellor Database column counsellor SqlType(varchar), Length(64,true), Default(None)
   *  @param image Database column image SqlType(varchar), Length(255,true), Default(None)
   *  @param originPrice Database column origin_price SqlType(int4)
   *  @param currentPrice Database column current_price SqlType(int4)
   *  @param insurancePrice Database column insurance_price SqlType(int4)
   *  @param priceExplain Database column price_explain SqlType(text)
   *  @param changeCause Database column change_cause SqlType(varchar), Length(512,true), Default(None)
   *  @param name Database column name SqlType(varchar), Length(255,true), Default(None) */
  case class PublishedEventRow(id: Int, org: String, status: Option[String] = None, template: String, venues: String, startTime: String, endTime: String, peopleLimit: Int, counsellor: Option[String] = None, image: Option[String] = None, originPrice: Int, currentPrice: Int, insurancePrice: Int, priceExplain: String, changeCause: Option[String] = None, name: Option[String] = None)
  /** GetResult implicit for fetching PublishedEventRow objects using plain SQL queries */
  implicit def GetResultPublishedEventRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[PublishedEventRow] = GR{
    prs => import prs._
    PublishedEventRow.tupled((<<[Int], <<[String], <<?[String], <<[String], <<[String], <<[String], <<[String], <<[Int], <<?[String], <<?[String], <<[Int], <<[Int], <<[Int], <<[String], <<?[String], <<?[String]))
  }
  /** Table description of table PUBLISHED_EVENT. Objects of this class serve as prototypes for rows in queries. */
  class PublishedEvent(_tableTag: Tag) extends Table[PublishedEventRow](_tableTag, "PUBLISHED_EVENT") {
    def * = (id, org, status, template, venues, startTime, endTime, peopleLimit, counsellor, image, originPrice, currentPrice, insurancePrice, priceExplain, changeCause, name) <> (PublishedEventRow.tupled, PublishedEventRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(org), status, Rep.Some(template), Rep.Some(venues), Rep.Some(startTime), Rep.Some(endTime), Rep.Some(peopleLimit), counsellor, image, Rep.Some(originPrice), Rep.Some(currentPrice), Rep.Some(insurancePrice), Rep.Some(priceExplain), changeCause, name).shaped.<>({r=>import r._; _1.map(_=> PublishedEventRow.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get, _7.get, _8.get, _9, _10, _11.get, _12.get, _13.get, _14.get, _15, _16)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column org SqlType(varchar), Length(128,true) */
    val org: Rep[String] = column[String]("org", O.Length(128,varying=true))
    /** Database column status SqlType(varchar), Length(32,true), Default(None) */
    val status: Rep[Option[String]] = column[Option[String]]("status", O.Length(32,varying=true), O.Default(None))
    /** Database column template SqlType(varchar), Length(128,true) */
    val template: Rep[String] = column[String]("template", O.Length(128,varying=true))
    /** Database column venues SqlType(varchar), Length(128,true) */
    val venues: Rep[String] = column[String]("venues", O.Length(128,varying=true))
    /** Database column start_time SqlType(varchar), Length(64,true) */
    val startTime: Rep[String] = column[String]("start_time", O.Length(64,varying=true))
    /** Database column end_time SqlType(varchar), Length(64,true) */
    val endTime: Rep[String] = column[String]("end_time", O.Length(64,varying=true))
    /** Database column people_limit SqlType(int4) */
    val peopleLimit: Rep[Int] = column[Int]("people_limit")
    /** Database column counsellor SqlType(varchar), Length(64,true), Default(None) */
    val counsellor: Rep[Option[String]] = column[Option[String]]("counsellor", O.Length(64,varying=true), O.Default(None))
    /** Database column image SqlType(varchar), Length(255,true), Default(None) */
    val image: Rep[Option[String]] = column[Option[String]]("image", O.Length(255,varying=true), O.Default(None))
    /** Database column origin_price SqlType(int4) */
    val originPrice: Rep[Int] = column[Int]("origin_price")
    /** Database column current_price SqlType(int4) */
    val currentPrice: Rep[Int] = column[Int]("current_price")
    /** Database column insurance_price SqlType(int4) */
    val insurancePrice: Rep[Int] = column[Int]("insurance_price")
    /** Database column price_explain SqlType(text) */
    val priceExplain: Rep[String] = column[String]("price_explain")
    /** Database column change_cause SqlType(varchar), Length(512,true), Default(None) */
    val changeCause: Rep[Option[String]] = column[Option[String]]("change_cause", O.Length(512,varying=true), O.Default(None))
    /** Database column name SqlType(varchar), Length(255,true), Default(None) */
    val name: Rep[Option[String]] = column[Option[String]]("name", O.Length(255,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table PublishedEvent */
  lazy val PublishedEvent = new TableQuery(tag => new PublishedEvent(tag))

  /** Entity class storing rows of table Strategy
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param evevt Database column evevt SqlType(int4)
   *  @param title Database column title SqlType(varchar), Length(255,true)
   *  @param content Database column content SqlType(text), Default(None)
   *  @param img Database column img SqlType(varchar), Length(255,true), Default(None)
   *  @param pubTime Database column pub_time SqlType(date)
   *  @param click Database column click SqlType(int4), Default(Some(0))
   *  @param likes Database column likes SqlType(int4), Default(Some(0))
   *  @param org Database column org SqlType(varchar), Length(255,true) */
  case class StrategyRow(id: Int, evevt: Int, title: String, content: Option[String] = None, img: Option[String] = None, pubTime: java.sql.Date, click: Option[Int] = Some(0), likes: Option[Int] = Some(0), org: String)
  /** GetResult implicit for fetching StrategyRow objects using plain SQL queries */
  implicit def GetResultStrategyRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[java.sql.Date], e4: GR[Option[Int]]): GR[StrategyRow] = GR{
    prs => import prs._
    StrategyRow.tupled((<<[Int], <<[Int], <<[String], <<?[String], <<?[String], <<[java.sql.Date], <<?[Int], <<?[Int], <<[String]))
  }
  /** Table description of table STRATEGY. Objects of this class serve as prototypes for rows in queries. */
  class Strategy(_tableTag: Tag) extends Table[StrategyRow](_tableTag, "STRATEGY") {
    def * = (id, evevt, title, content, img, pubTime, click, likes, org) <> (StrategyRow.tupled, StrategyRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(evevt), Rep.Some(title), content, img, Rep.Some(pubTime), click, likes, Rep.Some(org)).shaped.<>({r=>import r._; _1.map(_=> StrategyRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6.get, _7, _8, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column evevt SqlType(int4) */
    val evevt: Rep[Int] = column[Int]("evevt")
    /** Database column title SqlType(varchar), Length(255,true) */
    val title: Rep[String] = column[String]("title", O.Length(255,varying=true))
    /** Database column content SqlType(text), Default(None) */
    val content: Rep[Option[String]] = column[Option[String]]("content", O.Default(None))
    /** Database column img SqlType(varchar), Length(255,true), Default(None) */
    val img: Rep[Option[String]] = column[Option[String]]("img", O.Length(255,varying=true), O.Default(None))
    /** Database column pub_time SqlType(date) */
    val pubTime: Rep[java.sql.Date] = column[java.sql.Date]("pub_time")
    /** Database column click SqlType(int4), Default(Some(0)) */
    val click: Rep[Option[Int]] = column[Option[Int]]("click", O.Default(Some(0)))
    /** Database column likes SqlType(int4), Default(Some(0)) */
    val likes: Rep[Option[Int]] = column[Option[Int]]("likes", O.Default(Some(0)))
    /** Database column org SqlType(varchar), Length(255,true) */
    val org: Rep[String] = column[String]("org", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table Strategy */
  lazy val Strategy = new TableQuery(tag => new Strategy(tag))

  /** Entity class storing rows of table Test
   *  @param field1 Database column field1 SqlType(varchar), Length(128,true), Default(None)
   *  @param field2 Database column field2 SqlType(int4), Default(None) */
  case class TestRow(field1: Option[String] = None, field2: Option[Int] = None)
  /** GetResult implicit for fetching TestRow objects using plain SQL queries */
  implicit def GetResultTestRow(implicit e0: GR[Option[String]], e1: GR[Option[Int]]): GR[TestRow] = GR{
    prs => import prs._
    TestRow.tupled((<<?[String], <<?[Int]))
  }
  /** Table description of table test. Objects of this class serve as prototypes for rows in queries. */
  class Test(_tableTag: Tag) extends Table[TestRow](_tableTag, "test") {
    def * = (field1, field2) <> (TestRow.tupled, TestRow.unapply)

    /** Database column field1 SqlType(varchar), Length(128,true), Default(None) */
    val field1: Rep[Option[String]] = column[Option[String]]("field1", O.Length(128,varying=true), O.Default(None))
    /** Database column field2 SqlType(int4), Default(None) */
    val field2: Rep[Option[Int]] = column[Option[Int]]("field2", O.Default(None))
  }
  /** Collection-like TableQuery object for table Test */
  lazy val Test = new TableQuery(tag => new Test(tag))

  /** Entity class storing rows of table User
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param sex Database column sex SqlType(SEX), Default(None)
   *  @param certified Database column certified SqlType(bool), Default(None)
   *  @param loginName Database column login_name SqlType(varchar), Length(16,true)
   *  @param realName Database column real_name SqlType(varchar), Length(32,true), Default(None)
   *  @param nichName Database column nich_name SqlType(varchar), Length(32,true), Default(None)
   *  @param avatar Database column avatar SqlType(varchar), Length(255,true), Default(None)
   *  @param mobile Database column mobile SqlType(varchar), Length(11,true)
   *  @param email Database column email SqlType(varchar), Length(128,true), Default(None)
   *  @param brith Database column brith SqlType(date), Default(None)
   *  @param hashedPassword Database column hashed_password SqlType(varchar), Length(128,true)
   *  @param city Database column city SqlType(int4), Default(None)
   *  @param userSocial Database column user_social SqlType(int4), Default(None) */
  case class UserRow(id: Int, sex: Option[String] = None, certified: Option[Boolean] = None, loginName: String, realName: Option[String] = None, nichName: Option[String] = None, avatar: Option[String] = None, mobile: String, email: Option[String] = None, brith: Option[java.sql.Date] = None, hashedPassword: String, city: Option[Int] = None, userSocial: Option[Int] = None)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[Option[Boolean]], e3: GR[String], e4: GR[Option[java.sql.Date]], e5: GR[Option[Int]]): GR[UserRow] = GR{
    prs => import prs._
    UserRow.tupled((<<[Int], <<?[String], <<?[Boolean], <<[String], <<?[String], <<?[String], <<?[String], <<[String], <<?[String], <<?[java.sql.Date], <<[String], <<?[Int], <<?[Int]))
  }
  /** Table description of table USER. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends Table[UserRow](_tableTag, "USER") {
    def * = (id, sex, certified, loginName, realName, nichName, avatar, mobile, email, brith, hashedPassword, city, userSocial) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), sex, certified, Rep.Some(loginName), realName, nichName, avatar, Rep.Some(mobile), email, brith, Rep.Some(hashedPassword), city, userSocial).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2, _3, _4.get, _5, _6, _7, _8.get, _9, _10, _11.get, _12, _13)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column sex SqlType(SEX), Default(None) */
    val sex: Rep[Option[String]] = column[Option[String]]("sex", O.Default(None))
    /** Database column certified SqlType(bool), Default(None) */
    val certified: Rep[Option[Boolean]] = column[Option[Boolean]]("certified", O.Default(None))
    /** Database column login_name SqlType(varchar), Length(16,true) */
    val loginName: Rep[String] = column[String]("login_name", O.Length(16,varying=true))
    /** Database column real_name SqlType(varchar), Length(32,true), Default(None) */
    val realName: Rep[Option[String]] = column[Option[String]]("real_name", O.Length(32,varying=true), O.Default(None))
    /** Database column nich_name SqlType(varchar), Length(32,true), Default(None) */
    val nichName: Rep[Option[String]] = column[Option[String]]("nich_name", O.Length(32,varying=true), O.Default(None))
    /** Database column avatar SqlType(varchar), Length(255,true), Default(None) */
    val avatar: Rep[Option[String]] = column[Option[String]]("avatar", O.Length(255,varying=true), O.Default(None))
    /** Database column mobile SqlType(varchar), Length(11,true) */
    val mobile: Rep[String] = column[String]("mobile", O.Length(11,varying=true))
    /** Database column email SqlType(varchar), Length(128,true), Default(None) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Length(128,varying=true), O.Default(None))
    /** Database column brith SqlType(date), Default(None) */
    val brith: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("brith", O.Default(None))
    /** Database column hashed_password SqlType(varchar), Length(128,true) */
    val hashedPassword: Rep[String] = column[String]("hashed_password", O.Length(128,varying=true))
    /** Database column city SqlType(int4), Default(None) */
    val city: Rep[Option[Int]] = column[Option[Int]]("city", O.Default(None))
    /** Database column user_social SqlType(int4), Default(None) */
    val userSocial: Rep[Option[Int]] = column[Option[Int]]("user_social", O.Default(None))

    /** Uniqueness Index over (loginName) (database name USER_login_name_key) */
    val index1 = index("USER_login_name_key", loginName, unique=true)
    /** Uniqueness Index over (mobile) (database name USER_mobile_key) */
    val index2 = index("USER_mobile_key", mobile, unique=true)
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))

  /** Entity class storing rows of table UserEvent
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param userid Database column userid SqlType(int4)
   *  @param username Database column username SqlType(varchar), Length(128,true)
   *  @param eventid Database column eventid SqlType(int4)
   *  @param eventname Database column eventname SqlType(varchar), Length(255,true)
   *  @param joinedTime Database column joined_time SqlType(date) */
  case class UserEventRow(id: Int, userid: Int, username: String, eventid: Int, eventname: String, joinedTime: java.sql.Date)
  /** GetResult implicit for fetching UserEventRow objects using plain SQL queries */
  implicit def GetResultUserEventRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Date]): GR[UserEventRow] = GR{
    prs => import prs._
    UserEventRow.tupled((<<[Int], <<[Int], <<[String], <<[Int], <<[String], <<[java.sql.Date]))
  }
  /** Table description of table USER_EVENT. Objects of this class serve as prototypes for rows in queries. */
  class UserEvent(_tableTag: Tag) extends Table[UserEventRow](_tableTag, "USER_EVENT") {
    def * = (id, userid, username, eventid, eventname, joinedTime) <> (UserEventRow.tupled, UserEventRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userid), Rep.Some(username), Rep.Some(eventid), Rep.Some(eventname), Rep.Some(joinedTime)).shaped.<>({r=>import r._; _1.map(_=> UserEventRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column userid SqlType(int4) */
    val userid: Rep[Int] = column[Int]("userid")
    /** Database column username SqlType(varchar), Length(128,true) */
    val username: Rep[String] = column[String]("username", O.Length(128,varying=true))
    /** Database column eventid SqlType(int4) */
    val eventid: Rep[Int] = column[Int]("eventid")
    /** Database column eventname SqlType(varchar), Length(255,true) */
    val eventname: Rep[String] = column[String]("eventname", O.Length(255,varying=true))
    /** Database column joined_time SqlType(date) */
    val joinedTime: Rep[java.sql.Date] = column[java.sql.Date]("joined_time")
  }
  /** Collection-like TableQuery object for table UserEvent */
  lazy val UserEvent = new TableQuery(tag => new UserEvent(tag))

  /** Entity class storing rows of table UserEventLittle
   *  @param uid Database column uid SqlType(int4)
   *  @param event Database column event SqlType(int4) */
  case class UserEventLittleRow(uid: Int, event: Int)
  /** GetResult implicit for fetching UserEventLittleRow objects using plain SQL queries */
  implicit def GetResultUserEventLittleRow(implicit e0: GR[Int]): GR[UserEventLittleRow] = GR{
    prs => import prs._
    UserEventLittleRow.tupled((<<[Int], <<[Int]))
  }
  /** Table description of table USER_EVENT_LITTLE. Objects of this class serve as prototypes for rows in queries. */
  class UserEventLittle(_tableTag: Tag) extends Table[UserEventLittleRow](_tableTag, "USER_EVENT_LITTLE") {
    def * = (uid, event) <> (UserEventLittleRow.tupled, UserEventLittleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(uid), Rep.Some(event)).shaped.<>({r=>import r._; _1.map(_=> UserEventLittleRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column uid SqlType(int4) */
    val uid: Rep[Int] = column[Int]("uid")
    /** Database column event SqlType(int4) */
    val event: Rep[Int] = column[Int]("event")
  }
  /** Collection-like TableQuery object for table UserEventLittle */
  lazy val UserEventLittle = new TableQuery(tag => new UserEventLittle(tag))

  /** Entity class storing rows of table UserEvolution
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey */
  case class UserEvolutionRow(id: Int)
  /** GetResult implicit for fetching UserEvolutionRow objects using plain SQL queries */
  implicit def GetResultUserEvolutionRow(implicit e0: GR[Int]): GR[UserEvolutionRow] = GR{
    prs => import prs._
    UserEvolutionRow(<<[Int])
  }
  /** Table description of table USER_EVOLUTION. Objects of this class serve as prototypes for rows in queries. */
  class UserEvolution(_tableTag: Tag) extends Table[UserEvolutionRow](_tableTag, "USER_EVOLUTION") {
    def * = id <> (UserEvolutionRow, UserEvolutionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = Rep.Some(id).shaped.<>(r => r.map(_=> UserEvolutionRow(r.get)), (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table UserEvolution */
  lazy val UserEvolution = new TableQuery(tag => new UserEvolution(tag))

  /** Entity class storing rows of table UserIdentityInfo
   *  @param userId Database column user_id SqlType(int4), PrimaryKey
   *  @param photo Database column photo SqlType(varchar), Length(255,true), Default(None)
   *  @param idType Database column id_type SqlType(varchar), Length(16,true)
   *  @param idValue Database column id_value SqlType(varchar), Length(128,true)
   *  @param nation Database column nation SqlType(int4), Default(None) */
  case class UserIdentityInfoRow(userId: Int, photo: Option[String] = None, idType: String, idValue: String, nation: Option[Int] = None)
  /** GetResult implicit for fetching UserIdentityInfoRow objects using plain SQL queries */
  implicit def GetResultUserIdentityInfoRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[String], e3: GR[Option[Int]]): GR[UserIdentityInfoRow] = GR{
    prs => import prs._
    UserIdentityInfoRow.tupled((<<[Int], <<?[String], <<[String], <<[String], <<?[Int]))
  }
  /** Table description of table USER_IDENTITY_INFO. Objects of this class serve as prototypes for rows in queries. */
  class UserIdentityInfo(_tableTag: Tag) extends Table[UserIdentityInfoRow](_tableTag, "USER_IDENTITY_INFO") {
    def * = (userId, photo, idType, idValue, nation) <> (UserIdentityInfoRow.tupled, UserIdentityInfoRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userId), photo, Rep.Some(idType), Rep.Some(idValue), nation).shaped.<>({r=>import r._; _1.map(_=> UserIdentityInfoRow.tupled((_1.get, _2, _3.get, _4.get, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_id SqlType(int4), PrimaryKey */
    val userId: Rep[Int] = column[Int]("user_id", O.PrimaryKey)
    /** Database column photo SqlType(varchar), Length(255,true), Default(None) */
    val photo: Rep[Option[String]] = column[Option[String]]("photo", O.Length(255,varying=true), O.Default(None))
    /** Database column id_type SqlType(varchar), Length(16,true) */
    val idType: Rep[String] = column[String]("id_type", O.Length(16,varying=true))
    /** Database column id_value SqlType(varchar), Length(128,true) */
    val idValue: Rep[String] = column[String]("id_value", O.Length(128,varying=true))
    /** Database column nation SqlType(int4), Default(None) */
    val nation: Rep[Option[Int]] = column[Option[Int]]("nation", O.Default(None))
  }
  /** Collection-like TableQuery object for table UserIdentityInfo */
  lazy val UserIdentityInfo = new TableQuery(tag => new UserIdentityInfo(tag))

  /** Entity class storing rows of table UserTrackInfo
   *  @param userId Database column user_id SqlType(int4), PrimaryKey
   *  @param registerTime Database column register_time SqlType(date)
   *  @param lastLoginTime Database column last_login_time SqlType(timestamp), Default(None)
   *  @param lastLoginIp Database column last_login_ip SqlType(inet), Length(2147483647,false), Default(None) */
  case class UserTrackInfoRow(userId: Int, registerTime: java.sql.Date, lastLoginTime: Option[java.sql.Timestamp] = None, lastLoginIp: Option[String] = None)
  /** GetResult implicit for fetching UserTrackInfoRow objects using plain SQL queries */
  implicit def GetResultUserTrackInfoRow(implicit e0: GR[Int], e1: GR[java.sql.Date], e2: GR[Option[java.sql.Timestamp]], e3: GR[Option[String]]): GR[UserTrackInfoRow] = GR{
    prs => import prs._
    UserTrackInfoRow.tupled((<<[Int], <<[java.sql.Date], <<?[java.sql.Timestamp], <<?[String]))
  }
  /** Table description of table USER_TRACK_INFO. Objects of this class serve as prototypes for rows in queries. */
  class UserTrackInfo(_tableTag: Tag) extends Table[UserTrackInfoRow](_tableTag, "USER_TRACK_INFO") {
    def * = (userId, registerTime, lastLoginTime, lastLoginIp) <> (UserTrackInfoRow.tupled, UserTrackInfoRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userId), Rep.Some(registerTime), lastLoginTime, lastLoginIp).shaped.<>({r=>import r._; _1.map(_=> UserTrackInfoRow.tupled((_1.get, _2.get, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_id SqlType(int4), PrimaryKey */
    val userId: Rep[Int] = column[Int]("user_id", O.PrimaryKey)
    /** Database column register_time SqlType(date) */
    val registerTime: Rep[java.sql.Date] = column[java.sql.Date]("register_time")
    /** Database column last_login_time SqlType(timestamp), Default(None) */
    val lastLoginTime: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("last_login_time", O.Default(None))
    /** Database column last_login_ip SqlType(inet), Length(2147483647,false), Default(None) */
    val lastLoginIp: Rep[Option[String]] = column[Option[String]]("last_login_ip", O.Length(2147483647,varying=false), O.Default(None))
  }
  /** Collection-like TableQuery object for table UserTrackInfo */
  lazy val UserTrackInfo = new TableQuery(tag => new UserTrackInfo(tag))

  /** Entity class storing rows of table Venues
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param org Database column org SqlType(varchar), Length(128,true)
   *  @param name Database column name SqlType(varchar), Length(128,true)
   *  @param area Database column area SqlType(int4), Default(None)
   *  @param cap Database column cap SqlType(int4), Default(None)
   *  @param by Database column by SqlType(varchar), Length(32,true), Default(None)
   *  @param image Database column image SqlType(varchar), Length(256,true), Default(None)
   *  @param imageType Database column image_type SqlType(varchar), Length(16,true), Default(None)
   *  @param location Database column location SqlType(varchar), Length(256,true), Default(None) */
  case class VenuesRow(id: Int, org: String, name: String, area: Option[Int] = None, cap: Option[Int] = None, by: Option[String] = None, image: Option[String] = None, imageType: Option[String] = None, location: Option[String] = None)
  /** GetResult implicit for fetching VenuesRow objects using plain SQL queries */
  implicit def GetResultVenuesRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Int]], e3: GR[Option[String]]): GR[VenuesRow] = GR{
    prs => import prs._
    VenuesRow.tupled((<<[Int], <<[String], <<[String], <<?[Int], <<?[Int], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table VENUES. Objects of this class serve as prototypes for rows in queries. */
  class Venues(_tableTag: Tag) extends Table[VenuesRow](_tableTag, "VENUES") {
    def * = (id, org, name, area, cap, by, image, imageType, location) <> (VenuesRow.tupled, VenuesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(org), Rep.Some(name), area, cap, by, image, imageType, location).shaped.<>({r=>import r._; _1.map(_=> VenuesRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column org SqlType(varchar), Length(128,true) */
    val org: Rep[String] = column[String]("org", O.Length(128,varying=true))
    /** Database column name SqlType(varchar), Length(128,true) */
    val name: Rep[String] = column[String]("name", O.Length(128,varying=true))
    /** Database column area SqlType(int4), Default(None) */
    val area: Rep[Option[Int]] = column[Option[Int]]("area", O.Default(None))
    /** Database column cap SqlType(int4), Default(None) */
    val cap: Rep[Option[Int]] = column[Option[Int]]("cap", O.Default(None))
    /** Database column by SqlType(varchar), Length(32,true), Default(None) */
    val by: Rep[Option[String]] = column[Option[String]]("by", O.Length(32,varying=true), O.Default(None))
    /** Database column image SqlType(varchar), Length(256,true), Default(None) */
    val image: Rep[Option[String]] = column[Option[String]]("image", O.Length(256,varying=true), O.Default(None))
    /** Database column image_type SqlType(varchar), Length(16,true), Default(None) */
    val imageType: Rep[Option[String]] = column[Option[String]]("image_type", O.Length(16,varying=true), O.Default(None))
    /** Database column location SqlType(varchar), Length(256,true), Default(None) */
    val location: Rep[Option[String]] = column[Option[String]]("location", O.Length(256,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Venues */
  lazy val Venues = new TableQuery(tag => new Venues(tag))
}
