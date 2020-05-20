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

package com.ulusoy.hmscodelabs.main.identitykit

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.huawei.hms.identity.entity.UserAddress
import com.ulusoy.hmscodelabs.R
import com.ulusoy.hmscodelabs.databinding.FragmentIdentityKitBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

private const val GET_ADDRESS = 101

class IdentityKitFragment : DaggerFragment() {

    private lateinit var binding: FragmentIdentityKitBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: IdentityKitViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIdentityKitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.getUserAddress.setOnClickListener {
            viewModel.getUserAddress()
        }
        with(viewModel) {
            errorStateLiveData.observe(viewLifecycleOwner, Observer { errorMessage ->
                binding.userAddressValue.text = errorMessage
            })
            userAddressResult.observe(viewLifecycleOwner, Observer { result ->
                if (result.returnCode == 0 && result.status.hasResolution()) {
                    result.status.startResolutionForResult(requireActivity(), GET_ADDRESS)
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GET_ADDRESS) {
            when (resultCode) {
                RESULT_OK -> {
                    UserAddress.parseIntent(data)?.let { userAddress ->
                        val address = StringBuilder()
                            .appendln("name: ${userAddress.name}")
                            .appendln("city: ${userAddress.administrativeArea}")
                            .appendln("area: ${userAddress.locality}")
                            .appendln("address: ${userAddress.addressLine1} ${userAddress.addressLine2}")
                            .appendln("phone: ${userAddress.phoneNumber}")
                            .toString()
                        binding.userAddressValue.text = address
                    }
                }
                RESULT_CANCELED -> {
                    binding.userAddressValue.text = getString(R.string.operation_cancel)
                }
            }
        }
    }
}
