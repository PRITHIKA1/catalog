package io.nexure.discount.repository

import io.nexure.discount.config.CatalogDBConfiguration
import io.nexure.discount.exceptions.DatabaseOperationException
import io.nexure.discount.exceptions.DiscountAlreadyAppliedException
import io.nexure.discount.model.entity.Discount
import io.nexure.discount.model.entity.Product
import io.nexure.discount.model.enum.Country
import io.nexure.discount.util.ApplicationConstants.PRODUCTS_COLLECTION
import org.litote.kmongo.and
import com.mongodb.client.model.Filters.not
import org.litote.kmongo.elemMatch
import org.litote.kmongo.eq
import org.litote.kmongo.push
import org.litote.kmongo.coroutine.CoroutineCollection


class ProductRepository(
        private val productsCollection: CoroutineCollection<Product> = CatalogDBConfiguration
            .getDatabase().getCollection(PRODUCTS_COLLECTION),
    ): IProductRepository {

    override suspend fun getProductsByCountry(country: Country): List<Product> {
        return try {
            productsCollection
                .find(Product::country eq country.countryName)
                .toList()
        } catch (e: Exception) {
            throw DatabaseOperationException(
                "Failed to fetch products for country: $country  Error: ${e.message}"
            )
        }
    }

    override suspend fun applyDiscount(
        productId: String,
        discountId: String,
        percent: Double
    ): Boolean {
        try {
            val filter = and(
                Product::id eq productId,
                not(Product::discounts.elemMatch(Discount::discountId eq discountId))
            )
            val update = push(Product::discounts, Discount(discountId, percent))
            val result = productsCollection.updateOne(filter, update)

            if (result.matchedCount == 0L) {
                throw DiscountAlreadyAppliedException("Discount already applied or product not found")
            }
            return result.modifiedCount > 0

        } catch (e: DiscountAlreadyAppliedException) {
            throw e
        } catch (e: Exception) {
            throw DatabaseOperationException("Database operation failed for productId=$productId. Error: ${e.message}")
        }
    }
}
