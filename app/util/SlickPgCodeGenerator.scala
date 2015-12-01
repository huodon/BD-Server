package util

import Config._
import slick.profile.SqlProfile.ColumnOption
import play.api.Logger.{logger => LOG}

import scala.util.{Success, Failure}

/**
 * This customizes the Slick code generator. We only do simple name mappings.
 * For a more advanced example see https://github.com/cvogt/slick-presentation/tree/scala-exchange-2013
 */
object SlickPgCodeGenerator {

  import scala.concurrent.ExecutionContext.Implicits.global

  def run() = {

    LOG.info("Start")
    // write the generated results to file
    codegenFuture onComplete {
      case Success(codegen) =>
        LOG.info("Writing codes")
        codegen.writeToFile(
          "modules.slick.SlickPostgresDriver", // use our customized postgres driver
          "app",
          "modules.slick",
          "Tables",
          "Tables.scala"
        )
        LOG.info("Final")

      case Failure(t) =>
        LOG.error(t.getMessage)
        sys.exit(1)
    }
  }

  val db = slickProfile.api.Database.forURL(url, driver = jdbcDriver,password = "Action!@#$")
  // filter out desired tables
  // val included = Seq("USER", "ORG", "EVENT")
  val modelFuture = db.run(slickProfile.createModel())

  modelFuture onComplete {
    case Success(model) =>
      LOG.info(s"Found Tables: ${model.tables.map(t=>t.name.table)}")
    case Failure(t) =>
      LOG.error(t.getMessage)
  }

  val codegenFuture = modelFuture.map { model =>
    new slick.codegen.SourceCodeGenerator(model) {
      override def Table = new Table(_) {
        table =>
        override def Column = new Column(_) {
          column =>
          // customize db type -> scala type mapping, pls adjust it according to your environment
          override def rawType: String = model.tpe match {
            case "java.sql.Date" => "org.joda.time.LocalDate"
            case "java.sql.Time" => "org.joda.time.LocalTime"
            case "java.sql.Timestamp" => "org.joda.time.LocalDateTime"
            // currently, all types that's not built-in support were mapped to `String`
            case "String" => model.options.find(_.isInstanceOf[ColumnOption.SqlType])
              .map(_.asInstanceOf[ColumnOption.SqlType].typeName).map({
              case "hstore" => "Map[String, String]"
              case "geometry" => "com.vividsolutions.jts.geom.Geometry"
              case "int8[]" => "List[Long]"
              case "int[]" => "List[Int]"
              case _ => "String"
            }).getOrElse("String")
            case _ => super.rawType.asInstanceOf[String]
          }
        }
      }

      // ensure to use our customized postgres driver at `import profile.simple._`
      override def packageCode(profile: String, pkg: String, container: String, parentType: Option[String]): String = {
        s"""
package $pkg
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object $container extends {
  val profile = $profile
} with $container

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait $container${parentType.map(t => s" extends $t").getOrElse("")} {
  val profile: $profile
  import profile.simple._
  ${indent(code)}
}
      """.trim()
      }
    }
  }
}