package com.nicolas.whfrp3companion

import android.app.Application
import android.content.Context
import com.nicolas.whfrp3database.PlayerFacade
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.applicationContext

class WHFRP3Companion : Application() {
    private val whfrp3ApplicationContexts = listOf(whfrp3ApplicationModule(this))

    override fun onCreate() {
        super.onCreate()

        startKoin(this, whfrp3ApplicationContexts)
    }
}

private fun whfrp3ApplicationModule(context: Context) = applicationContext {
    bean { PlayerFacade(context) }
}