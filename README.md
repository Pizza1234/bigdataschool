# Big Data School

Reads logging data from file and send to Kafka via Spark in json format.
Another process recieves data frim Kafka and saves to Cassandra.
Kafka and Cassandra should be started with default settings on
localhost.

## Project Description

A. UserVisits dataset (src\main\resources\uservisits) contains server
 logs for different web pages. Data format is: 
    - sourceIP VARCHAR(116)
	- destURL VARCHAR(100)
	- visitDate DATE
	- adRevenue FLOAT
	- userAgent VARCHAR(256)
	- countryCode CHAR(3)
	- languageCode CHAR(6)
	- searchWord VARCHAR(32)
	- duration INT

B. In a spark-shell, output top 10 countries from which visits occurred
 with aggregated number of visits (src\main\resources\script).

C. Create Spark job (Spark API is available for Java, Scala, Python
 and R) which calculates top 10 countries and produce these values in
 JSON format to Kafka (src\main\scala\LogSparkKafka.scala).

D. Create one more Spark Job, which listens to Kafka, parses published
 messages and saves them to Cassandra
 (src\main\scala\KafkaSparkCasandra.scala).

E. Published on Github: https://github.com/Pizza1234/bigdataschool

## Running Kafka

Before running Spark jobs Kafka and Cassandra should be started with
default settings on localhost.

To start Kafka in Linux, goto Kafka Home and run:

    > bin/zookeeper-server-start.sh config/zookeeper.properties
    
and 

    > bin/kafka-server-start.sh config/server.properties
    
To start Kafka in Windows, goto Kafka Home and run:

    > bin\windows\zookeeper-server-start.bat config\zookeeper.properties
    
and 

    > bin\windows\kafka-server-start.bat config\server.properties

## Creating a topic in Kafka

Before running jobs topic in Cassandra should be created.

It can be done with a windows command:

    > bin\windows\kafka-topics.bat --create --zookeeper localhost:2181
     --replication-factor 1 --partitions 1 --topic test

In Linux:

    > bin/kafka-topics.sh --create --zookeeper localhost:2181
     --replication-factor 1 --partitions 1 --topic test
    
## Running Cassandra

The easiest way to start Cassandra on Windows:

    apache-cassandra-3.9>bin\cassandra -f

On Linux:

    apache-cassandra-3.9>bin/cassandra -f

After this run CQL shell with a command (Windows) 

    apache-cassandra-3.9>bin\cqlsh

## Creating Table in Cassandra

Create KEYSPACE:
 
    CREATE KEYSPACE spark WITH replication = {'class': 'SimpleStrategy',
     'replication_factor': 1};

And TABLE:

    CREATE TABLE spark.countries (country text PRIMARY KEY, visits int);

## Running Spark Shell Script

Spark shell can be accessed with:

    spark-2.0.2-bin-hadoop2.6>bin\spark-shell.cmd
    
Top 10 countries by count of visits can be shown in Spark shell
with a help of commands listed in script (src\main\resources\script).

## Running jobs

First run KafkaSparkCassandra (src\main\scala\KafkaSparkCasandra.scala).
It will listen to Kafka and recieve data in json format and save to
created Cassandra table.

Then run LogSparkKafka (src\main\scala\LogSparkKafka.scala)
job. It will read data from the log file and send to Kafka as json
string.


