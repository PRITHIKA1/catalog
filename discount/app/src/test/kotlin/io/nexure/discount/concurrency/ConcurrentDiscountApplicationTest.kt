package io.nexure.discount.concurrency

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking {

    val client = HttpClient(CIO)

    val url = "http://localhost:8080/api/discount/products/PROD002/discount"

    val requestBody = """
        {
          "discountId": "DISC0056",
          "percent": 15.08
        }
    """.trimIndent()

    val concurrentRequests = 20

    println("🚀 Sending $concurrentRequests concurrent requests")

    val time = measureTimeMillis {

        coroutineScope {
            repeat(concurrentRequests) { index ->
                launch(Dispatchers.IO) {
                    try {
                        val response: HttpResponse = client.put(url) {
                            contentType(ContentType.Application.Json)
                            setBody(requestBody)
                        }

                        println("✅ [$index] Status: ${response.status.value}")
                    } catch (e: Exception) {
                        println("❌ [$index] Error: ${e.message}")
                    }
                }
            }
        }
    }

    println("⏱ Completed in ${time}ms")
    client.close()
}
