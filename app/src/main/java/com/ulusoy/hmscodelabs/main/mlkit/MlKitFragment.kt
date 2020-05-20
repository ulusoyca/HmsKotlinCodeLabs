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

package com.ulusoy.hmscodelabs.main.mlkit

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.huawei.hms.mlsdk.common.LensEngine
import com.huawei.hms.mlsdk.face.MLFaceAnalyzer
import com.ulusoy.hmscodelabs.R
import com.ulusoy.hmscodelabs.databinding.FragmentMlKitBinding
import dagger.Lazy
import dagger.android.support.DaggerFragment
import java.io.IOException
import javax.inject.Inject
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

private const val REQUEST_CODE_PERMISSIONS = 101

class MlKitFragment : DaggerFragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: FragmentMlKitBinding

    @Inject
    lateinit var mlFaceAnalyzer: MLFaceAnalyzer

    @Inject
    lateinit var lensEngine: Lazy<LensEngine>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMlKitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mlFaceAnalyzer.setTransactor(FaceAnalyzerTransactor(binding.overlay))
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.permission_rationale),
            REQUEST_CODE_PERMISSIONS,
            Manifest.permission.CAMERA
        )
    }

    override fun onResume() {
        super.onResume()
        if (hasCameraPermission()) {
            startLensEngine()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.preview.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        lensEngine.get().release()
        mlFaceAnalyzer.destroy()
    }

    private fun startLensEngine() {
        try {
            binding.preview.start(lensEngine.get(), binding.overlay)
        } catch (e: IOException) {
            Timber.e("Failed to start lens engine: ${e.message}")
            this.lensEngine.get().release()
        }
    }

    private fun hasCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        // This will display a dialog directing them to enable the permission in app settings.
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        startLensEngine()
    }
}
