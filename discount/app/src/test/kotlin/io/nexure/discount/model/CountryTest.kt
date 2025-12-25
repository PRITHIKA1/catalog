package io.nexure.discount.model

import io.nexure.discount.exceptions.InvalidCountryException
import io.nexure.discount.model.enum.Country
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows

@DisplayName("Country Enum Tests")
class CountryTest {

    @Test
    @DisplayName("Should have correct VAT values for all countries")
    fun testCountryVATValues() {
        assertEquals(70.0, Country.SWEDEN.vat)
        assertEquals(65.0, Country.FRENCH.vat)
        assertEquals(40.0, Country.ITALIAN.vat)
    }

    @Test
    @DisplayName("Should have correct country names")
    fun testCountryNames() {
        assertEquals("Sweden", Country.SWEDEN.countryName)
        assertEquals("French", Country.FRENCH.countryName)
        assertEquals("Italian", Country.ITALIAN.countryName)
    }

    @Test
    @DisplayName("Should convert country to JSON correctly")
    fun testToJson() {
        assertEquals("Sweden", Country.SWEDEN.toJson())
        assertEquals("French", Country.FRENCH.toJson())
        assertEquals("Italian", Country.ITALIAN.toJson())
    }

    @Test
    @DisplayName("Should parse valid country from JSON")
    fun testFromJson_ValidCountries() {
        assertEquals(Country.SWEDEN, Country.fromJson("Sweden"))
        assertEquals(Country.FRENCH, Country.fromJson("French"))
        assertEquals(Country.ITALIAN, Country.fromJson("Italian"))
    }

    @Test
    @DisplayName("Should throw InvalidCountryException for unknown country")
    fun testFromJson_InvalidCountry() {
        val exception = assertThrows<InvalidCountryException> {
            Country.fromJson("Germany")
        }
        assertTrue(exception.message!!.contains("Unknown country: Germany"))
    }

    @Test
    @DisplayName("Should throw InvalidCountryException for empty string")
    fun testIsValidCountry_EmptyString() {
        assertThrows<InvalidCountryException> {
            Country.isValidCountry("")
        }
    }

    @Test
    @DisplayName("Should throw InvalidCountryException for null")
    fun testIsValidCountry_Null() {
        assertThrows<InvalidCountryException> {
            Country.isValidCountry(null)
        }
    }

    @Test
    @DisplayName("Should throw InvalidCountryException for blank string")
    fun testIsValidCountry_BlankString() {
        assertThrows<InvalidCountryException> {
            Country.isValidCountry("   ")
        }
    }

    @Test
    @DisplayName("Should validate all valid countries")
    fun testIsValidCountry_ValidCountries() {
        assertDoesNotThrow {
            Country.isValidCountry("Sweden")
        }
        assertDoesNotThrow {
            Country.isValidCountry("French")
        }
        assertDoesNotThrow {
            Country.isValidCountry("Italian")
        }
    }

    @Test
    @DisplayName("Should throw InvalidCountryException for invalid country in validation")
    fun testIsValidCountry_InvalidCountry() {
        assertThrows<InvalidCountryException> {
            Country.isValidCountry("Spain")
        }
    }

    @Test
    @DisplayName("Should have all countries in countries map")
    fun testCountriesMap() {
        assertEquals(3, Country.countries.size)
        assertTrue(Country.countries.containsKey("Sweden"))
        assertTrue(Country.countries.containsKey("French"))
        assertTrue(Country.countries.containsKey("Italian"))
    }

    @Test
    @DisplayName("Should retrieve correct country from map")
    fun testCountriesMapValues() {
        assertEquals(Country.SWEDEN, Country.countries["Sweden"])
        assertEquals(Country.FRENCH, Country.countries["French"])
        assertEquals(Country.ITALIAN, Country.countries["Italian"])
    }

    @Test
    @DisplayName("Should handle case sensitivity")
    fun testFromJson_CaseSensitive() {
        // Country names are case-sensitive
        assertThrows<InvalidCountryException> {
            Country.fromJson("sweden") // lowercase
        }
        
        assertThrows<InvalidCountryException> {
            Country.fromJson("SWEDEN") // uppercase
        }
    }
}
