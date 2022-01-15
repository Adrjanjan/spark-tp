package fr.ensimag.clusterdataproducer.kafka

import fr.ensimag.clusterdataproducer.generator.TaskEvent
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer


@Configuration
class KafkaConfiguration {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val bootstrapAddress = "localhost:9092"

    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        logger.trace("Kafka admin bean created")
        val configs: MutableMap<String, Any> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        return KafkaAdmin(configs)
    }

    @Bean
    fun topic(): NewTopic {
        return TopicBuilder.name("task-events")
            .partitions(10)
            .replicas(1)
            .build()
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, TaskEvent> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, TaskEvent> {
        return KafkaTemplate(producerFactory())
    }
}