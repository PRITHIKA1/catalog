package io.nexure.discount.service

import io.mockk.*
import io.nexure.discount.exceptions.*
import io.nexure.discount.model.dto.ApplyDiscountRequest
import io.nexure.discount.model.entity.Discount
import io.nexure.discount.model.entity.Product
import io.nexure.discount.model.enum.Country
import io.nexure.discount.repository.ProductRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows

@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var productService: ProductService

    @BeforeEach
    fun setup() {
        productRepository = mockk()
        productService = ProductService(productRepository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    @DisplayName("Should fetch all products by country successfully")
    fun testFetchAllProductsByCountry_Success() = runTest {
        // Given
        val country = "Sweden"
        val products = listOf(
            Product(
                id = "PROD001",
                name = "Product 1",
                basePrice = 100.0,
                country = "Sweden",
                discounts = listOf(Discount("DISC001", 10.0))
            ),
            Product(
                id = "PROD002",
                name = "Product 2",
                basePrice = 200.0,
                country = "Sweden",
                discounts = emptyList()
            )
        )

        coEvery { productRepository.getProductsByCountry(Country.SWEDEN) } returns products

        // When
        val result = productService.fetchAllProductsByCountry(country)

        // Then
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("PROD001", result[0].id)
        assertEquals("Product 1", result[0].name)
        assertEquals(Country.SWEDEN, result[0].country)
        coVerify(exactly = 1) { productRepository.getProductsByCountry(Country.SWEDEN) }
    }

    @Test
    @DisplayName("Should throw InvalidCountryException for invalid country")
    fun testFetchAllProductsByCountry_InvalidCountry() = runTest {
        // Given
        val invalidCountry = "Germany"

        // When & Then
        assertThrows<InvalidCountryException> {
            productService.fetchAllProductsByCountry(invalidCountry)
        }

        coVerify(exactly = 0) { productRepository.getProductsByCountry(any()) }
    }

    @Test
    @DisplayName("Should throw InvalidCountryException for null country")
    fun testFetchAllProductsByCountry_NullCountry() = runTest {
        // When & Then
        assertThrows<InvalidCountryException> {
            productService.fetchAllProductsByCountry(null)
        }

        coVerify(exactly = 0) { productRepository.getProductsByCountry(any()) }
    }

    @Test
    @DisplayName("Should throw InvalidCountryException for empty country")
    fun testFetchAllProductsByCountry_EmptyCountry() = runTest {
        // When & Then
        assertThrows<InvalidCountryException> {
            productService.fetchAllProductsByCountry("")
        }

        coVerify(exactly = 0) { productRepository.getProductsByCountry(any()) }
    }

    @Test
    @DisplayName("Should throw DiscountException when repository fails")
    fun testFetchAllProductsByCountry_RepositoryFailure() = runTest {
        // Given
        val country = "Sweden"
        coEvery { productRepository.getProductsByCountry(Country.SWEDEN) } throws 
            RuntimeException("Database error")

        // When & Then
        val exception = assertThrows<DiscountException> {
            productService.fetchAllProductsByCountry(country)
        }

        assertTrue(exception.message!!.contains("Failed to fetch products"))
        coVerify(exactly = 1) { productRepository.getProductsByCountry(Country.SWEDEN) }
    }

    @Test
    @DisplayName("Should update discount successfully")
    fun testUpdateDiscount_Success() = runTest {
        // Given
        val productId = "PROD001"
        val request = ApplyDiscountRequest("DISC001", 15.0)

        coEvery { 
            productRepository.applyDiscount(productId, request.discountId, request.percent) 
        } returns true

        // When
        val result = productService.updateDiscount(productId, request)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { 
            productRepository.applyDiscount(productId, request.discountId, request.percent) 
        }
    }

    @Test
    @DisplayName("Should throw InvalidProductIdException for null product ID")
    fun testUpdateDiscount_NullProductId() = runTest {
        // Given
        val request = ApplyDiscountRequest("DISC001", 15.0)

        // When & Then
        assertThrows<InvalidProductIdException> {
            productService.updateDiscount(null, request)
        }

        coVerify(exactly = 0) { productRepository.applyDiscount(any(), any(), any()) }
    }

    @Test
    @DisplayName("Should throw InvalidProductIdException for empty product ID")
    fun testUpdateDiscount_EmptyProductId() = runTest {
        // Given
        val request = ApplyDiscountRequest("DISC001", 15.0)

        // When & Then
        assertThrows<InvalidProductIdException> {
            productService.updateDiscount("", request)
        }

        coVerify(exactly = 0) { productRepository.applyDiscount(any(), any(), any()) }
    }

    @Test
    @DisplayName("Should throw InvalidDiscountException for zero discount")
    fun testUpdateDiscount_ZeroDiscount() = runTest {
        // Given
        val productId = "PROD001"
        val request = ApplyDiscountRequest("DISC001", 0.0)

        // When & Then
        assertThrows<InvalidDiscountException> {
            productService.updateDiscount(productId, request)
        }

        coVerify(exactly = 0) { productRepository.applyDiscount(any(), any(), any()) }
    }

    @Test
    @DisplayName("Should throw InvalidDiscountException for negative discount")
    fun testUpdateDiscount_NegativeDiscount() = runTest {
        // Given
        val productId = "PROD001"
        val request = ApplyDiscountRequest("DISC001", -10.0)

        // When & Then
        assertThrows<InvalidDiscountException> {
            productService.updateDiscount(productId, request)
        }

        coVerify(exactly = 0) { productRepository.applyDiscount(any(), any(), any()) }
    }

    @Test
    @DisplayName("Should throw InvalidDiscountException for discount over 100")
    fun testUpdateDiscount_DiscountOver100() = runTest {
        // Given
        val productId = "PROD001"
        val request = ApplyDiscountRequest("DISC001", 101.0)

        // When & Then
        assertThrows<InvalidDiscountException> {
            productService.updateDiscount(productId, request)
        }

        coVerify(exactly = 0) { productRepository.applyDiscount(any(), any(), any()) }
    }

    @Test
    @DisplayName("Should propagate DiscountAlreadyAppliedException from repository")
    fun testUpdateDiscount_DuplicateDiscount() = runTest {
        // Given
        val productId = "PROD001"
        val request = ApplyDiscountRequest("DISC001", 15.0)

        coEvery { 
            productRepository.applyDiscount(productId, request.discountId, request.percent) 
        } throws DiscountAlreadyAppliedException("Discount already applied")

        // When & Then
        assertThrows<DiscountAlreadyAppliedException> {
            productService.updateDiscount(productId, request)
        }

        coVerify(exactly = 1) { 
            productRepository.applyDiscount(productId, request.discountId, request.percent) 
        }
    }

    @Test
    @DisplayName("Should handle repository exception and wrap in DiscountException")
    fun testUpdateDiscount_RepositoryException() = runTest {
        // Given
        val productId = "PROD001"
        val request = ApplyDiscountRequest("DISC001", 15.0)

        coEvery { 
            productRepository.applyDiscount(productId, request.discountId, request.percent) 
        } throws RuntimeException("Database connection failed")

        // When & Then
        val exception = assertThrows<DiscountException> {
            productService.updateDiscount(productId, request)
        }

        assertTrue(exception.message!!.contains("Failed to update products"))
        coVerify(exactly = 1) { 
            productRepository.applyDiscount(productId, request.discountId, request.percent) 
        }
    }

    @Test
    @DisplayName("Should validate discount request with valid inputs")
    fun testValidateDiscountRequest_ValidInputs() {
        // When & Then - should not throw
        assertDoesNotThrow {
            productService.validateDiscountRequest("PROD001", 50.0)
        }
    }

    @Test
    @DisplayName("Should validate discount request at boundaries")
    fun testValidateDiscountRequest_BoundaryValues() {
        // Valid boundary values
        assertDoesNotThrow {
            productService.validateDiscountRequest("PROD001", 0.01) // Just above 0
        }
        
        assertDoesNotThrow {
            productService.validateDiscountRequest("PROD001", 100.0) // At maximum
        }
    }

    @Test
    @DisplayName("Should fetch products for all valid countries")
    fun testFetchAllProductsByCountry_AllCountries() = runTest {
        // Test Sweden
        val swedenProducts = listOf(
            Product("P1", "Product 1", 100.0, "Sweden", emptyList())
        )
        coEvery { productRepository.getProductsByCountry(Country.SWEDEN) } returns swedenProducts
        val swedenResult = productService.fetchAllProductsByCountry("Sweden")
        assertEquals(1, swedenResult.size)

        // Test French
        val frenchProducts = listOf(
            Product("P2", "Product 2", 200.0, "French", emptyList())
        )
        coEvery { productRepository.getProductsByCountry(Country.FRENCH) } returns frenchProducts
        val frenchResult = productService.fetchAllProductsByCountry("French")
        assertEquals(1, frenchResult.size)

        // Test Italian
        val italianProducts = listOf(
            Product("P3", "Product 3", 300.0, "Italian", emptyList())
        )
        coEvery { productRepository.getProductsByCountry(Country.ITALIAN) } returns italianProducts
        val italianResult = productService.fetchAllProductsByCountry("Italian")
        assertEquals(1, italianResult.size)
    }

    @Test
    @DisplayName("Should return empty list when no products found")
    fun testFetchAllProductsByCountry_EmptyResult() = runTest {
        // Given
        val country = "Sweden"
        coEvery { productRepository.getProductsByCountry(Country.SWEDEN) } returns emptyList()

        // When
        val result = productService.fetchAllProductsByCountry(country)

        // Then
        assertNotNull(result)
        assertEquals(0, result.size)
        coVerify(exactly = 1) { productRepository.getProductsByCountry(Country.SWEDEN) }
    }
}
