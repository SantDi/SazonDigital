package com.sazon.digital

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SazonApp : Application() {
    override fun onCreate() {
        super.onCreate()
        com.google.android.material.color.DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
