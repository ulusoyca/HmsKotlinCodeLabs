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

import android.os.CancellationSignal
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huawei.hms.support.api.fido.bioauthn.*
import com.ulusoy.hmscodelabs.main.fido.util.HwBioAuthnCipherFactory
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FidoKitViewModel
@Inject constructor(
    private val fingerprintManager: FingerprintManager,
    @Named(NAMED_STORE_KEY)
    val storeKey: String,
    @Named(NAMED_STATUS_LIVE_DATA_KEY)
    private val _resultOutLiveData: MutableLiveData<String>,
    private val faceManager: FaceManager
) : ViewModel() {

    val resultOutLiveData: LiveData<String>
        get() = _resultOutLiveData

    fun onFingerprintWithoutCyrptoObject() {
        viewModelScope.launch(Dispatchers.Default) {
            // Checks whether fingerprint authentication is available.
            val errorCode = fingerprintManager.canAuth()
            if (errorCode == 0) {
                _resultOutLiveData.postValue("Can not authenticate. errorCode=$errorCode")
            } else {
                _resultOutLiveData.postValue(
                    """
                    Start fingerprint authentication without CryptoObject.
                    Authenticating......
                """.trimIndent()
                )
                fingerprintManager.auth()
            }
        }
    }

    fun onFingerprintWithCyrptoObject() {
        viewModelScope.launch(Dispatchers.Default) {
            // Checks whether fingerprint authentication is available.
            val errorCode = fingerprintManager.canAuth()
            if (errorCode == 0) {
                _resultOutLiveData.postValue("Can not authenticate. errorCode=$errorCode")
            } else {
                _resultOutLiveData.postValue(
                    """
                    Start fingerprint authentication with CryptoObject.
                    Authenticating......
                """.trimIndent()
                )

                // Construct CryptoObject.
                val cipher = HwBioAuthnCipherFactory(storeKey, true).getCipher()
                fingerprintManager.auth(CryptoObject(cipher))
            }
        }
    }

    fun onFaceAuthenticateWithoutCyrpto() {
        val callback = getBioAuthnCallback()

        // Checks whether face manager authentication is available.
        val errorCode = faceManager.canAuth()
        if (errorCode == 0) {
            _resultOutLiveData.postValue("Can not authenticate. errorCode=$errorCode")
        } else {
            _resultOutLiveData.postValue(
                """
                    Start facemanager authentication with CryptoObject.
                    Authenticating......
                """.trimIndent()
            )

            // Recommended CryptoObject to be set to null. KeyStore is not associated with face authentication in current
            // version. KeyGenParameterSpec.Builder.setUserAuthenticationRequired() must be set false in this scenario.
            val crypto: CryptoObject? = null
            faceManager.auth(crypto, CancellationSignal(), 0, callback, null)
        }
    }

    private fun getBioAuthnCallback(): BioAuthnCallback {
        return object : BioAuthnCallback() {
            override fun onAuthError(errMsgId: Int, errString: CharSequence) {
                val msg = if (errMsgId == 1012) {
                    "The camera permission may not be enabled."
                } else {
                    ""
                }
                _resultOutLiveData.value =
                    "Authentication error. errorCode=$errMsgId,errorMessage=$errString msg=$msg"
            }

            override fun onAuthHelp(helpMsgId: Int, helpString: CharSequence) {
                _resultOutLiveData.value =
                    "Authentication help. helpMsgId=$helpMsgId,helpString=$helpString"
            }

            override fun onAuthSucceeded(result: BioAuthnResult) {
                _resultOutLiveData.value =
                    "Authentication succeeded. CryptoObject=" + result.cryptoObject
            }

            override fun onAuthFailed() {
                _resultOutLiveData.value = "Authentication failed."
            }
        }
    }
}
