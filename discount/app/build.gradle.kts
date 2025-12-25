val logbackVersion: String by project
val kodeinVersion: String by project
val ktorVersion: String by project
val jacksonVersion: String by project

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    application
}
repositories {
    mavenCentral()
    mavenLocal()
}
dependencies {

    // Ktor core
    implementation("io.ktor:ktor-server-core-jvm:2.3.7")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.7")

    // Dependency Injection
    implementation("org.kodein.di:kodein-di:${kodeinVersion}")

    // JSON + Jackson
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.7")
    implementation("io.ktor:ktor-serialization-jackson-jvm:2.3.7")

    // Global exception handling
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.7")

    // MongoDB + KMongo
    implementation("org.litote.kmongo:kmongo-coroutine:4.11.0")
    implementation("org.litote.kmongo:kmongo-jackson-mapping:4.11.0")
    implementation("org.mongodb:mongodb-driver-reactivestreams:4.11.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Tests
    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.7")
    testImplementation("io.ktor:ktor-client-core:2.3.7")
    testImplementation("io.ktor:ktor-client-cio:2.3.7")
    testImplementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.testcontainers:mongodb:1.19.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.1")
    testImplementation("io.mockk:mockk:1.13.8")
}

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }
application { mainClass = "io.nexure.discount.ApplicationKt" }

tasks {
    test {
        useJUnitPlatform()
        testLogging { events("passed", "skipped", "failed") }
    }
}

