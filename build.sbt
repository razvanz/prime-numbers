name := "prime_streamer"
version := "1.0"
scalaVersion := "2.13.2"

lazy val akkaVersion = "2.6.9"
lazy val akkaHttpVersion = "10.2.0"
lazy val akkaGrpcVersion = "1.0.2"

enablePlugins(AkkaGrpcPlugin)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
  "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
  "com.typesafe.akka" %% "akka-discovery"           % akkaVersion,
  "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
  "ch.qos.logback"    % "logback-classic"           % "1.2.3",

  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
  "com.typesafe.akka" %% "akka-stream-testkit"      % akkaVersion     % Test,
  "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
  "org.scalatest"     %% "scalatest"                % "3.1.1"         % Test
)
