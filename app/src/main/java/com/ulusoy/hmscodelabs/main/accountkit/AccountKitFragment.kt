package com.ulusoy.hmscodelabs.main.accountkit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ulusoy.hmscodelabs.R
import dagger.android.support.DaggerFragment

class AccountKitFragment : DaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_kit, container, false)
    }
}
