package fr.ensimag.clusterdataproducer

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties
@SpringBootApplication
class ClusterdataProducerApplication

fun main(args: Array<String>) {
    runApplication<ClusterdataProducerApplication>(*args)
    val logger = LoggerFactory.getLogger("ClusterdataProducerApplication")
    logger.trace("Started app????")
}

