package io.nexure.discount.plugin

import io.ktor.server.application.Application
import io.nexure.discount.controller.configureProductController

const val API_URL_PREFIX = "/api/discount"

fun Application.configureRouting() {
    configureProductController()
}
