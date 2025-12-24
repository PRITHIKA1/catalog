package io.nexure.discount.controller

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.nexure.discount.exceptions.DiscountException
import io.nexure.discount.model.dto.ApplyDiscountRequest
import io.nexure.discount.plugin.API_URL_PREFIX
import io.nexure.discount.service.ProductService
import io.nexure.discount.util.ApplicationConstants.HeaderConstants.COUNTRY
import io.nexure.discount.util.ApplicationConstants.HeaderConstants.PRODUCT_ID
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Application.configureProductController(productService: ProductService) {
    val logger: Logger = LoggerFactory.getLogger(javaClass)

    routing {
        route(API_URL_PREFIX) {
            get("/products") {
                try {
                    val country: String? = call.request.queryParameters[COUNTRY]
                    logger.info("Getting products for country: $country")
                    val products = productService.fetchAllProductsByCountry(country)

                    call.respond(HttpStatusCode.OK, products)
                } catch (ex: DiscountException) {
                    throw ex
                } catch (e: Exception) {
                    logger.error("Error getting products", e)
                    throw DiscountException("Error getting products", e)
                }
            }

            put("/products/{id}/discount") {
                try {
                    val productId: String? = call.parameters[PRODUCT_ID]
                    val discountRequest: ApplyDiscountRequest = call.receive()

                    logger.info("Updating discount for product: $productId")

                    productService.updateDiscount(
                        productId, discountRequest
                    )

                    call.respond(
                        HttpStatusCode.OK,
                        mapOf(
                            "status" to 200,
                            "message" to "Data updated Successfully"
                        )
                    )
                } catch (ex: DiscountException) {
                    throw ex
                } catch (e: Exception) {
                    logger.error("Error updating product", e)
                    throw DiscountException("Error updating products", e)
                }
            }
        }
    }
}
