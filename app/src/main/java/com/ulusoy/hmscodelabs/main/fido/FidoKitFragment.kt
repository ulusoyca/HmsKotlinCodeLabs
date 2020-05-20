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

package com.ulusoy.hmscodelabs.main.fido

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulusoy.hmscodelabs.R
import com.ulusoy.hmscodelabs.databinding.FragmentFidoKitBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

private const val REQUEST_CODE_PERMISSIONS = 101

class FidoKitFragment : DaggerFragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: FragmentFidoKitBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: FidoKitViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFidoKitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            binding.fingerprintWithoutCyrpto.setOnClickListener {
                onFingerprintWithoutCyrptoObject()
            }
            binding.fingerprintWithCyrpto.setOnClickListener {
                onFingerprintWithCyrptoObject()
            }
            binding.fingerprintWithCyrpto.setOnClickListener {
                EasyPermissions.requestPermissions(
                    requireActivity(),
                    getString(R.string.permission_rationale),
                    REQUEST_CODE_PERMISSIONS,
                    Manifest.permission.CAMERA
                )
                if (hasCameraPermission()) {
                    onFaceAuthenticateWithoutCyrpto()
                }
            }
            resultOutLiveData.observe(viewLifecycleOwner, Observer { setMessage(it) })
        }
    }

    private fun setMessage(msg: String) {
        binding.resultTextView.text = msg
    }

    private fun hasCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        // This will display a dialog directing them to enable the permission in app settings.
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        viewModel.onFaceAuthenticateWithoutCyrpto()
    }
}
