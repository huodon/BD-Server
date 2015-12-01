package util

object Config{
  // connection info for a pre-populated throw-away, in-memory db for this demo, which is freshly initialized on every run
  val scriptsPath = ""
  val initScripts = Seq("drop-tables.sql","create-tables.sql","populate-tables.sql")
  val host = "120.25.252.244"
  val url = "jdbc:postgresql://120.25.252.244/admin?user=sa"
  val jdbcDriver =  "org.postgresql.Driver"
  val slickProfile = module.slick.SlickPostgresDriver
}
