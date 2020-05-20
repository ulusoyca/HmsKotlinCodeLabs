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

package com.ulusoy.hmscodelabs.main.mlkit

import android.util.SparseArray
import com.huawei.hms.mlsdk.common.MLAnalyzer
import com.huawei.hms.mlsdk.face.MLFace
import com.ulusoy.hmscodelabs.main.mlkit.ui.GraphicOverlay
import com.ulusoy.hmscodelabs.main.mlkit.ui.MLFaceGraphic

class FaceAnalyzerTransactor(
    private val graphicOverlay: GraphicOverlay
) : MLAnalyzer.MLTransactor<MLFace> {

    override fun transactResult(result: MLAnalyzer.Result<MLFace>?) {
        result?.let {
            val faceSparseArray: SparseArray<MLFace> = it.analyseList
            for (i in 0 until faceSparseArray.size()) {
                val graphic = MLFaceGraphic(graphicOverlay, faceSparseArray.valueAt(i))
                graphicOverlay.add(graphic)
            }
        }
    }

    override fun destroy() {
        graphicOverlay.clear()
    }
}
