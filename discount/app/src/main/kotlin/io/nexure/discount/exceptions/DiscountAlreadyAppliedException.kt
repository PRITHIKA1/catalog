package io.nexure.discount.exceptions

data class DiscountAlreadyAppliedException(val errorMessage: String) :
    DiscountException(errorMessage)
