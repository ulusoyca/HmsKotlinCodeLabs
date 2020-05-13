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
            else -> null
        }
        if (direction == null) {
            Toast.makeText(context, R.string.codelab_not_implemented, Toast.LENGTH_LONG).show()
        } else {
            findNavController().navigate(direction)
        }
    }
}
