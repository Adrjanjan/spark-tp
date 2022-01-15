package fr.ensimag.clusterdataproducer.generator

import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors
import java.util.zip.GZIPInputStream
import kotlin.io.path.Path
import kotlin.io.path.isDirectory


@Component
class Generator(
    private val kafkaTemplate: KafkaTemplate<String, TaskEvent>,
    private val generatorConfiguration: GeneratorConfiguration
) {

    private val logger = LoggerFactory.getLogger(javaClass)


    fun ungzip(content: Path): String = GZIPInputStream(
        content.toFile().readBytes().inputStream()
    ).bufferedReader(StandardCharsets.UTF_8).use { it.readText() }

    @EventListener(ApplicationReadyEvent::class)
    fun generate() {

        val filePaths = Files.list(Path(generatorConfiguration.directory))
//            .peek { logger.error(it.name) }
            .filter { !it.isDirectory() }
            .sorted()
            .collect(Collectors.toList())

        listOf(filePaths[0]).stream().forEachOrdered { path ->
            logger.error("Started generating tasks for path $path")
            ungzip(path).split("\n")
                .stream()
                .map { line -> TaskEvent(line) }
                .filter { task -> task.time != 0L }
                .forEach {
                    sendMessage(it)
                    Thread.sleep(100)
                }
        }
    }

    fun sendMessage(message: TaskEvent) {
        val future = kafkaTemplate.send(generatorConfiguration.kafkaAddress, message)
        if (generatorConfiguration.logSending) {
            future.addCallback(
                { result ->
                    logger.trace("Sent message=[$message] with offset=[$result!!.recordMetadata.offset()]")
                },
                { ex ->
                    logger.error("Unable to send message=[$message] due to : $ex.message")
                }
            )
        }
    }
}