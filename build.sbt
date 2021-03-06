
javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

lazy val root = (project in file(".")).
  settings(
    name := "letsencrypt-elb-lambda",
    version := "1.0",
    scalaVersion := "2.11.8",
    retrieveManaged := true,
    libraryDependencies ++= {
      Seq(
        "com.amazonaws" % "aws-lambda-java-core"   % "1.1.0",
        "com.amazonaws" % "aws-lambda-java-events" % "1.3.0",
        "com.amazonaws" % "aws-java-sdk-s3" % "1.11.41",
        "com.amazonaws" % "aws-java-sdk-elasticloadbalancing" % "1.11.41",
        "com.fasterxml.jackson.module"  %% "jackson-module-scala" % "2.7.8",
        "com.typesafe" % "config" % "1.3.1",
        "org.scalatest" %% "scalatest" % "3.0.0" % "test"
      )
    }
  )

test in assembly := {}

assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
   }
