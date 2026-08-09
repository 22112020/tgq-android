package com.tgq.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tgq.app.data.ApiClient
import com.tgq.app.data.ApiException
import com.tgq.app.data.HokiResponse
import com.tgq.app.data.LunaParser
import com.tgq.app.data.MarketInfo
import com.tgq.app.data.PredictResponse
import com.tgq.app.data.Session
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

data class AppUiState(
    val booting: Boolean = true,
    val loading: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val loggedIn: Boolean = false,
    val username: String = "",
    val serverBase: String = "",
    val hoki: HokiResponse? = null,
    val markets: List<MarketInfo> = emptyList(),
    val engines: List<String> = emptyList(),
    val lastPrediction: PredictResponse? = null
)

class AppViewModel : ViewModel() {

    private val api = ApiClient()
    private val _ui = MutableStateFlow(AppUiState())
    val ui: StateFlow<AppUiState> = _ui.asStateFlow()

    fun boot() {
        val session = Session.get()
        _ui.update {
            it.copy(booting = false, loggedIn = session.isAdmin, username = session.username, serverBase = session.serverBase)
        }
        if (session.isAdmin) refreshAll()
    }

    fun clearTransient() {
        _ui.update { it.copy(message = null, error = null) }
    }

    fun refreshAll() {
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, error = null) }
            val hoki = async { runCatching { api.getHoki() }.getOrNull() }
            val markets = async { runCatching { api.getMarkets() }.getOrNull() }
            val engines = async { runCatching { api.getEngines() }.getOrNull() }
            val h = hoki.await()
            val m = markets.await()
            val e = engines.await()
            _ui.update {
                it.copy(
                    loading = false,
                    hoki = h,
                    markets = m?.markets ?: it.markets,
                    engines = e?.engines ?: it.engines,
                    error = if (h == null && m == null) "Gagal terhubung ke server TGQ." else null
                )
            }
        }
    }

    fun refreshMarkets() {
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, error = null) }
            val m = runCatching { api.getMarkets() }.getOrNull()
            _ui.update {
                it.copy(
                    loading = false,
                    markets = m?.markets ?: it.markets,
                    error = if (m == null) "Gagal memuat pasaran." else null
                )
            }
        }
    }

    fun refreshHoki() {
        viewModelScope.launch {
            _ui.update { it.copy(loading = true) }
            val h = runCatching { api.getHoki() }.getOrNull()
            _ui.update { it.copy(loading = false, hoki = h) }
        }
    }

    fun login(username: String, password: String, onDone: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, error = null) }
            try {
                val res = api.login(username.trim(), password)
                val session = Session.get()
                session.token = res.token
                session.username = res.username
                _ui.update { it.copy(loading = false, loggedIn = true, username = res.username) }
                refreshAll()
                onDone(true, null)
            } catch (ex: ApiException) {
                _ui.update { it.copy(loading = false, error = ex.message) }
                onDone(false, ex.message)
            } catch (ex: Exception) {
                _ui.update { it.copy(loading = false, error = "Gagal terhubung: ${ex.message}") }
                onDone(false, "Gagal terhubung ke server.")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            runCatching { api.postLogout() }
            Session.get().clearSession()
            _ui.update {
                it.copy(
                    loggedIn = false, username = "",
                    hoki = null, markets = emptyList(), lastPrediction = null
                )
            }
        }
    }

    fun setServer(url: String) {
        Session.get().serverBase = url
        _ui.update { it.copy(serverBase = Session.get().serverBase, message = "Server diubah.") }
        refreshAll()
    }

    fun predict(market: String) {
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, error = null) }
            try {
                val engine = resolveEngine(market)
                val res = api.predict(engine, market)
                _ui.update { it.copy(loading = false, lastPrediction = res) }
            } catch (ex: ApiException) {
                _ui.update { it.copy(loading = false, error = ex.message) }
            } catch (ex: Exception) {
                _ui.update { it.copy(loading = false, error = "Gagal: ${ex.message}") }
            }
        }
    }

    private suspend fun resolveEngine(market: String): String {
        val map = runCatching { api.getEngines() }.getOrNull()?.marketEngineMap
        map?.get(market)?.let { return it }
        return "historical_trend"
    }

    /** Port of Luna Parse: parse pasted text, submit to /api/input, return summary message. */
    suspend fun parseAndSubmit(text: String): String {
        val items = LunaParser.parse(text)
            .map { com.tgq.app.data.InputItem(it.market, it.result, it.period) }
        if (items.isEmpty()) return "Tidak ada data valid ditemukan."
        val date = todayWib()
        val res = api.postInput(date, items)
        val detail = if (res.errors?.isNotEmpty() == true) " · " + res.errors.joinToString("; ") else ""
        return if (res.success) "Luna Parse: ${res.saved} data tersimpan!$detail"
        else "Gagal menyimpan${if (res.message.isNotBlank()) ": ${res.message}" else ""}$detail"
    }

    private fun todayWib(): String {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"))
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH) + 1
        val d = cal.get(Calendar.DAY_OF_MONTH)
        return "%04d-%02d-%02d".format(y, m, d)
    }
}
