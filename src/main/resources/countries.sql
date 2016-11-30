CREATE KEYSPACE spark WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
USE spark;
CREATE TABLE spark.countries (country text PRIMARY KEY, visits int);
#DROP TABLE countries;
#SELECT * FROM countries;
