package io.nexure.discount.config

import io.ktor.server.application.ApplicationEnvironment
import io.nexure.discount.model.dto.ConfigurationDTO

object Configuration {
    lateinit var env: ConfigurationDTO

    fun initConfig(environment: ApplicationEnvironment) {
        env = ConfigurationDTO(
            catalogDBName = environment.config.property("ktor.db.catalog_db_name").getString(),
            mongoDBConnectionString = environment.config.property("ktor.db.mongo_db_connection_string").getString(),
            maxDBConnectionIdleTime = environment.config.property("ktor.db.max_db_connection_idle_time").getString().toLong(),
            minDBConnections = environment.config.property("ktor.db.min_db_connections").getString().toInt(),
            maxDBConnections = environment.config.property("ktor.db.max_db_connections").getString().toInt()
        )
    }
}
