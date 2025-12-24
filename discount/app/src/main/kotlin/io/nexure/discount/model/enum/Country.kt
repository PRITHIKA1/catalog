package io.nexure.discount.model.enum

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import io.nexure.discount.exceptions.InvalidCountryException

enum class Country(
    val countryName: String,
    val vat: Double
) {
    SWEDEN("Sweden", 70.0),
    FRENCH("French", 65.0),
    ITALIAN("Italian", 40.0);

    @JsonValue
    fun toJson(): String = countryName

    companion object {
        val countries: Map<String, Country> = entries.associateBy { it.countryName }

        @JvmStatic
        @JsonCreator
        fun fromJson(value: String): Country =
            countries[value] ?: throw InvalidCountryException("Unknown country: $value")

        fun isValidCountry(value: String?): Boolean {
            if (value.isNullOrBlank())
                throw InvalidCountryException("Country is Null or Empty: $value")

            Country.fromJson(value)
            return true
        }
    }
}
