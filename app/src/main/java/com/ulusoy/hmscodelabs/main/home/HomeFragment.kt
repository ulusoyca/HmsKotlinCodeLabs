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

package com.ulusoy.hmscodelabs.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.ulusoy.hmscodelabs.R
import com.ulusoy.hmscodelabs.databinding.FragmentHomeBinding
import com.ulusoy.hmscodelabs.main.home.epoxy.HomeEpoxyController
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import javax.inject.Named

class HomeFragment : DaggerFragment(), TopicSelectedListener {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    @Named(NAMED_TOPIC_NAMES)
    lateinit var topics: List<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = HomeEpoxyController(
            topics = topics,
            topicSelectedListener = this@HomeFragment
        )
        binding.topicsRecyclerView.setController(controller)
    }

    override fun onTopicSelected(topic: Int) {
        val direction: NavDirections? = when (topic) {
            R.string.topic_name_account_kit -> HomeFragmentDirections.actionHomeFragmentToAccountKitFragment()
            R.string.topic_name_map_kit -> HomeFragmentDirections.actionHomeFragmentToMapKitFragment()
            R.string.topic_name_location_kit -> HomeFragmentDirections.actionHomeFragmentToLocationKitFragment()
            R.string.topic_name_ml_kit -> HomeFragmentDirections.actionHomeFragmentToMlKitFragment()
            R.string.topic_name_fido_kit -> HomeFragmentDirections.actionHomeFragmentToFidoKitFragment()
            R.string.topic_name_identity_kit -> HomeFragmentDirections.actionHomeFragmentToIdentityKitFragment()
            R.string.topic_name_scan_kit -> HomeFragmentDirections.actionHomeFragmentToScanKitFragment()
            R.string.topic_name_analytics_kit -> HomeFragmentDirections.actionHomeFragmentToAnalyticsKitFragment()
            else -> null
        }
        if (direction == null) {
            Toast.makeText(context, R.string.codelab_not_implemented, Toast.LENGTH_LONG).show()
        } else {
            findNavController().navigate(direction)
        }
    }
}
