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

package com.ulusoy.hmscodelabs.main.locationkit

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulusoy.hmscodelabs.R
import com.ulusoy.hmscodelabs.databinding.FragmentLocationKitBinding
import dagger.android.support.DaggerFragment
import java.lang.StringBuilder
import javax.inject.Inject
import pub.devrel.easypermissions.EasyPermissions

private const val REQUEST_CODE_PERMISSIONS = 101
private const val ACCESS_BACKGROUND_LOCATION = "android.permission.ACCESS_BACKGROUND_LOCATION"

class LocationKitFragment : DaggerFragment() {

    private lateinit var binding: FragmentLocationKitBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: LocationKitViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationKitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkFineAndCoarseLocationPermissions()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkBackgroundLocationPermission()
        }

        val locationStringBuilder = StringBuilder()
        with(viewModel) {
            locations.observe(viewLifecycleOwner, Observer {
                for (location in it) {
                    locationStringBuilder.append("lat: ${location.latitude} long: ${location.longitude} accuracy: lat: ${location.accuracy}")
                }
                binding.locationKitResult.text = locationStringBuilder.toString()
            })
            locationAvailability.observe(viewLifecycleOwner, Observer {
                binding.locationKitAvailability.text = "Location Available: $it"
            })
            locationUpdateRemove.observe(viewLifecycleOwner, Observer { isUpdateRemoved ->
                if (isUpdateRemoved) {
                    binding.locationKitResult.text = getString(R.string.location_kit_location_update_removed)
                }
            })
        }

        binding.locationKitRequest.setOnClickListener {
            viewModel.onLocationUpdateRequest()
        }

        binding.locationKitRemove.setOnClickListener {
            viewModel.onLocationUpdateRemove()
        }
    }

    private fun checkFineAndCoarseLocationPermissions() {
        if (!EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.map_kit_permission_rationale),
                REQUEST_CODE_PERMISSIONS,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun checkBackgroundLocationPermission() {
        if (!EasyPermissions.hasPermissions(requireContext(), ACCESS_BACKGROUND_LOCATION)) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.map_kit_permission_rationale),
                REQUEST_CODE_PERMISSIONS,
                ACCESS_BACKGROUND_LOCATION
            )
        }
    }
}
