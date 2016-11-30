val logFile = "G:\\Coursera\\BigDataSchoolKharkiv\\uservisits"

val logRDD = spark.sparkContext.textFile(logFile).map(_.trim.split(","))
val filteredLog = logRDD.filter(attributes => attributes.length >= 6 && attributes(5).length == 3)
val logDF = filteredLog.map(attributes => (attributes(5),1)).reduceByKey((a,b) => a+b).toDF()

logDF.createOrReplaceTempView("countries")

val countryDF = spark.sql("SELECT _1 as country, _2 as visits FROM countries ORDER BY visits DESC LIMIT 10")

countryDF.show()


