package io.nexure.discount.service

import io.nexure.discount.model.dto.ApplyDiscountRequest
import io.nexure.discount.model.dto.ProductResponse

interface IProductService {
    suspend fun fetchAllProductsByCountry(country: String?): List<ProductResponse>
    suspend fun updateDiscount(productId: String?, request: ApplyDiscountRequest): Boolean
}
