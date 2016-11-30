import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import com.datastax.spark.connector._
import org.json4s._
import org.json4s.jackson.JsonMethods._

object KafkaSparkCasandra extends App {

  case class CountryVisit(country: String, visits: Int)

  val sparkKafkaConf = new SparkConf().setAppName("KafkaSparkCassandra").setMaster("local[*]")
    .set("spark.cassandra.connection.host", "127.0.0.1")
  //.set("spark.cassandra.connection.native.port", "9042")
  //.set("spark.cassandra.connection.rpc.port", "9160")

  val ssc = new StreamingContext(sparkKafkaConf, Seconds(2))
  val sc = ssc.sparkContext
  //ssc.checkpoint("checkpoint")

  val topicMap = Map("test" -> 1);
  val lines = KafkaUtils.createStream(ssc, "localhost:2181", "group", topicMap).map(_._2)
  implicit val formats = DefaultFormats
  val stockParsed = lines.map(line => parse(line).extract[CountryVisit])

  stockParsed.foreachRDD(rdd => rdd.saveToCassandra("spark", "countries", SomeColumns("country", "visits")))

  ssc.start()
  ssc.awaitTermination()
}

