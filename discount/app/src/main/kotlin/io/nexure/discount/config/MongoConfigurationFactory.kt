package io.nexure.discount.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.connection.ConnectionPoolSettings
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.ConcurrentHashMap

object MongoConfigurationFactory {
    private val logger: Logger = LoggerFactory.getLogger(MongoConfigurationFactory::class.java)
    private val client: CoroutineClient = KMongo.createClient().coroutine
    private val databases = ConcurrentHashMap<String, CoroutineDatabase>()

    /**
     * Get a CoroutineDatabase instance for a given database name and URL.
     * Thread-safe, uses ConcurrentHashMap to cache instances.
     * Used Double Check Lock to ensure only one instance created when concurrent requests comes in
     */
    fun getDatabase(databaseName: String, databaseUrl: String? = null): CoroutineDatabase {
        databases[databaseName]?.let { return it }

        synchronized(databases) {
            databases[databaseName]?.let { return it }

            logger.info("Creating MongoDatabase instance for $databaseName...")

            val dbClient: CoroutineClient = if (databaseUrl != null) {
                val settings = MongoClientSettings.builder()
                    .applyConnectionString(ConnectionString(databaseUrl))
                    .applyToConnectionPoolSettings {
                        it.applySettings(
                            ConnectionPoolSettings.builder()
                                .maxConnectionIdleTime(
                                    Configuration.env.maxDBConnectionIdleTime,
                                    TimeUnit.MILLISECONDS
                                )
                                .minSize(Configuration.env.minDBConnections)
                                .maxSize(Configuration.env.maxDBConnections)
                                .build()
                        )
                    }
                    .applicationName("Discount")
                    .build()
                KMongo.createClient(settings).coroutine
            } else {
                client
            }

            val database = dbClient.getDatabase(databaseName)
            databases[databaseName] = database
            return database
        }
    }
}
