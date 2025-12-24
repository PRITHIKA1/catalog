package io.nexure.discount.exceptions

open class DiscountException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
