package fr.ensimag.clusterdataconsumer.kafka

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


@Component
@ConfigurationProperties(prefix = "kafka")
class KafkaConfiguration(
    val broker: String = "",
    val topic: String = "task-events",


)