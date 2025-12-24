package io.nexure.discount.model.dto

data class ConfigurationDTO(
    val catalogDBName: String,
    val mongoDBConnectionString: String,
    val maxDBConnectionIdleTime: Long,
    val maxDBConnections: Int,
    val minDBConnections: Int,
)
