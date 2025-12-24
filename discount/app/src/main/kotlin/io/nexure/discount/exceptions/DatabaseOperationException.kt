package io.nexure.discount.exceptions

data class DatabaseOperationException(val errorMessage: String) :
        DiscountException("Database operation failed: $errorMessage")
