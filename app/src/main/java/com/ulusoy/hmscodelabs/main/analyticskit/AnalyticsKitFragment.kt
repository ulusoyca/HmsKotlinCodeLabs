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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.huawei.hms.analytics.HiAnalyticsTools
import com.ulusoy.hmscodelabs.R
import com.ulusoy.hmscodelabs.databinding.FragmentAnalyticsKitBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class AnalyticsKitFragment : DaggerFragment() {

    private lateinit var binding: FragmentAnalyticsKitBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AnalyticsKitViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalyticsKitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HiAnalyticsTools.enableLog()

        with(viewModel) {
            currentQuestionLiveData.observe(viewLifecycleOwner, Observer {
                binding.question.text = it
            })

            answerResult.observe(viewLifecycleOwner, Observer { resultCorrect ->
                if (resultCorrect) {
                    Toast.makeText(context, R.string.correct_answer, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, R.string.wrong_answer, Toast.LENGTH_SHORT).show()
                }
            })

            binding.settings.setOnClickListener {
                findNavController().navigate(
                    AnalyticsKitFragmentDirections.actionAnalyticsKitFragmentToAnalyticsSettingsFragment()
                )
            }

            binding.next.setOnClickListener { nextQuestion() }

            binding.trueAnswer.setOnClickListener { trueAnswerClick() }

            binding.falseAnswer.setOnClickListener { falseAnswerClick() }

            binding.postScore.setOnClickListener { postScore() }
        }
    }
}
