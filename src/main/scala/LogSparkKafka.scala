import java.util.HashMap

import org.apache.kafka.clients.producer.{ProducerRecord, KafkaProducer, ProducerConfig}
import org.apache.spark.sql.{DataFrame, SparkSession}

object LogSparkKafka {
  def main(args: Array[String]) {
    val logFile = "src/main/resources/uservisits" // Should be some file on your system
    val spark = SparkSession
        .builder()
        .appName("LogSparkKafka")
        .master("local[1]")
        .getOrCreate()

    // For implicit conversions from RDDs to DataFrames
    import spark.implicits._

    val logDF = spark.sparkContext
      .textFile(logFile)
      .map(_.trim.split(","))
      .filter(attributes => attributes.length >= 6 && attributes(5).length == 3)
      .map(attributes => (attributes(5), 1))
      .reduceByKey((a, b) => a + b)
      .toDF()

    // Register the DataFrame as a temporary view
    logDF.createOrReplaceTempView("countries")

    val countryDF = spark.sql("SELECT _1 as country, _2 as visits FROM countries ORDER BY visits DESC LIMIT 10")

    countryDF.show()
    sendToKafka(countryDF)

  }

  def sendToKafka(df: DataFrame) {
    // Zookeeper connection properties
    val props = new HashMap[String, Object]()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")


    // Send messages in json to Kafka
    df.toJSON.foreachPartition(partition => {
      val producer = new KafkaProducer[String, String](props)
      partition.foreach(row => {
        val message = new ProducerRecord[String, String]("test", null, row)
        producer.send(message)
      })
    })

  }
}