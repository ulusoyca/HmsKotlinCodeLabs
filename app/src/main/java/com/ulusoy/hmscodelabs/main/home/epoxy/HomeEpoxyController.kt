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
