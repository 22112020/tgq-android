package com.tgq.app.data

import android.content.Context
import android.content.SharedPreferences
import com.tgq.app.R

/**
 * Persists app config & admin session.
 * Server URL, admin token and username are stored in a single prefs file.
 */
class Session private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("tgq_native", Context.MODE_PRIVATE)

    var serverBase: String
        get() = prefs.getString(context.getString(R.string.server_pref), DEFAULT_SERVER)
            ?: DEFAULT_SERVER
        set(value) {
            var v = value.trim().trimEnd('/')
            if (v.startsWith("http://") || v.startsWith("https://")) {
                prefs.edit().putString(context.getString(R.string.server_pref), v).apply()
            }
        }

    var token: String
        get() = prefs.getString(context.getString(R.string.token_pref), "") ?: ""
        set(value) {
            prefs.edit().putString(context.getString(R.string.token_pref), value).apply()
        }

    var username: String
        get() = prefs.getString(context.getString(R.string.user_pref), "") ?: ""
        set(value) {
            prefs.edit().putString(context.getString(R.string.user_pref), value).apply()
        }

    val isAdmin: Boolean get() = token.isNotEmpty()

    fun clearSession() {
        token = ""
        username = ""
    }

    companion object {
        private const val DEFAULT_SERVER = "https://tgq.duaduasatusatu.qzz.io"

        @Volatile
        private var instance: Session? = null

        fun init(context: Context): Session {
            return instance ?: synchronized(this) {
                instance ?: Session(context.applicationContext).also { instance = it }
            }
        }

        fun get(): Session =
            instance ?: throw IllegalStateException("Session.init(context) must be called first")
    }
}
