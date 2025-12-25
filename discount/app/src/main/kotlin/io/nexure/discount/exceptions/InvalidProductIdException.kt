package io.nexure.discount.exceptions

data class InvalidProductIdException(val errorMessage: String)
    : DiscountException(errorMessage)
