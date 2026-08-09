package com.tgq.app

import android.app.Application
import com.tgq.app.data.Session

class TgqApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Session.init(this)
    }
}
