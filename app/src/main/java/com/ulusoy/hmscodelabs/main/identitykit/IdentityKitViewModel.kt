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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huawei.hms.identity.AddressClient
import com.huawei.hms.identity.entity.GetUserAddressResult
import com.huawei.hms.identity.entity.UserAddressRequest
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IdentityKitViewModel
@Inject constructor(
    private val addressClient: AddressClient
) : ViewModel() {

    private val _userAddressResult = MutableLiveData<GetUserAddressResult>()
    val userAddressResult: LiveData<GetUserAddressResult>
        get() = _userAddressResult

    private val _errorStateLiveData = MutableLiveData<String>()
    val errorStateLiveData: LiveData<String>
        get() = _errorStateLiveData

    fun getUserAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            val task = addressClient.getUserAddress(UserAddressRequest())
            task.addOnSuccessListener {
                _userAddressResult.postValue(it)
            }
            task.addOnFailureListener {
                _errorStateLiveData.postValue(it.message)
            }
        }
    }
}
