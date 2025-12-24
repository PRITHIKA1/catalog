package io.nexure.discount

import io.ktor.server.application.Application
import io.nexure.discount.config.Configuration
import io.nexure.discount.plugin.configureExceptionHandling
import io.nexure.discount.plugin.configureRouting
import io.nexure.discount.plugin.configureSerialization

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    Configuration.initConfig(this.environment)
    configureRouting()
    configureSerialization()
    configureExceptionHandling()
}
