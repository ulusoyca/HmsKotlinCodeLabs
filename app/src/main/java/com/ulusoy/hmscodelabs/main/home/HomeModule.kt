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

package com.ulusoy.hmscodelabs.main.home

import androidx.lifecycle.ViewModel
import com.ulusoy.hmscodelabs.FragmentScope
import com.ulusoy.hmscodelabs.R
import com.ulusoy.hmscodelabs.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

const val NAMED_TOPIC_NAMES = "topic_names"

@Module
abstract class HomeModule {
    @FragmentScope
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    /* Note: the return type should be ViewModel */
    abstract fun bindViewModel(viewModel: HomeViewModel): ViewModel

    companion object {
        @Provides
        @Named(NAMED_TOPIC_NAMES)
        fun provideTopicNames(): List<Int> = listOf(
            R.string.topic_name_ad_kit,
            R.string.topic_name_account_kit,
            R.string.topic_name_location_kit,
            R.string.topic_name_map_kit,
            R.string.topic_name_fido_kit,
            R.string.topic_name_identity_kit,
            R.string.topic_name_ml_kit,
            R.string.topic_name_scan_kit
        )
    }
}
