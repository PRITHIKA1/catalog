package io.nexure.discount.plugin

import io.ktor.server.application.Application
import io.nexure.discount.controller.configureProductController
import io.nexure.discount.service.ProductService
import org.kodein.di.instance
import kotlin.getValue

const val API_URL_PREFIX = "/api/discount"

fun Application.configureRouting() {
    val productService: ProductService by DIConfig.di.instance()
    configureProductController(productService)
}
