package com.ulusoy.hmscodelabs

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class HmsCodelabsApp : DaggerApplication() {
    private val appComponent: AndroidInjector<HmsCodelabsApp> by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return appComponent
    }
}
