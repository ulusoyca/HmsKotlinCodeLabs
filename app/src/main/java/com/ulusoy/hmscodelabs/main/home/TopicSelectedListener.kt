package com.ulusoy.hmscodelabs.main.home

import androidx.annotation.StringRes

interface TopicSelectedListener {
    fun onTopicSelected(@StringRes topic: Int)
}
