package io.nexure.discount.model.entity

import io.nexure.discount.model.enum.Country

data class Product(
    val id: String,
    val name: String,
    val basePrice: Double,
    val country: Country,
    val discounts: List<Discount> = emptyList()
)
