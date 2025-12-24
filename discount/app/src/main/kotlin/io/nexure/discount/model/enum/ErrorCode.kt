package io.nexure.discount.model.enum

enum class ErrorCode(val description: String) {
    ERR_101("Product Not Found"),
    ERR_102("Discount already exists"),
    ERR_103("Database Operation Not Found"),
    ERR_104("Failed to complete the operation"),
    ERR_105("Invalid Discount Value"),
    ERR_106("Invalid ProductId Value"),
    ERR_107("Invalid Country Code"),
}