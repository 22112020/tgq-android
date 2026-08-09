package com.tgq.app.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HokiResponse(
    @SerialName("success") val success: Boolean = false,
    @SerialName("main") val main: List<String> = emptyList(),
    @SerialName("backup") val backup: List<String> = emptyList(),
    @SerialName("main_str") val mainStr: String = "",
    @SerialName("backup_str") val backupStr: String = "",
    @SerialName("confidence") val confidence: Double = 0.0,
    @SerialName("stats") val stats: HokiStats? = null,
    @SerialName("date") val date: String = "",
    @SerialName("generated_at") val generatedAt: String = "",
    @SerialName("cached") val cached: Boolean = false
)

@Serializable
data class HokiStats(
    @SerialName("total_records_analyzed") val totalRecords: Int = 0,
    @SerialName("markets_analyzed") val markets: Int = 0,
    @SerialName("ranked_top10") val rankedTop10: List<String> = emptyList()
)

@Serializable
data class MarketsResponse(
    @SerialName("markets") val markets: List<MarketInfo> = emptyList(),
    @SerialName("count") val count: Int = 0
)

@Serializable
data class MarketInfo(
    @SerialName("name") val name: String = "",
    @SerialName("latest_result") val latestResult: String = "",
    @SerialName("latest_period") val latestPeriod: String = "",
    @SerialName("last_updated") val lastUpdated: String = ""
)

@Serializable
data class EnginesResponse(
    @SerialName("engines") val engines: List<String> = emptyList(),
    @SerialName("count") val count: Int = 0,
    @SerialName("market_engine_map") val marketEngineMap: Map<String, String> = emptyMap()
)

@Serializable
data class PredictResponse(
    @SerialName("success") val success: Boolean = false,
    @SerialName("engine") val engine: String = "",
    @SerialName("market") val market: String = "",
    @SerialName("target_period") val targetPeriod: String = "",
    @SerialName("prediction") val prediction: Prediction = Prediction(),
    @SerialName("confidence") val confidence: Double = 0.0,
    @SerialName("timestamp") val timestamp: String = ""
)

@Serializable
data class Prediction(
    @SerialName("main") val main: List<String> = emptyList(),
    @SerialName("backup") val backup: List<String> = emptyList()
)

@Serializable
data class LoginResponse(
    @SerialName("token") val token: String = "",
    @SerialName("username") val username: String = ""
)

@Serializable
data class AuthStatus(
    @SerialName("authenticated") val authenticated: Boolean = false,
    @SerialName("username") val username: String? = null
)

@Serializable
data class InputItem(
    @SerialName("market") val market: String,
    @SerialName("result") val result: String,
    @SerialName("period") val period: String
)

@Serializable
data class InputRequest(
    @SerialName("date") val date: String,
    @SerialName("items") val items: List<InputItem>
)

@Serializable
data class InputResponse(
    @SerialName("success") val success: Boolean = false,
    @SerialName("saved") val saved: Int = 0,
    @SerialName("errors") val errors: List<String>? = null,
    @SerialName("message") val message: String = ""
)

@Serializable
data class ApiError(
    @SerialName("detail") val detail: String = ""
)
