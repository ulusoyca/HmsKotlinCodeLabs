/*
 * Copyright 2020 Cagatay Ulusoy (Ulus Oy Apps). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ulusoy.hmscodelabs.main

import com.ulusoy.hmscodelabs.ActivityScope
import com.ulusoy.hmscodelabs.main.accountkit.AccountKitFragmentModule
import com.ulusoy.hmscodelabs.main.adkit.AdKitFragmentModule
import com.ulusoy.hmscodelabs.main.home.HomeFragmentModule
import com.ulusoy.hmscodelabs.main.locationkit.LocationKitFragmentModule
import com.ulusoy.hmscodelabs.main.mlkit.MlKitFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            HomeFragmentModule::class,
            AccountKitFragmentModule::class,
            LocationKitFragmentModule::class,
            AdKitFragmentModule::class,
            MlKitFragmentModule::class,
            MainModule::class
        ]
    )
    abstract fun contributeMainActivityInjector(): MainActivity
}
