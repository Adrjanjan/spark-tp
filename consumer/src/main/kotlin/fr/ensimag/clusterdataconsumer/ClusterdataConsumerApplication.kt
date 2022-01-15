package fr.ensimag.clusterdataconsumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClusterdataConsumerApplication

fun main(args: Array<String>) {
    runApplication<ClusterdataConsumerApplication>(*args)
}
