package com.eatfair.orderapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EatFairApp: Application() {

    override fun onCreate() {
        super.onCreate()

    }
}
