package fr.ensimag.clusterdataconsumer.consumer

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "consumer")
data class ConsumerConfiguration(
    val kafkaHost: String = "localhost",
    val kafkaPort: Int = 9092,
    val broker: String = ""
)