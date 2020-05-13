package com.ulusoy.hmscodelabs.main.accountkit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.ulusoy.hmscodelabs.R
import com.ulusoy.hmscodelabs.databinding.FragmentAccountKitBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class AccountKitFragment : DaggerFragment() {

    private lateinit var binding: FragmentAccountKitBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AccountKitViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountKitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.accountKitIdToken.setOnClickListener {
            this@AccountKitFragment.startActivityForResult(
                viewModel.huaweiIdTokenAuthServiceWithTokenId.signInIntent,
                viewModel.tokenIdSignInRequestCode
            )
        }

        binding.accountKitAuthMode.setOnClickListener {
            this@AccountKitFragment.startActivityForResult(
                viewModel.huaweiIdTokenAuthServiceWithAuthCode.signInIntent,
                viewModel.authCodeSignInRequestCode
            )
        }

        binding.accountKitSignOut.setOnClickListener {
            viewModel.onSignOutRequested()
        }

        viewModel.signOutLiveData.observe(viewLifecycleOwner, Observer { signedOut ->
            if (signedOut) {
                val currentText = binding.accountKitResult.text
                binding.accountKitResult.text = "$currentText\n\nSigned out"
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            if (requestCode == viewModel.tokenIdSignInRequestCode ||
                requestCode == viewModel.authCodeSignInRequestCode
            ) {
                // Login Success
                val authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data)
                viewModel.onSignInRequestResult(requestCode)
                val method = if (requestCode == viewModel.tokenIdSignInRequestCode) {
                    getString(R.string.account_kit_id_token_mode)
                } else {
                    getString(R.string.account_kit_auth_mode)
                }
                if (authHuaweiIdTask.isSuccessful) {
                    val huaweiAccount = authHuaweiIdTask.result
                    huaweiAccount.accessToken
                    binding.accountKitResult.text =
                        """
                            $method success
                            display name = ${huaweiAccount.displayName}
                            
                            unionId = ${huaweiAccount.unionId}
                            
                            openId = ${huaweiAccount.openId}
                        """.trimIndent()
                } else {
                    binding.accountKitResult.text =
                        "$method failed:${(authHuaweiIdTask.exception as ApiException).statusCode}"
                }
            }
        }
    }
}
