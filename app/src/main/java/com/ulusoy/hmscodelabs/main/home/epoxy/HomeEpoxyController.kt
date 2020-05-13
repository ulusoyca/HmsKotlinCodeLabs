package com.ulusoy.hmscodelabs.main.home.epoxy

import com.airbnb.epoxy.EpoxyController
import com.ulusoy.hmscodelabs.main.home.TopicSelectedListener
import com.ulusoy.hmscodelabs.topic

class HomeEpoxyController(
    private val topics: List<Int>,
    private val topicSelectedListener: TopicSelectedListener
) : EpoxyController() {

    init {
        requestModelBuild()
    }

    override fun buildModels() {
        for ((index, topicName) in topics.withIndex()) {
            topic {
                id(index)
                topicSelectedListener(topicSelectedListener)
                topic(topicName)
            }
        }
    }
}
