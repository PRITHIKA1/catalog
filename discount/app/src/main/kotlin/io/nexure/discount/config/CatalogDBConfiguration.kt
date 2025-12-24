package io.nexure.discount.config

import org.litote.kmongo.coroutine.CoroutineDatabase

object CatalogDBConfiguration {
    fun getDatabase(): CoroutineDatabase {
        return MongoConfigurationFactory.getDatabase(
            Configuration.env.catalogDBName,
            Configuration.env.mongoDBConnectionString
        )
    }
}