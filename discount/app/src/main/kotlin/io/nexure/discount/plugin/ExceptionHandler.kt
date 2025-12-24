package io.nexure.discount.plugin

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.nexure.discount.exceptions.*
import io.nexure.discount.model.dto.ErrorResponse
import io.nexure.discount.model.enum.ErrorCode

fun Application.configureExceptionHandling() {
    install(StatusPages) {
        exception<ProductNotFoundException> { call, cause ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(
                    errorCode = ErrorCode.ERR_101,
                    message = cause.message ?: "Product not found"
                )
            )
        }

        exception<DiscountAlreadyAppliedException> { call, cause ->
            call.respond(
                HttpStatusCode.Conflict,
                ErrorResponse(
                    errorCode = ErrorCode.ERR_102,
                    message = cause.message ?: "Discount already applied"
                )
            )
        }

        exception<DatabaseOperationException> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    errorCode = ErrorCode.ERR_103,
                    message = cause.message ?: "Database operation failed"
                )
            )
        }

        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled exception", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    errorCode = ErrorCode.ERR_104,
                    message = "Unexpected server error"
                )
            )
        }

        exception<InvalidDiscountException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    errorCode = ErrorCode.ERR_105,
                    message = cause.message ?: "Invalid Discount value"
                )
            )
        }

        exception<InvalidProductIdException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    errorCode = ErrorCode.ERR_106,
                    message = cause.message ?: "Invalid ProductId value"
                )
            )
        }

        exception<InvalidCountryException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    errorCode = ErrorCode.ERR_107,
                    message = cause.message ?: "Invalid Country code"
                )
            )
        }
    }
}
