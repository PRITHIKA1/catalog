package io.nexure.discount.exceptions

data class ProductNotFoundException(val errorMessage: String):
    DiscountException(errorMessage)
