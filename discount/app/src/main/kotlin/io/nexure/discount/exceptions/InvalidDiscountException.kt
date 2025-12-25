package io.nexure.discount.exceptions

data class InvalidDiscountException(val errorMessage: String):
        DiscountException(errorMessage)
