import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.johnrengelman.shadow") version "5.2.0"
    kotlin("jvm") version "1.6.10"
}

group = "fr.ensimag"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx.spark:kotlin-spark-api-3.0:1.0.2")

    compileOnly("org.apache.spark:spark-sql_2.13:3.2.0")
    implementation("org.apache.kafka:kafka-streams:3.0.0")
    implementation("org.apache.spark:spark-streaming_2.13:3.2.0")
    implementation("org.apache.spark:spark-streaming-kafka_2.11:1.6.3")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    dependencies {
        exclude {
            it.moduleGroup == "org.apache.spark" || it.moduleGroup == "org.scala-lang"
        }
    }
}

