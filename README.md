# Analyzing Data with Spark TP

Project uses [ClusterData form Google 2011 traces](https://github.com/google/cluster-data/blob/master/ClusterData2011_2.md) specifically from _task-event_ table and consists of Stream producer and Stream Consumer connected with Kafka.

##  [Stream producer](producer) 

Simple application build with Spring & Kotlin to read data from _task-event_ files and forward it do Kafka broker. 


## [Stream consumer](consumer)

Simple application build with Spring & Kotlin to perform analytics on _task-event_ data using Apache Spark Streaming. 


## [Kafka](kafka)

Directory consisting only of [docker-compose](kafka/docker-compose.yml) file responsible for setting up Kafka and Zookeeper. 



