name := "akkaflow"
version := "2.2"
scalaVersion := "2.11.8"
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {
  val akkaVersion = "2.5.1"  
  Seq(
    "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j"      % akkaVersion,
    "ch.qos.logback"    %  "logback-classic" % "1.1.3",
    "com.typesafe.akka" %% "akka-testkit"    % akkaVersion   % "test",
    "org.scalatest"     %% "scalatest"       % "2.2.0"       % "test",
    "com.typesafe.akka" %% "akka-remote"     % akkaVersion,
    "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion % "test",
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-distributed-data" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
    "mysql" % "mysql-connector-java" % "5.1.12",
    "com.github.philcali" %% "cronish" % "0.1.3",
    "org.json4s" % "json4s-jackson_2.11" % "3.5.0",
    "org.apache.commons" % "commons-email" % "1.4",
    "com.typesafe.akka" %% "akka-http" % "10.0.1"
  )
}
