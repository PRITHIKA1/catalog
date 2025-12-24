package io.nexure.discount.exceptions

data class DiscountAlreadyAppliedException(val errorMessage: String) :
    DiscountException("Discount already applied: $errorMessage")
