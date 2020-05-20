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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huawei.hms.support.api.fido.bioauthn.BioAuthnCallback
import com.huawei.hms.support.api.fido.bioauthn.BioAuthnResult
import com.huawei.hms.support.api.fido.bioauthn.FaceManager
import com.huawei.hms.support.api.fido.bioauthn.FingerprintManager
import com.ulusoy.hmscodelabs.FragmentScope
import com.ulusoy.hmscodelabs.ViewModelKey
import com.ulusoy.hmscodelabs.main.MainActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import java.util.concurrent.Executors
import javax.inject.Named

const val NAMED_STORE_KEY = "STORE_KEY"
const val NAMED_STATUS_LIVE_DATA_KEY = "STATUS_LIVE_DATA"

@Module
abstract class FidoKitModule {
    @FragmentScope
    @Binds
    @IntoMap
    @ViewModelKey(FidoKitViewModel::class)
    /* Note: the return type should be ViewModel */
    abstract fun bindViewModel(viewModel: FidoKitViewModel): ViewModel

    companion object {
        @Provides
        @FragmentScope
        @Named(NAMED_STORE_KEY)
        fun provideStoreKey(): String = "hw_test_fingerprint"

        @Provides
        @FragmentScope
        @Named(NAMED_STATUS_LIVE_DATA_KEY)
        fun provideStatusLiveData() = MutableLiveData<String>()

        @Provides
        @FragmentScope
        fun provideFingerprintManager(
            mainActivity: MainActivity,
            @Named(NAMED_STATUS_LIVE_DATA_KEY) statusLiveData: MutableLiveData<String>
        ): FingerprintManager {
            val callback: BioAuthnCallback = object : BioAuthnCallback() {
                override fun onAuthError(errMsgId: Int, errString: CharSequence) {
                    statusLiveData.value =
                        "Authentication error. errorCode=$errMsgId,errorMessage=$errString"
                }

                override fun onAuthSucceeded(result: BioAuthnResult) {
                    statusLiveData.value =
                        "Authentication succeeded. CryptoObject= ${result.cryptoObject}"
                }

                override fun onAuthFailed() {
                    statusLiveData.value = "Authentication failed."
                }
            }
            return FingerprintManager(
                mainActivity,
                Executors.newSingleThreadExecutor(),
                callback
            )
        }

        @Provides
        @FragmentScope
        fun provideFaceManager(mainActivity: MainActivity) = FaceManager(mainActivity)
    }
}
