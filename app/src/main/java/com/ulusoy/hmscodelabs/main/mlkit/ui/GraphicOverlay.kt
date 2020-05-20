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

package com.ulusoy.hmscodelabs.main.mlkit.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.huawei.hms.mlsdk.common.LensEngine

class GraphicOverlay
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mPreviewWidth = 0
    private var mWidthScaleFactor = 1.0f
    private var previewHeight = 0
    private var mHeightScaleFactor = 1.0f
    private var mFacing = LensEngine.BACK_LENS
    private var currentGraphic: Graphic? = null

    abstract class Graphic(private val mOverlay: GraphicOverlay) {

        abstract fun draw(canvas: Canvas?)

        fun scaleX(horizontal: Float): Float {
            return horizontal * mOverlay.mWidthScaleFactor
        }

        fun scaleY(vertical: Float): Float {
            return vertical * mOverlay.mHeightScaleFactor
        }

        fun translateX(x: Float): Float {
            return if (mOverlay.mFacing == LensEngine.FRONT_LENS) {
                mOverlay.width - scaleX(x)
            } else {
                scaleX(x)
            }
        }

        fun translateY(y: Float): Float {
            return scaleY(y)
        }

        fun postInvalidate() {
            mOverlay.postInvalidate()
        }
    }

    fun clear() {
        currentGraphic = null
        postInvalidate()
    }

    fun add(graphic: Graphic) {
        currentGraphic = graphic
        postInvalidate()
    }

    fun setCameraInfo(previewWidth: Int, previewHeight: Int, facing: Int) {
            mPreviewWidth = previewWidth
            this.previewHeight = previewHeight
            mFacing = facing
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mPreviewWidth != 0 && previewHeight != 0) {
            mWidthScaleFactor = width.toFloat() / mPreviewWidth.toFloat()
            mHeightScaleFactor = height.toFloat() / previewHeight.toFloat()
        }
        currentGraphic?.draw(canvas)
    }
}
