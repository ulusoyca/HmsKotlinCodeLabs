package com.ulusoy.hmscodelabs.main.accountkit

import androidx.lifecycle.ViewModel
import com.ulusoy.hmscodelabs.FragmentScope
import com.ulusoy.hmscodelabs.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AccountKitModule {
    @FragmentScope
    @Binds
    @IntoMap
    @ViewModelKey(AccountKitViewModel::class)
    /* Note: the return type should be ViewModel */
    abstract fun bindViewModel(viewModel: AccountKitViewModel): ViewModel
}
