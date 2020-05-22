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

package com.ulusoy.hmscodelabs.main.pushkit

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class PushKitViewModel
@Inject constructor(
    private val context: Context
) : ViewModel() {

    private val _pushTokenLiveData = MutableLiveData<String>()
    val pushToken: LiveData<String>
        get() = _pushTokenLiveData

    fun getToken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // read from agconnect-services.json
                val appId =
                    AGConnectServicesConfig.fromContext(context).getString("client/app_id")
                val pushtoken = HmsInstanceId.getInstance(context).getToken(appId, "HCM")
                if (!TextUtils.isEmpty(pushtoken)) {
                    _pushTokenLiveData.postValue(pushtoken)
                }
            } catch (e: Exception) {
                Timber.w("getToken failed: ${e.message})")
            }
        }
    }
}
