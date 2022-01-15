package fr.ensimag.clusterdataconsumer.consumer

import com.fasterxml.jackson.databind.JsonDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.streaming.*
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka.*
import org.jetbrains.kotlinx.spark.api.*
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component


@Component
class Consumer(
    private val consumerConfiguration: ConsumerConfiguration
) {

    @EventListener(ApplicationReadyEvent::class)
    fun consume() {
        val conf = SparkConf().setMaster("local[2]").setAppName("KafkaTaskEventStreaming")
        val streamingContext = StreamingContext(conf, Durations.seconds(1))

        val kafkaParams = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to consumerConfiguration.broker,
            ConsumerConfig.GROUP_ID_CONFIG to 1,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class
        )
        val tasks = KafkaUtils.createDirectStream<String, TaskEvent>(
            streamingContext,
            LocationStrategies.PreferConsistent,
            ConsumerStrategies.Subscribe<String, TaskEvent>(topicsSet, kafkaParams)
        )

        // Get the lines, split them into words, count the words and print
        val lines = tasks.map { it.value }
        val words = lines.flatMap { it.split(" ") }
        val wordCounts = words.map { x -> [x, 1L]) }.reduceByKey { (a, b) -> a + b }
        wordCounts.print()

        // Start the computation
        streamingContext.start()
        streamingContext.awaitTermination()
    }

    fun consume2() {
        withSpark {

        }
    }

}