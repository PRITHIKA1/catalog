package io.nexure.discount.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.pluginOrNull
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.jackson.jackson
import java.text.SimpleDateFormat


fun Application.configureSerialization() {
    if (pluginOrNull(ContentNegotiation) == null) {
        install(ContentNegotiation) {
            jackson {
                dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            }
        }
    }
}