package io.nexure.discount.repository

import com.mongodb.MongoWriteException
import io.nexure.discount.config.CatalogDBConfiguration
import io.nexure.discount.exceptions.DatabaseOperationException
import io.nexure.discount.exceptions.DiscountAlreadyAppliedException
import io.nexure.discount.model.entity.Discount
import io.nexure.discount.model.entity.Product
import io.nexure.discount.model.enum.Country
import io.nexure.discount.util.ApplicationConstants.PRODUCTS_COLLECTION
import org.litote.kmongo.setOnInsert
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.push
import org.litote.kmongo.combine


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

    override suspend fun applyDiscount(productId: String, discountId: String, percent: Double): Boolean {
        return try {
            val update = combine(
                setOnInsert(Product::discounts, emptyList()),
                push(Product::discounts, Discount(discountId, percent))
            )

            val result = productsCollection.updateOne(
                Product::id eq productId,
                update
            )
            result.modifiedCount > 0
        } catch (e: MongoWriteException) {
            if (e.error.code == 11000) {
                throw DiscountAlreadyAppliedException("productId: $productId")
            } else {
                throw DatabaseOperationException("productId: $productId Error: ${e.message}")
            }
        } catch (e: Exception) {
            throw DatabaseOperationException("productId: $productId Error: ${e.message}")
        }
    }
}
