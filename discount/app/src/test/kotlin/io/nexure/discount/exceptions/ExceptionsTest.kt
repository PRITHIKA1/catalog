package io.nexure.discount.exceptions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName

@DisplayName("Exception Tests")
class ExceptionsTest {

    @Test
    @DisplayName("DiscountException should be created with message")
    fun testDiscountException_WithMessage() {
        val exception = DiscountException("Test error message")
        
        assertEquals("Test error message", exception.message)
        assertNull(exception.cause)
    }

    @Test
    @DisplayName("DiscountException should be created with message and cause")
    fun testDiscountException_WithMessageAndCause() {
        val cause = RuntimeException("Root cause")
        val exception = DiscountException("Test error", cause)
        
        assertEquals("Test error", exception.message)
        assertEquals(cause, exception.cause)
    }

    @Test
    @DisplayName("InvalidCountryException should extend DiscountException")
    fun testInvalidCountryException_ExtendsDiscountException() {
        val exception = InvalidCountryException("Invalid country")
        
        assertTrue(exception is DiscountException)
        assertEquals("Invalid country", exception.message)
    }

    @Test
    @DisplayName("InvalidProductIdException should extend DiscountException")
    fun testInvalidProductIdException_ExtendsDiscountException() {
        val exception = InvalidProductIdException("Invalid product ID")
        
        assertTrue(exception is DiscountException)
        assertEquals("Invalid product ID", exception.message)
    }

    @Test
    @DisplayName("InvalidDiscountException should extend DiscountException")
    fun testInvalidDiscountException_ExtendsDiscountException() {
        val exception = InvalidDiscountException("Invalid discount")
        
        assertTrue(exception is DiscountException)
        assertEquals("Invalid discount", exception.message)
    }

    @Test
    @DisplayName("DiscountAlreadyAppliedException should extend DiscountException")
    fun testDiscountAlreadyAppliedException_ExtendsDiscountException() {
        val exception = DiscountAlreadyAppliedException("Discount already applied")
        
        assertTrue(exception is DiscountException)
        assertEquals("Discount already applied", exception.message)
    }

    @Test
    @DisplayName("DatabaseOperationException should extend DiscountException")
    fun testDatabaseOperationException_ExtendsDiscountException() {
        val exception = DatabaseOperationException("Database operation failed")
        
        assertTrue(exception is DiscountException)
        assertEquals("Database operation failed", exception.message)
    }

    @Test
    @DisplayName("ProductNotFoundException should extend DiscountException")
    fun testProductNotFoundException_ExtendsDiscountException() {
        val exception = ProductNotFoundException("Product not found")
        
        assertTrue(exception is DiscountException)
        assertEquals("Product not found", exception.message)
    }

    @Test
    @DisplayName("All exceptions should be throwable")
    fun testAllExceptionsAreThrowable() {
        // Verify exceptions can be thrown and caught properly
        try {
            throw DiscountException("Test")
        } catch (e: DiscountException) {
            assertEquals("Test", e.message)
        }
        
        try {
            throw InvalidCountryException("Test")
        } catch (e: InvalidCountryException) {
            assertEquals("Test", e.message)
        }
        
        try {
            throw InvalidProductIdException("Test")
        } catch (e: InvalidProductIdException) {
            assertEquals("Test", e.message)
        }
        
        try {
            throw InvalidDiscountException("Test")
        } catch (e: InvalidDiscountException) {
            assertEquals("Test", e.message)
        }
        
        try {
            throw DiscountAlreadyAppliedException("Test")
        } catch (e: DiscountAlreadyAppliedException) {
            assertEquals("Test", e.message)
        }
        
        try {
            throw DatabaseOperationException("Test")
        } catch (e: DatabaseOperationException) {
            assertEquals("Test", e.errorMessage)
        }
        
        try {
            throw ProductNotFoundException("Test")
        } catch (e: ProductNotFoundException) {
            assertEquals("Test", e.message)
        }
    }

    @Test
    @DisplayName("Exceptions should support stack trace")
    fun testExceptionsHaveStackTrace() {
        val exception = DiscountException("Test")
        
        assertNotNull(exception.stackTrace)
        assertTrue(exception.stackTrace.isNotEmpty())
    }

    @Test
    @DisplayName("Exception cause chain should work")
    fun testExceptionCauseChain() {
        val rootCause = IllegalArgumentException("Root cause")
        val topException = DiscountException("Top level", rootCause)
        
        assertEquals(rootCause, topException.cause)
        assertEquals("Root cause", topException.cause?.message)
    }
}
