package io.nexure.discount.exceptions

data class InvalidCountryException(val errorMessage: String)
    : DiscountException(errorMessage)
