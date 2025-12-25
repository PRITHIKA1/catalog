package io.nexure.discount.model

import io.nexure.discount.model.dto.ProductResponse
import io.nexure.discount.model.entity.Discount
import io.nexure.discount.model.entity.Product
import io.nexure.discount.model.enum.Country
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName

@DisplayName("ProductResponse Unit Tests")
class ProductResponseTest {

    @Test
    @DisplayName("Should create ProductResponse from Product with no discounts")
    fun testFromProduct_NoDiscounts() {
        // Given
        val product = Product(
            id = "PROD001",
            name = "Test Product",
            basePrice = 100.0,
            country = "Sweden",
            discounts = emptyList()
        )

        // When
        val response = ProductResponse.fromProduct(product)

        // Then
        assertEquals("PROD001", response.id)
        assertEquals("Test Product", response.name)
        assertEquals(100.0, response.basePrice)
        assertEquals(Country.SWEDEN, response.country)
        assertTrue(response.discounts.isEmpty())
        // Sweden VAT is 70% (70.0), so 100 * (1 + 70/100) = 100 * 1.7 = 170.0
        assertEquals(170.0, response.finalPrice, 0.01)
    }

    @Test
    @DisplayName("Should create ProductResponse from Product with single discount")
    fun testFromProduct_SingleDiscount() {
        // Given
        val product = Product(
            id = "PROD002",
            name = "Discounted Product",
            basePrice = 100.0,
            country = "Sweden",
            discounts = listOf(Discount("DISC001", 10.0))
        )

        // When
        val response = ProductResponse.fromProduct(product)

        // Then
        assertEquals("PROD002", response.id)
        assertEquals(1, response.discounts.size)
        // 100 * (1 - 0.10) * (1 + 0.70) = 90 * 1.7 = 153.0
        assertEquals(153.0, response.finalPrice, 0.01)
    }

    @Test
    @DisplayName("Should create ProductResponse from Product with multiple discounts")
    fun testFromProduct_MultipleDiscounts() {
        // Given
        val product = Product(
            id = "PROD003",
            name = "Multi-discount Product",
            basePrice = 200.0,
            country = "French",
            discounts = listOf(
                Discount("DISC001", 10.0),
                Discount("DISC002", 15.0)
            )
        )

        // When
        val response = ProductResponse.fromProduct(product)

        // Then
        assertEquals("PROD003", response.id)
        assertEquals(2, response.discounts.size)
        // French VAT is 65% (0.65)
        // 200 * (1 - 0.25) * (1 + 0.65) = 200 * 0.75 * 1.65 = 247.5
        assertEquals(247.5, response.finalPrice, 0.01)
    }

    @Test
    @DisplayName("Should calculate final price correctly with no discount")
    fun testFinalPrice_NoDiscount() {
        // Given
        val basePrice = 100.0
        val vat = 70.0 // Sweden VAT
        val discounts = emptyList<Discount>()

        // When
        val finalPrice = ProductResponse.finalPrice(basePrice, vat, discounts)

        // Then
        // 100 * 1 * 1.70 = 170.0
        assertEquals(170.0, finalPrice, 0.01)
    }

    @Test
    @DisplayName("Should calculate final price correctly with 10% discount")
    fun testFinalPrice_TenPercentDiscount() {
        // Given
        val basePrice = 100.0
        val vat = 70.0 // Sweden VAT
        val discounts = listOf(Discount("DISC001", 10.0))

        // When
        val finalPrice = ProductResponse.finalPrice(basePrice, vat, discounts)

        // Then
        // 100 * (1 - 0.10) * 1.70 = 153.0
        assertEquals(153.0, finalPrice, 0.01)
    }

    @Test
    @DisplayName("Should calculate final price correctly with 100% discount")
    fun testFinalPrice_HundredPercentDiscount() {
        // Given
        val basePrice = 100.0
        val vat = 70.0
        val discounts = listOf(Discount("DISC001", 100.0))

        // When
        val finalPrice = ProductResponse.finalPrice(basePrice, vat, discounts)

        // Then
        // 100 * (1 - 1.0) * 1.70 = 0
        assertEquals(0.0, finalPrice, 0.01)
    }

