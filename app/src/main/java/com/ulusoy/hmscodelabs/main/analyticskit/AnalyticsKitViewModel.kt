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

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.type.HAEventType
import com.huawei.hms.analytics.type.HAParamType
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class AnalyticsKitViewModel
@Inject constructor(
    @Named(NAMED_QUESTIONS)
    private val questions: List<String>,
    @Named(NAMED_ANSWERS)
    private val answers: List<Boolean>,
    private val analyticsInstance: HiAnalyticsInstance
) : ViewModel() {

    private var currentQuestionIndex: Int = 0

    private var score = 0

    private val _currentQuestionLiveData = MutableLiveData<String>().apply {
        value = questions[currentQuestionIndex]
    }
    val currentQuestionLiveData: LiveData<String>
        get() = _currentQuestionLiveData

    private val _answerResult = MutableLiveData<Boolean>()
    val answerResult: LiveData<Boolean>
        get() = _answerResult

    fun nextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questions.size
        _currentQuestionLiveData.value = questions[currentQuestionIndex]
    }

    fun trueAnswerClick() {
        checkAnswer(true)
        reportAnswerEvent("true")
    }

    fun falseAnswerClick() {
        checkAnswer(false)
        reportAnswerEvent("false")
    }

    fun postScore() {
        val bundle = Bundle()
        bundle.putLong(HAParamType.SCORE, score.toLong())
        analyticsInstance.onEvent(HAEventType.SUBMITSCORE, bundle)
    }

    private fun checkAnswer(answer: Boolean) {
        if (answer == answers[currentQuestionIndex]) {
            score += 20
        }
    }

    private fun reportAnswerEvent(answer: String) {
        val bundle = Bundle().apply {
            putString("question", questions[currentQuestionIndex])
            putString("answer", answer)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            putString("answerTime", sdf.format(Date()))
        }
        // Report a pre-defined Event
        analyticsInstance.onEvent("Answer", bundle)
    }
}
