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

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huawei.hms.ads.identifier.AdvertisingIdClient
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdKitViewModel
@Inject constructor(
    private val context: Context
) : ViewModel() {

    private val _oaidLiveData = MutableLiveData<String>()
    val oaidLiveData: LiveData<String>
        get() = _oaidLiveData

    private val _stateLiveData = MutableLiveData<Boolean>()
    val stateLiveData: LiveData<Boolean>
        get() = _stateLiveData

    fun getOaid() {
        viewModelScope.launch(Dispatchers.IO) {
            AdvertisingIdClient.getAdvertisingIdInfo(context)?.let {
                _oaidLiveData.postValue(it.id)
                _stateLiveData.postValue(it.isLimitAdTrackingEnabled)
            }
        }
    }
}
