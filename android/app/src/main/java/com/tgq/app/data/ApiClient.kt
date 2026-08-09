package com.tgq.app.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

/**
 * Minimal typed HTTP client for the TGQ API.
 * Reads server base + admin token from [Session].
 */
class ApiClient {

    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
    private val jsonMedia = "application/json; charset=utf-8".toMediaType()

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)
        .writeTimeout(40, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private fun url(path: String): String = Session.get().serverBase + path

    private fun request(path: String, method: String = "GET", body: String? = null): Request {
        val builder = Request.Builder().url(url(path)).method(method, body?.toRequestBody(jsonMedia))
        val token = Session.get().token
        if (token.isNotEmpty()) builder.header("Authorization", "Bearer $token")
        builder.header("Accept", "application/json")
        return builder.build()
    }

    private suspend fun <T> execute(path: String, method: String = "GET", body: String? = null): T {
        return withContext(Dispatchers.IO) {
            val res = client.newCall(request(path, method, body)).execute()
            val text = res.body?.string().orEmpty()
            if (!res.isSuccessful) {
                val detail = runCatching {
                    json.decodeFromString<ApiError>(text).detail
                }.getOrDefault(text.ifEmpty { "HTTP ${res.code}" })
                throw ApiException(res.code, detail)
            }
            json.decodeFromString(serializer<T>(), text)
        }
    }

    suspend fun getHoki(): HokiResponse = execute("/api/hoki")

    suspend fun getMarkets(): MarketsResponse = execute("/api/markets")

    suspend fun getEngines(): EnginesResponse = execute("/api/engines")

    suspend fun predict(engine: String, market: String): PredictResponse =
        execute(
            "/api/predict", "POST",
            json.encodeToString(PredictRequest(engine, market))
        )

    suspend fun login(username: String, password: String): LoginResponse =
        execute(
            "/api/login", "POST",
            json.encodeToString(LoginRequest(username, password))
        )

    suspend fun authStatus(): AuthStatus = execute("/api/auth/status")

    suspend fun postLogout(): AuthStatus = execute("/api/logout", "POST", "{}")

    suspend fun postInput(date: String, items: List<InputItem>): InputResponse =
        execute(
            "/api/input", "POST",
            json.encodeToString(InputRequest(date, items))
        )
}

class ApiException(val code: Int, override val message: String) : Exception(message)

@kotlinx.serialization.Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@kotlinx.serialization.Serializable
data class PredictRequest(
    val engine: String,
    val market: String
)
