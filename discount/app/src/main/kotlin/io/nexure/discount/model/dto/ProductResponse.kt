package io.nexure.discount.model.dto

import io.nexure.discount.model.entity.Discount
import io.nexure.discount.model.entity.Product
import io.nexure.discount.model.enum.Country

data class ProductResponse(
    val id: String,
    val name: String,
    val basePrice: Double,
    val country: Country,
    val discounts: List<Discount>,
    var finalPrice: Double
) {
    companion object {
        fun fromProduct(product: Product): ProductResponse {
            return ProductResponse(
                id = product.id,
                name = product.name,
                country = Country.fromJson(product.country),
                discounts = product.discounts,
                basePrice = product.basePrice,
                finalPrice = finalPrice(product.basePrice, Country.fromJson(product.country).vat, product.discounts)
            )
        }

        fun finalPrice(basePrice: Double, vat: Double, discounts: List<Discount>): Double {
            val totalDiscount = discounts.sumOf { it.percent } / 100.0
            return basePrice * (1 - totalDiscount) * (1 + vat)
        }
    }
}
