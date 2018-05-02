package com.nicolas.whfrp3companion

import android.app.Application
import android.content.Context
import com.nicolas.database.HandRepository
import com.nicolas.database.PlayerRepository
import com.nicolas.database.anko.AnkoHandRepository
import com.nicolas.database.anko.AnkoPlayerRepository
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
    bean { AnkoPlayerRepository(context) as PlayerRepository }
    bean { AnkoHandRepository(context) as HandRepository }
}