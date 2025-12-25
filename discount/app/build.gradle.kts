val logbackVersion: String by project
val kodeinVersion: String by project
val ktorVersion: String by project
val kmongoVersion: String by project
val mongoDriverVersion: String by project
val coroutinesVersion: String by project
val junitVersion: String by project
val junitPlatformVersion: String by project
val mockkVersion: String by project

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
    implementation("io.ktor:ktor-server-core-jvm:${ktorVersion}")
    implementation("io.ktor:ktor-server-netty-jvm:${ktorVersion}")

    // Dependency Injection
    implementation("org.kodein.di:kodein-di:${kodeinVersion}")

    // JSON + Jackson
    implementation("io.ktor:ktor-server-content-negotiation-jvm:${ktorVersion}")
    implementation("io.ktor:ktor-serialization-jackson-jvm:${ktorVersion}")

    // Global exception handling
    implementation("io.ktor:ktor-server-status-pages-jvm:${ktorVersion}")

    // MongoDB + KMongo
    implementation("org.litote.kmongo:kmongo-coroutine:${kmongoVersion}")
    implementation("org.litote.kmongo:kmongo-jackson-mapping:${kmongoVersion}")
    implementation("org.mongodb:mongodb-driver-reactivestreams:${mongoDriverVersion}")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")

    // Logging
    implementation("ch.qos.logback:logback-classic:${logbackVersion}")

    // Tests
    testImplementation("io.ktor:ktor-server-tests-jvm:${ktorVersion}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${coroutinesVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter:${junitVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${junitPlatformVersion}")
    testImplementation("io.mockk:mockk:${mockkVersion}")
}

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }
application { mainClass = "io.nexure.discount.ApplicationKt" }

tasks {
    test {
        useJUnitPlatform()
        testLogging { events("passed", "skipped", "failed") }
    }
}

