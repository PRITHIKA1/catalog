package io.nexure.discount.model.dto

data class ApplyDiscountRequest(
    val discountId: String,
    val percent: Double
)
