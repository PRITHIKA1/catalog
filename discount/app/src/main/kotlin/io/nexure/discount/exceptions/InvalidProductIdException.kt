package io.nexure.discount.exceptions

data class InvalidProductIdException(val errorMessage: String)
    : DiscountException("Invalid product ID: $errorMessage")
