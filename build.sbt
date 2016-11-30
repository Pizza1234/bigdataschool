name := "bigdataschool"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.0.2",
  "org.apache.spark" %% "spark-sql" % "2.0.2",
  "org.apache.spark" %% "spark-streaming" % "2.0.2",
  "org.apache.spark" %% "spark-streaming-kafka-0-8" % "2.0.2",
  "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.0-M3",
  "org.json4s" %% "json4s-jackson" % "3.5.0"
)