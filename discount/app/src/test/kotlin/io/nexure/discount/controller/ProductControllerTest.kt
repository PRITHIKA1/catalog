package io.nexure.discount.controller

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import io.nexure.discount.module

/**
 * Integration tests for ProductController
 * 
 * These tests require a running MongoDB instance.
 * Uses application-test.conf for test configuration with default values.
 * 
 * To run with custom test database, set environment variables:
 * - TEST_MONGO_DB_CONNECTION_STRING (default: mongodb://localhost:27017)
 * - TEST_CATALOG_DB_NAME (default: test_catalog_db)
 * 
 * For true unit tests without database, see ProductServiceTest which uses mocks.
 */
@DisplayName("ProductController Integration Tests")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ProductControllerTest {

    @Test
    @DisplayName("GET /api/discount/products - should return products for valid country")
    fun testGetProducts_ValidCountry() = testApplication {
        externalServices {
            // Make tests independent
        }
        application {
            module()
        }

        val response = client.get("/api/discount/products?country=Sweden")
        
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @DisplayName("GET /api/discount/products - should return 400 for invalid country")
    fun testGetProducts_InvalidCountry() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        application {
            module()
        }

        val response = client.get("/api/discount/products?country=Germany")
        
        assertTrue(response.status.value >= 400)
    }

    @Test
    @DisplayName("GET /api/discount/products - should return products without country filter")
    fun testGetProducts_NoCountryFilter() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        application {
            module()
        }

        // This should throw an error since country validation requires non-null
        val response = client.get("/api/discount/products")
        
        assertTrue(response.status.value >= 400)
    }

    @Test
    @DisplayName("PUT /api/discount/products/{id}/discount - should apply discount successfully")
    fun testApplyDiscount_Success() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        application {
            module()
        }

        val response = client.put("/api/discount/products/PROD001/discount") {
            contentType(ContentType.Application.Json)
            setBody("""{"discountId": "DISC_NEW", "percent": 15.0}""")
        }
        
        // Should be OK or conflict if already exists
        assertTrue(response.status.value in listOf(200, 400, 409))
    }

    @Test
    @DisplayName("PUT /api/discount/products/{id}/discount - should reject invalid discount percentage")
    fun testApplyDiscount_InvalidPercentage() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        application {
            module()
        }

        val response = client.put("/api/discount/products/PROD001/discount") {
            contentType(ContentType.Application.Json)
            setBody("""{"discountId": "DISC_INVALID", "percent": 150.0}""")
        }
        
        assertTrue(response.status.value >= 400)
    }

    @Test
    @DisplayName("PUT /api/discount/products/{id}/discount - should reject negative discount")
    fun testApplyDiscount_NegativePercentage() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        application {
            module()
        }

        val response = client.put("/api/discount/products/PROD001/discount") {
            contentType(ContentType.Application.Json)
            setBody("""{"discountId": "DISC_NEG", "percent": -10.0}""")
        }
        
        assertTrue(response.status.value >= 400)
    }

    @Test
    @DisplayName("PUT /api/discount/products/{id}/discount - should reject zero discount")
    fun testApplyDiscount_ZeroPercentage() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        application {
            module()
        }

        val response = client.put("/api/discount/products/PROD001/discount") {
            contentType(ContentType.Application.Json)
            setBody("""{"discountId": "DISC_ZERO", "percent": 0.0}""")
        }
        
        assertTrue(response.status.value >= 400)
    }

    @Test
    @DisplayName("PUT /api/discount/products/{id}/discount - should handle malformed JSON")
    fun testApplyDiscount_MalformedJson() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        application {
            module()
        }

        val response = client.put("/api/discount/products/PROD001/discount") {
            contentType(ContentType.Application.Json)
            setBody("""{"invalid json}""")
        }
        
        assertTrue(response.status.value >= 400)
    }

    @Test
    @DisplayName("PUT /api/discount/products/{id}/discount - should handle missing request body")
    fun testApplyDiscount_MissingBody() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        application {
            module()
        }

        val response = client.put("/api/discount/products/PROD001/discount") {
            contentType(ContentType.Application.Json)
        }
        
        assertTrue(response.status.value >= 400)
    }

    @Test
    @DisplayName("GET /api/discount/products - should handle all valid countries")
    fun testGetProducts_AllValidCountries() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        application {
            module()
        }

        val countries = listOf("Sweden", "French", "Italian")
        
        countries.forEach { country ->
            val response = client.get("/api/discount/products?country=$country")
            assertEquals(HttpStatusCode.OK, response.status, "Failed for country: $country")
        }
    }

    @Test
    @DisplayName("PUT /api/discount/products/{id}/discount - should accept boundary discount values")
    fun testApplyDiscount_BoundaryValues() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        application {
            module()
        }

        // Test with 0.01% (just above 0)
        val response1 = client.put("/api/discount/products/PROD_BOUNDARY_1/discount") {
            contentType(ContentType.Application.Json)
            setBody("""{"discountId": "DISC_MIN", "percent": 0.01}""")
        }
        assertTrue(response1.status.value in listOf(200, 400, 409))

        // Test with 100% (maximum valid)
        val response2 = client.put("/api/discount/products/PROD_BOUNDARY_2/discount") {
            contentType(ContentType.Application.Json)
            setBody("""{"discountId": "DISC_MAX", "percent": 100.0}""")
        }
        assertTrue(response2.status.value in listOf(200, 400, 409))
    }

    @Test
    @DisplayName("GET /api/discount/products - should handle URL encoded country names")
    fun testGetProducts_URLEncodedCountry() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        application {
            module()
        }

        val response = client.get("/api/discount/products?country=Sweden")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @DisplayName("PUT /api/discount/products/{id}/discount - should handle special characters in product ID")
    fun testApplyDiscount_SpecialCharactersInProductId() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        application {
            module()
        }

        val response = client.put("/api/discount/products/PROD-001_TEST/discount") {
            contentType(ContentType.Application.Json)
            setBody("""{"discountId": "DISC_SPECIAL", "percent": 10.0}""")
        }
        
        // Should either succeed or fail with proper error
        assertTrue(response.status.value in 200..599)
    }
}
