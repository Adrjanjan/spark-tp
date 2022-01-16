@file:JvmName("ConsumerApp")

package fr.ensimag.clusterdataconsumer

import org.jetbrains.kotlinx.spark.api.withCached
import org.jetbrains.kotlinx.spark.api.withSpark

class Consumer(
    val kafkaHost: String = "localhost",
    val kafkaPort: Int = 9092,
    val broker: String = ""
) {

    fun main() {
        val logFile = "/opt/spark/README.md" // Change to your Spark Home path
        withSpark {
            spark.read().textFile(logFile).withCached {
                val numAs = filter { it.contains("a") }.count()
                val numBs = filter { it.contains("b") }.count()
                println("Lines with a: $numAs, lines with b: $numBs")
            }
        }
    }

}
