package com.ulusoy.hmscodelabs.main

import com.ulusoy.hmscodelabs.ActivityScope
import com.ulusoy.hmscodelabs.main.accountkit.AccountKitFragmentModule
import com.ulusoy.hmscodelabs.main.home.HomeFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            HomeFragmentModule::class,
            AccountKitFragmentModule::class,
            MainModule::class
        ]
    )
    abstract fun contributeMainActivityInjector(): MainActivity
}
