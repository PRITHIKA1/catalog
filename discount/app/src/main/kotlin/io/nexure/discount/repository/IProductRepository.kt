package io.nexure.discount.repository

import io.nexure.discount.model.entity.Product
import io.nexure.discount.model.enum.Country

interface IProductRepository {
    suspend fun getProductsByCountry(country: Country): List<Product>
    suspend fun applyDiscount(productId: String, discountId: String, percent: Double): Boolean
}