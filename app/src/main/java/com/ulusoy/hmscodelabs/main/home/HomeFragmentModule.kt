package com.ulusoy.hmscodelabs.main.home

import com.ulusoy.hmscodelabs.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeFragmentModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun bindInfinitiveFragment(): HomeFragment
}
