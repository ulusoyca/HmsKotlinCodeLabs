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

package com.ulusoy.hmscodelabs.main.accountkit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class AccountKitViewModel
@Inject constructor(
    @Named(NAMED_ID_TOKEN)
    val huaweiIdTokenAuthServiceWithTokenId: HuaweiIdAuthService,
    @Named(NAMED_AUTH_TOKEN)
    val huaweiIdTokenAuthServiceWithAuthCode: HuaweiIdAuthService,
    @Named(NAMED_REQUEST_CODE_SIGN_IN_TOKEN_ID)
    val tokenIdSignInRequestCode: Int,
    @Named(NAMED_REQUEST_CODE_SIGN_IN_AUTH_CODE)
    val authCodeSignInRequestCode: Int
) : ViewModel() {

    private val _signOutLiveData = MutableLiveData<Boolean>()
    val signOutLiveData: LiveData<Boolean>
        get() = _signOutLiveData

    private var lastSignedInService: HuaweiIdAuthService? = null

    fun onSignInRequestResult(requestCode: Int) {
        lastSignedInService = if (requestCode == tokenIdSignInRequestCode) {
            huaweiIdTokenAuthServiceWithTokenId
        } else {
            huaweiIdTokenAuthServiceWithAuthCode
        }
    }

    fun onSignOutRequested() {
        lastSignedInService?.let {
            viewModelScope.launch {
                signOut(it)
            }
        }
    }

    private suspend fun signOut(service: HuaweiIdAuthService) {
        return suspendCancellableCoroutine { continuation ->
            val task = service.signOut()
            task.addOnCompleteListener { signOutTask ->
                if (signOutTask.isSuccessful) {
                    _signOutLiveData.postValue(true)
                    continuation.resume(Unit) {
                        Timber.d("Sign out task cancel: ${it.message}")
                    }
                } else {
                    _signOutLiveData.postValue(false)
                    continuation.resumeWithException(task.exception)
                }
            }
        }
    }
}