    @Test
    @DisplayName("Should calculate final price for Italian country")
    fun testFinalPrice_ItalianVAT() {
        // Given
        val basePrice = 100.0
        val vat = 40.0 // Italian VAT
        val discounts = listOf(Discount("DISC001", 20.0))

        // When
        val finalPrice = ProductResponse.finalPrice(basePrice, vat, discounts)

        // Then
        // 100 * (1 - 0.20) * 1.40 = 112.0
        assertEquals(112.0, finalPrice, 0.01)
    }

    @Test
    @DisplayName("Should calculate final price for French country")
    fun testFinalPrice_FrenchVAT() {
        // Given
        val basePrice = 100.0
        val vat = 65.0 // French VAT
        val discounts = emptyList<Discount>()

        // When
        val finalPrice = ProductResponse.finalPrice(basePrice, vat, discounts)

        // Then
        // 100 * 1 * 1.65 = 165.0
        assertEquals(165.0, finalPrice, 0.01)
    }

    @Test
    @DisplayName("Should handle cumulative discounts correctly")
    fun testFinalPrice_CumulativeDiscounts() {
        // Given
        val basePrice = 500.0
        val vat = 70.0
        val discounts = listOf(
            Discount("DISC001", 10.0),
            Discount("DISC002", 20.0),
            Discount("DISC003", 5.0)
        )

        // When
        val finalPrice = ProductResponse.finalPrice(basePrice, vat, discounts)

        // Then
        // Total discount = 35%
        // 500 * (1 - 0.35) * 1.70 = 500 * 0.65 * 1.70 = 552.5
        assertEquals(552.5, finalPrice, 0.01)
    }

    @Test
    @DisplayName("Should create ProductResponse with all fields mapped correctly")
    fun testFromProduct_AllFieldsMapped() {
        // Given
        val product = Product(
            id = "PROD999",
            name = "Complete Product",
            basePrice = 299.99,
            country = "Italian",
            discounts = listOf(
                Discount("DISC001", 15.0),
                Discount("DISC002", 5.0)
            )
        )

        // When
        val response = ProductResponse.fromProduct(product)

        // Then
        assertEquals(product.id, response.id)
        assertEquals(product.name, response.name)
        assertEquals(product.basePrice, response.basePrice)
        assertEquals(Country.ITALIAN, response.country)
        assertEquals(2, response.discounts.size)
        assertEquals("DISC001", response.discounts[0].discountId)
        assertEquals(15.0, response.discounts[0].percent)
        assertEquals("DISC002", response.discounts[1].discountId)
        assertEquals(5.0, response.discounts[1].percent)
        
        // Italian VAT is 40% (0.40)
        // 299.99 * (1 - 0.20) * 1.40 = 299.99 * 0.80 * 1.40 = 335.9888
        assertEquals(335.99, response.finalPrice, 0.01)
    }

    @Test
    @DisplayName("Should handle zero base price")
    fun testFinalPrice_ZeroBasePrice() {
        // Given
        val basePrice = 0.0
        val vat = 70.0
        val discounts = listOf(Discount("DISC001", 10.0))

        // When
        val finalPrice = ProductResponse.finalPrice(basePrice, vat, discounts)

        // Then
        assertEquals(0.0, finalPrice, 0.01)
    }

    @Test
    @DisplayName("Should handle large base price")
    fun testFinalPrice_LargeBasePrice() {
        // Given
        val basePrice = 99999.99
        val vat = 70.0
        val discounts = listOf(Discount("DISC001", 50.0))

        // When
        val finalPrice = ProductResponse.finalPrice(basePrice, vat, discounts)

        // Then
        // 99999.99 * (1 - 0.50) * 1.70 = 84999.99
        assertEquals(84999.99, finalPrice, 0.5)
    }
}
