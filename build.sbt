lazy val akkaHttpVersion = "10.2.8"
lazy val akkaVersion     = "2.7.0"
lazy val circeVersion    = "0.14.1"

scalaVersion    := "2.13.4"
name := "my-bank-app"

libraryDependencies ++= Seq(
  "com.typesafe.akka"  %% "akka-http"                  % akkaHttpVersion,
  "com.typesafe.akka"  %% "akka-actor-typed"           % akkaVersion,
  "com.typesafe.akka"  %% "akka-stream"                % akkaVersion,
  "com.typesafe.akka"  %% "akka-persistence-typed"     % akkaVersion,
  "com.datastax.oss"   %  "java-driver-core"           % "4.13.0",   // https://github.com/akka/alpakka/issues/2556
  "com.lightbend.akka" %% "akka-persistence-jdbc"      % "5.2.0",
  "com.typesafe.akka"  %% "akka-persistence-query"     % akkaVersion,
  "io.circe"           %% "circe-core"                 % circeVersion,
  "io.circe"           %% "circe-generic"              % circeVersion,
  "io.circe"           %% "circe-parser"               % circeVersion,
  "de.heikoseeberger"  %% "akka-http-circe"            % "1.39.2",
  "ch.qos.logback"     %  "logback-classic"            % "1.2.10",
  "com.github.tminglei" %% "slick-pg"                  % "0.20.3",
  "com.github.tminglei" %% "slick-pg_play-json"        % "0.20.3",

  "com.typesafe.akka"  %% "akka-http-testkit"          % akkaHttpVersion % Test,
  "com.typesafe.akka"  %% "akka-actor-testkit-typed"   % akkaVersion     % Test,
  "org.scalatest"      %% "scalatest"                  % "3.2.9"         % Test,

  "com.typesafe.slick" %% "slick" % "3.4.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1",
  "org.postgresql"     % "postgresql"                  % "42.5.1",
  "com.github.tminglei" %% "slick-pg" % "0.21.0",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.21.0"
)

