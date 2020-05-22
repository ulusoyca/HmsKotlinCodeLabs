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
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.ulusoy.hmscodelabs.databinding.FragmentAnalyticsSettingsBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

private const val SCAN_FRAME_SIZE = 300

// declare the key ,used to get the value returned from scankit
private const val SCAN_RESULT = "scanResult"

class AnalyticsSettingsFragment : DaggerFragment() {

    private lateinit var binding: FragmentAnalyticsSettingsBinding

    @Inject
    lateinit var analyticsInstance: HiAnalyticsInstance

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalyticsSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveSettingButton.setOnClickListener {
            analyticsInstance.setUserProfile("favor_sport", binding.textField.editText?.text.toString())
        }
    }
}
