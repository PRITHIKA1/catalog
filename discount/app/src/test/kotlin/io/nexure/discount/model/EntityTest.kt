package io.nexure.discount.model.entity

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName

@DisplayName("Entity Model Tests")
class EntityTest {

    @Test
    @DisplayName("Product should be created with all required fields")
    fun testProduct_Creation() {
        val discounts = listOf(Discount("DISC001", 10.0))
        val product = Product(
            id = "PROD001",
            name = "Test Product",
            basePrice = 100.0,
            country = "Sweden",
            discounts = discounts
        )

        assertEquals("PROD001", product.id)
        assertEquals("Test Product", product.name)
        assertEquals(100.0, product.basePrice)
        assertEquals("Sweden", product.country)
        assertEquals(1, product.discounts.size)
    }

    @Test
    @DisplayName("Product should have empty discounts by default")
    fun testProduct_DefaultEmptyDiscounts() {
        val product = Product(
            id = "PROD002",
            name = "Product 2",
            basePrice = 200.0,
            country = "French"
        )

        assertTrue(product.discounts.isEmpty())
    }

    @Test
    @DisplayName("Discount should be created with discountId and percent")
    fun testDiscount_Creation() {
        val discount = Discount("DISC001", 15.5)

        assertEquals("DISC001", discount.discountId)
        assertEquals(15.5, discount.percent)
    }

    @Test
    @DisplayName("Product should support data class copy")
    fun testProduct_DataClassCopy() {
        val original = Product(
            id = "PROD001",
            name = "Original",
            basePrice = 100.0,
            country = "Sweden",
            discounts = emptyList()
        )

        val copied = original.copy(name = "Modified")

        assertEquals("PROD001", copied.id)
        assertEquals("Modified", copied.name)
        assertEquals(100.0, copied.basePrice)
        assertEquals("Sweden", copied.country)
    }

    @Test
    @DisplayName("Discount should support data class copy")
    fun testDiscount_DataClassCopy() {
        val original = Discount("DISC001", 10.0)
        val copied = original.copy(percent = 20.0)

        assertEquals("DISC001", copied.discountId)
        assertEquals(20.0, copied.percent)
    }

    @Test
    @DisplayName("Product should support equality comparison")
    fun testProduct_Equality() {
        val product1 = Product("PROD001", "Product", 100.0, "Sweden", emptyList())
        val product2 = Product("PROD001", "Product", 100.0, "Sweden", emptyList())
        val product3 = Product("PROD002", "Product", 100.0, "Sweden", emptyList())

        assertEquals(product1, product2)
        assertNotEquals(product1, product3)
    }

    @Test
    @DisplayName("Discount should support equality comparison")
    fun testDiscount_Equality() {
        val discount1 = Discount("DISC001", 10.0)
        val discount2 = Discount("DISC001", 10.0)
        val discount3 = Discount("DISC002", 10.0)

        assertEquals(discount1, discount2)
        assertNotEquals(discount1, discount3)
    }

    @Test
    @DisplayName("Product should have proper toString")
    fun testProduct_ToString() {
        val product = Product("PROD001", "Product", 100.0, "Sweden", emptyList())
        val toString = product.toString()

        assertTrue(toString.contains("PROD001"))
        assertTrue(toString.contains("Product"))
        assertTrue(toString.contains("100.0"))
        assertTrue(toString.contains("Sweden"))
    }

    @Test
    @DisplayName("Discount should have proper toString")
    fun testDiscount_ToString() {
        val discount = Discount("DISC001", 15.0)
        val toString = discount.toString()

        assertTrue(toString.contains("DISC001"))
        assertTrue(toString.contains("15.0"))
    }

    @Test
    @DisplayName("Product should support multiple discounts")
    fun testProduct_MultipleDiscounts() {
        val discounts = listOf(
            Discount("DISC001", 10.0),
            Discount("DISC002", 15.0),
            Discount("DISC003", 5.0)
        )
        
        val product = Product("PROD001", "Product", 100.0, "Sweden", discounts)

        assertEquals(3, product.discounts.size)
        assertEquals(10.0, product.discounts[0].percent)
        assertEquals(15.0, product.discounts[1].percent)
        assertEquals(5.0, product.discounts[2].percent)
    }

    @Test
    @DisplayName("Product should handle zero base price")
    fun testProduct_ZeroBasePrice() {
        val product = Product("PROD001", "Free Product", 0.0, "Sweden", emptyList())

        assertEquals(0.0, product.basePrice)
    }

    @Test
    @DisplayName("Discount should handle boundary percentage values")
    fun testDiscount_BoundaryValues() {
        val minDiscount = Discount("MIN", 0.01)
        val maxDiscount = Discount("MAX", 100.0)

        assertEquals(0.01, minDiscount.percent)
        assertEquals(100.0, maxDiscount.percent)
    }

    @Test
    @DisplayName("Product hashCode should be consistent")
    fun testProduct_HashCode() {
        val product1 = Product("PROD001", "Product", 100.0, "Sweden", emptyList())
        val product2 = Product("PROD001", "Product", 100.0, "Sweden", emptyList())

        assertEquals(product1.hashCode(), product2.hashCode())
    }

    @Test
    @DisplayName("Discount hashCode should be consistent")
    fun testDiscount_HashCode() {
        val discount1 = Discount("DISC001", 10.0)
        val discount2 = Discount("DISC001", 10.0)

        assertEquals(discount1.hashCode(), discount2.hashCode())
    }
}
