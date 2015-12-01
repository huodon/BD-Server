import sbt.Keys._
import sbt._


object BandouBuild extends Build {
//  val root = (project in file("."))
//             .settings(Project.defaultSettings ++ bandouSettings)
//
//
//  val bandouSettings = Seq(
//    scalaVersion := "2.11.7",
//    libraryDependencies ++= Seq(
//
//    ),
//    slick <<= slickCodeGenTask,
//    sourceGenerators in Compile <+= slickCodeGenTask
//  )

//  lazy val slick = TaskKey[Seq[File]]("gen-tables")
//  lazy val slickCodeGenTask = (
//    sourceManaged,
//    dependencyClasspath in Compile,
//    runner in Compile,
//    streams) map { (dir, cp, runner, stream) =>
//    val outDir = (dir / "slick").toPath
//    val url = "jdbc:postgresql://120.25.252.244:5432/bandou"
//    val jdbcDriver = "org.postgres.Driver"
//    val slickDriver = ""
//    val pkg = ""
////    toError(runner.run(""))
//    val name = outDir + "/"
//    Seq(file(name))
//  }
}

