package fr.ensimag.clusterdataproducer.generator

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "generator")
data class GeneratorConfiguration(
    var directory: String = "",
    var kafkaAddress: String = "",
    val logSending: Boolean = false
)