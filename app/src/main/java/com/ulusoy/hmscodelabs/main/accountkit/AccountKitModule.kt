package com.ulusoy.hmscodelabs.main.accountkit

import androidx.lifecycle.ViewModel
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService
import com.ulusoy.hmscodelabs.FragmentScope
import com.ulusoy.hmscodelabs.ViewModelKey
import com.ulusoy.hmscodelabs.main.MainActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

const val NAMED_ID_TOKEN = "NAMED_ID_TOKEN"
const val NAMED_AUTH_TOKEN = "NAMED_AUTH_TOKEN"
const val NAMED_REQUEST_CODE_SIGN_IN_TOKEN_ID = "NAMED_REQUEST_CODE_SIGN_IN_TOKEN_ID"
const val NAMED_REQUEST_CODE_SIGN_IN_AUTH_CODE = "NAMED_REQUEST_CODE_SIGN_IN_AUTH_CODE"

@Module
abstract class AccountKitModule {
    @FragmentScope
    @Binds
    @IntoMap
    @ViewModelKey(AccountKitViewModel::class)
    /* Note: the return type should be ViewModel */
    abstract fun bindViewModel(viewModel: AccountKitViewModel): ViewModel

    @Module
    companion object {
        @JvmStatic
        @Provides
        @Named(NAMED_ID_TOKEN)
        @FragmentScope
        fun provideHuaweiIdAuthServiceWithTokenId(mainActivity: MainActivity): HuaweiIdAuthService {
            // Request Authorization
            val huaweiIdAuthParams = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .createParams()
            // Call the getService method of HuaweiIdAuthManager to initialize the HuaweiIdAuthService object
            return HuaweiIdAuthManager.getService(mainActivity, huaweiIdAuthParams)
        }

        @JvmStatic
        @Provides
        @Named(NAMED_AUTH_TOKEN)
        @FragmentScope
        fun provideHuaweiIdAuthServiceWithAuthCode(mainActivity: MainActivity): HuaweiIdAuthService {
            // Request Authorization
            val huaweiIdAuthParams = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setAuthorizationCode()
                .createParams()
            // Call the getService method of HuaweiIdAuthManager to initialize the HuaweiIdAuthService object
            return HuaweiIdAuthManager.getService(mainActivity, huaweiIdAuthParams)
        }

        @JvmStatic
        @Provides
        @Named(NAMED_REQUEST_CODE_SIGN_IN_TOKEN_ID)
        @FragmentScope
        fun provideTokenIdSignInRequestCode(): Int = 0

        @JvmStatic
        @Provides
        @Named(NAMED_REQUEST_CODE_SIGN_IN_AUTH_CODE)
        @FragmentScope
        fun provideAuthCodeSignInRequestCode(): Int = 1
    }
}
