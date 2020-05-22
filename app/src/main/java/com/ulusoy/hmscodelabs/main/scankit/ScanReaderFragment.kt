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

package com.ulusoy.hmscodelabs.main.scankit

import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.ml.scan.HmsScan
import com.ulusoy.hmscodelabs.databinding.FragmentScanReaderBinding
import kotlin.math.roundToInt

private const val SCAN_FRAME_SIZE = 300

// declare the key ,used to get the value returned from scankit
private const val SCAN_RESULT = "scanResult"

class ScanReaderFragment : Fragment() {

    private lateinit var binding: FragmentScanReaderBinding

    private lateinit var remoteView: RemoteView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanReaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scanFrameSize = SCAN_FRAME_SIZE * resources.displayMetrics.density
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels

        // 3.caculate viewfinder's rect,it's in the middle of the layout
        // set scanning area(Optional, rect can be null,If not configure,default is in the center of layout)
        val rect = Rect().apply {
            left = (screenWidth / 2 - scanFrameSize / 2).roundToInt()
            right = (screenWidth / 2 + scanFrameSize / 2).roundToInt()
            top = (screenHeight / 2 - scanFrameSize / 2).roundToInt()
            bottom = (screenHeight / 2 + scanFrameSize / 2).roundToInt()
        }
        // initialize RemoteView instance, and set calling back for scanning result
        remoteView = RemoteView.Builder()
            .setContext(requireActivity())
            .setBoundingBox(rect)
            .setFormat(HmsScan.ALL_SCAN_TYPE)
            .build().apply {
                onCreate(savedInstanceState)
                setOnResultCallback { result ->
                    result?.takeIf { isResultEffective(it) }?.let { hmsScan ->
                        findNavController().navigate(
                            ScanReaderFragmentDirections.actionScanReaderFragmentToScanKitFragment(hmsScan[0])
                        )
                    }
                }
            }
        binding.rim.addView(
            remoteView, FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )
    }

    override fun onStart() {
        super.onStart()
        remoteView.onStart()
    }

    override fun onResume() {
        super.onResume()
        remoteView.onResume()
    }

    override fun onStop() {
        remoteView.onStop()
        super.onStop()
    }

    override fun onPause() {
        remoteView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        remoteView.onDestroy()
        super.onDestroy()
    }

    private fun isResultEffective(result: Array<HmsScan>) =
        result.isNotEmpty() && !TextUtils.isEmpty(result[0].getOriginalValue())
}
