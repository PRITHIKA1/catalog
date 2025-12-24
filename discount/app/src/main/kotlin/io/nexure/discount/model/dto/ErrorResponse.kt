package io.nexure.discount.model.dto

import io.nexure.discount.model.enum.ErrorCode

data class ErrorResponse(
    val errorCode: ErrorCode,
    val message: String
)
