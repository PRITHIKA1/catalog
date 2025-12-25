package io.nexure.discount.service

import io.nexure.discount.exceptions.DiscountException
import io.nexure.discount.exceptions.InvalidDiscountException
import io.nexure.discount.exceptions.InvalidProductIdException
import io.nexure.discount.model.dto.ApplyDiscountRequest
import io.nexure.discount.model.dto.ProductResponse
import io.nexure.discount.model.entity.Product
import io.nexure.discount.model.enum.Country
import io.nexure.discount.repository.ProductRepository
import org.slf4j.LoggerFactory

class ProductService(
    private val productRepository: ProductRepository
) : IProductService{

    private val logger = LoggerFactory.getLogger(ProductService::class.java)

    /**
     * Fetch all products and calculate final prices
     */
    override suspend fun fetchAllProductsByCountry(country: String?): List<ProductResponse> {
        try {
            Country.isValidCountry(country)
            val products: List<Product> = productRepository.getProductsByCountry(Country.fromJson(country!!))

            return products.map { product ->
                ProductResponse.fromProduct(product)
            }
        } catch (ex: DiscountException) {
            logger.error(ex.message, ex)
            throw ex
        } catch (ex: Exception) {
            logger.error(ex.message, ex)
            throw DiscountException("Failed to fetch products. Error: ${ex.message}")
        }
    }

    /**
     * Update Discount for product
     */
    override suspend fun updateDiscount(productId: String?, request: ApplyDiscountRequest): Boolean {
        try {
            validateDiscountRequest(productId, request.percent)

             productRepository.applyDiscount(
                productId = productId!!,
                discountId = request.discountId,
                percent = request.percent
            )

            logger.info("Product with id $productId updated successfully")
            return true
        } catch (ex: DiscountException) {
            logger.error(ex.message, ex)
            throw ex
        } catch (ex: Exception) {
            logger.error(ex.message, ex)
            throw DiscountException("Failed to update products. Error: ${ex.message}")
        }
    }

    fun validateDiscountRequest(productId: String?, discountPercent: Double) {
        if(discountPercent <= 0 || discountPercent > 100) {
            throw InvalidDiscountException("Discount percentage must be between 0 and 100")
        }
        if(productId.isNullOrBlank()) {
            throw InvalidProductIdException("Product ID is blank")
        }
    }
}
