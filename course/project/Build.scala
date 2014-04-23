import sbt._
import Keys._
import play.Project._
import com.github.play2war.plugin._

object ApplicationBuild extends Build {

  val appName         = "profwell"
  val appVersion      = "test"

  val appDependencies = Seq(
    "mysql" % "mysql-connector-java" % "5.1.6",

    "junit" % "junit" % "4.11",

    "commons-io" % "commons-io" % "2.4",
    "commons-dbcp" % "commons-dbcp" % "1.4",

    "org.hibernate.javax.persistence" % "hibernate-jpa-2.0-api" % "1.0.1.Final",

    "org.hibernate" % "hibernate-entitymanager" % "4.2.0.Final",

    "org.hibernate" % "hibernate-core" % "4.2.0.Final",

    javaCore,
    javaJdbc,
    javaJpa
  )

  val main = play.Project(appName, appVersion, appDependencies)
    .settings(Play2WarPlugin.play2WarSettings: _*)
    .settings(Play2WarKeys.servletVersion := "2.5")

}
