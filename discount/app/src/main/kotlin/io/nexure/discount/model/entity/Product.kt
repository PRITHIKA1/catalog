package io.nexure.discount.model.entity

import io.nexure.discount.model.enum.Country

data class Product(
    val id: String,
    val name: String,
    val basePrice: Double,
    val country: String,
    val discounts: List<Discount> = emptyList()
)
