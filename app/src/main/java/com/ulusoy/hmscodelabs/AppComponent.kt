package com.ulusoy.hmscodelabs

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

/**
 * Application component refers to application level modules only
 */
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ContributeActivityModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : AndroidInjector<HmsCodelabsApp> {
    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<HmsCodelabsApp>
}
