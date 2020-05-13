package com.ulusoy.hmscodelabs

import android.content.Context
import dagger.Binds
import dagger.Module

/**
 * Application module refers to sub components and provides application level dependencies.
 */
@Module
internal abstract class AppModule {
    @Binds
    abstract fun provideContext(application: HmsCodelabsApp): Context
}
