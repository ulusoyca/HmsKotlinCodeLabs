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

package com.ulusoy.hmscodelabs.main.analyticskit

import android.content.Context
import androidx.lifecycle.ViewModel
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.ulusoy.hmscodelabs.FragmentScope
import com.ulusoy.hmscodelabs.R
import com.ulusoy.hmscodelabs.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

const val NAMED_QUESTIONS = "Questions"
const val NAMED_ANSWERS = "Answers"

@Module
abstract class AnalyticsKitModule {
    @FragmentScope
    @Binds
    @IntoMap
    @ViewModelKey(AnalyticsKitViewModel::class)
    /* Note: the return type should be ViewModel */
    abstract fun bindViewModel(viewModel: AnalyticsKitViewModel): ViewModel

    companion object {
        @Provides
        @Named(NAMED_QUESTIONS)
        @FragmentScope
        fun provideQuestions(context: Context) = listOf(
            context.getString(R.string.q1),
            context.getString(R.string.q2),
            context.getString(R.string.q3),
            context.getString(R.string.q4),
            context.getString(R.string.q5)
        )

        @Provides
        @Named(NAMED_ANSWERS)
        @FragmentScope
        fun provideAnswers() = listOf(
            true,
            true,
            false,
            false,
            true
        )

        @Provides
        @FragmentScope
        fun provideAnalyticsInstance(context: Context): HiAnalyticsInstance =
            HiAnalytics.getInstance(context).apply {
                setAnalyticsEnabled(true)
                regHmsSvcEvent()
            }
    }
}
