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

package com.ulusoy.hmscodelabs.main.adkit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulusoy.hmscodelabs.R
import com.ulusoy.hmscodelabs.databinding.FragmentAdKitBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class AdKitFragment : DaggerFragment() {

    private lateinit var binding: FragmentAdKitBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AdKitViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdKitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            getOaid()
            stateLiveData.observe(viewLifecycleOwner, Observer { isLimitAdTrackingEnabled ->
                binding.adKitAdsPersonalizationStatusState.text = requireContext().getString(
                    if (isLimitAdTrackingEnabled) {
                        R.string.enabled
                    } else {
                        R.string.disabled
                    }
                )
            })
            oaidLiveData.observe(viewLifecycleOwner, Observer { oaid ->
                binding.adKitValueOaid.text = oaid
            })
        }
    }
}
