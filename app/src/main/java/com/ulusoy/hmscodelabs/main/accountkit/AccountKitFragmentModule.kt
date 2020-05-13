package com.ulusoy.hmscodelabs.main.accountkit

import com.ulusoy.hmscodelabs.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AccountKitFragmentModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [AccountKitModule::class])
    abstract fun bindInfinitiveFragment(): AccountKitFragment
}
