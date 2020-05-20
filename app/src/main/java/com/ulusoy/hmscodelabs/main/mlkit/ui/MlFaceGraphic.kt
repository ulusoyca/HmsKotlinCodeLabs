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

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import com.huawei.hms.mlsdk.face.MLFace
import com.huawei.hms.mlsdk.face.MLFaceShape
import com.ulusoy.hmscodelabs.main.mlkit.ui.GraphicOverlay.Graphic
import java.text.DecimalFormat
import java.util.*

private const val BOX_STROKE_WIDTH = 8.0f
private const val LINE_WIDTH = 5.0f

class MLFaceGraphic(
    private val overlay: GraphicOverlay,
    private val mFace: MLFace?
) : Graphic(overlay) {

    private val facePositionPaint: Paint
    private val landmarkPaint: Paint
    private val boxPaint: Paint
    private val facePaint: Paint
    private val eyePaint: Paint
    private val eyebrowPaint: Paint
    private val lipPaint: Paint
    private val nosePaint: Paint
    private val noseBasePaint: Paint
    private val textPaint: Paint
    private val probabilityPaint: Paint

    init {
        val selectedColor = Color.WHITE
        facePositionPaint = Paint()
        facePositionPaint.color = selectedColor
        textPaint = Paint()
        textPaint.color = Color.WHITE
        textPaint.textSize = 24f
        textPaint.typeface = Typeface.DEFAULT
        probabilityPaint = Paint()
        probabilityPaint.color = Color.WHITE
        probabilityPaint.textSize = 35f
        probabilityPaint.typeface = Typeface.DEFAULT
        landmarkPaint = Paint()
        landmarkPaint.color = Color.RED
        landmarkPaint.style = Paint.Style.FILL
        landmarkPaint.strokeWidth = 10f
        boxPaint = Paint()
        boxPaint.color = Color.WHITE
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeWidth = BOX_STROKE_WIDTH
        facePaint = Paint()
        facePaint.color = Color.parseColor("#ffcc66")
        facePaint.style = Paint.Style.STROKE
        facePaint.strokeWidth = LINE_WIDTH
        eyePaint = Paint()
        eyePaint.color = Color.parseColor("#00ccff")
        eyePaint.style = Paint.Style.STROKE
        eyePaint.strokeWidth = LINE_WIDTH
        eyebrowPaint = Paint()
        eyebrowPaint.color = Color.parseColor("#006666")
        eyebrowPaint.style = Paint.Style.STROKE
        eyebrowPaint.strokeWidth = LINE_WIDTH
        nosePaint = Paint()
        nosePaint.color = Color.parseColor("#ffff00")
        nosePaint.style = Paint.Style.STROKE
        nosePaint.strokeWidth = LINE_WIDTH
        noseBasePaint = Paint()
        noseBasePaint.color = Color.parseColor("#ff6699")
        noseBasePaint.style = Paint.Style.STROKE
        noseBasePaint.strokeWidth = LINE_WIDTH
        lipPaint = Paint()
        lipPaint.color = Color.parseColor("#990000")
        lipPaint.style = Paint.Style.STROKE
        lipPaint.strokeWidth = LINE_WIDTH
    }

    private fun sortHashMap(map: HashMap<String, Float>): List<String> {
        val entry: Set<Map.Entry<String, Float>> =
            map.entries
        val list: MutableList<Map.Entry<String, Float>> =
            ArrayList(entry)
        list.sortWith(Comparator { o1, o2 ->
            if (o2.value - o1.value >= 0) {
                1
            } else {
                -1
            }
        })
        val emotions: MutableList<String> =
            ArrayList()
        for (i in 0..1) {
            emotions.add(list[i].key)
        }
        return emotions
    }

    override fun draw(canvas: Canvas?) {
        if (mFace == null) {
            return
        }
        val start = 350f
        var x = start
        val width = 500f
        var y = overlay.height - 300.0f

        val emotions = HashMap<String, Float>()
        emotions["Smiling"] = mFace.emotions.smilingProbability
        emotions["Neutral"] = mFace.emotions.neutralProbability
        emotions["Angry"] = mFace.emotions.angryProbability
        emotions["Fear"] = mFace.emotions.fearProbability
        emotions["Sad"] = mFace.emotions.sadProbability
        emotions["Disgust"] = mFace.emotions.disgustProbability
        emotions["Surprise"] = mFace.emotions.surpriseProbability

        val result = sortHashMap(emotions)
        val decimalFormat = DecimalFormat("0.000")
        canvas?.run {
            // Draw the facial feature value.
            drawText(
                "Left eye: " + decimalFormat.format(
                    mFace.features.leftEyeOpenProbability.toDouble()
                ), x, y, probabilityPaint
            )
            x += width
            drawText(
                "Right eye: " + decimalFormat.format(
                    mFace.features.rightEyeOpenProbability.toDouble()
                ), x, y, probabilityPaint
            )
            y -= 40.0f
            x = start

            // Draw the facial feature value.
            drawText(
                "Left eye: " + decimalFormat.format(
                    mFace.features.leftEyeOpenProbability.toDouble()
                ), x, y, probabilityPaint
            )
            x += width
            drawText(
                "Right eye: " + decimalFormat.format(
                    mFace.features.rightEyeOpenProbability.toDouble()
                ), x, y, probabilityPaint
            )
            y -= 40.0f
            x = start
            drawText(
                "Moustache Probability: " + decimalFormat.format(
                    mFace.features.moustacheProbability.toDouble()
                ), x, y, probabilityPaint
            )
            x += width
            drawText(
                "Glass Probability: " + decimalFormat.format(
                    mFace.features.sunGlassProbability.toDouble()
                ), x, y, probabilityPaint
            )
            y -= 40.0f
            x = start
            drawText(
                "Hat Probability: " + decimalFormat.format(
                    mFace.features.hatProbability.toDouble()
                ), x, y, probabilityPaint
            )
            x += width
            drawText("Age: " + mFace.features.age, x, y, probabilityPaint)
            y -= 40.0f
            x = start
            val sex =
                if (mFace.features.sexProbability > 0.5f) "Female" else "Male"
            drawText("Gender: $sex", x, y, probabilityPaint)
            x += width
            drawText(
                "EulerAngleY: " + decimalFormat.format(
                    mFace.rotationAngleY.toDouble()
                ), x, y, probabilityPaint
            )
            y -= 40.0f
            x = start
            drawText(
                "EulerAngleZ: " + decimalFormat.format(
                    mFace.rotationAngleZ.toDouble()
                ), x, y, probabilityPaint
            )
            x += width
            drawText(
                "EulerAngleX: " + decimalFormat.format(
                    mFace.rotationAngleX.toDouble()
                ), x, y, probabilityPaint
            )
            y -= 40.0f
            x = start
            drawText(result[0], x, y, probabilityPaint)

            // Draw a face contour.
            if (mFace.faceShapeList != null) {
                for (faceShape in mFace.faceShapeList) {
                    if (faceShape == null) {
                        continue
                    }
                    val points = faceShape.points
                    for (i in points.indices) {
                        val point = points[i]
                        drawPoint(
                            translateX(point!!.x.toFloat()),
                            translateY(point.y.toFloat()),
                            boxPaint
                        )
                        if (i != points.size - 1) {
                            val next = points[i + 1]
                            if (point.x != null && point.y != null) {
                                if (i % 3 == 0) {
                                    drawText(
                                        "${i + 1}",
                                        translateX(point.x.toFloat()),
                                        translateY(point.y.toFloat()),
                                        textPaint
                                    )
                                }
                                drawLines(
                                    floatArrayOf(
                                        translateX(point.x.toFloat()),
                                        translateY(point.y.toFloat()),
                                        translateX(next.x.toFloat()),
                                        translateY(next.y.toFloat())
                                    ), getPaint(faceShape)
                                )
                            }
                        }
                    }
                }
            }
            // Face Key Points
            for (keyPoint in mFace.faceKeyPoints) {
                if (keyPoint != null) {
                    val point = keyPoint.point
                    drawCircle(
                        translateX(point.x),
                        translateY(point.y),
                        10f, landmarkPaint
                    )
                }
            }
        }
    }

    private fun getPaint(faceShape: MLFaceShape): Paint {
        return when (faceShape.faceShapeType) {
            MLFaceShape.TYPE_LEFT_EYE, MLFaceShape.TYPE_RIGHT_EYE -> eyePaint
            MLFaceShape.TYPE_BOTTOM_OF_LEFT_EYEBROW, MLFaceShape.TYPE_BOTTOM_OF_RIGHT_EYEBROW, MLFaceShape.TYPE_TOP_OF_LEFT_EYEBROW, MLFaceShape.TYPE_TOP_OF_RIGHT_EYEBROW -> eyebrowPaint
            MLFaceShape.TYPE_BOTTOM_OF_LOWER_LIP, MLFaceShape.TYPE_TOP_OF_LOWER_LIP, MLFaceShape.TYPE_BOTTOM_OF_UPPER_LIP, MLFaceShape.TYPE_TOP_OF_UPPER_LIP -> lipPaint
            MLFaceShape.TYPE_BOTTOM_OF_NOSE -> noseBasePaint
            MLFaceShape.TYPE_BRIDGE_OF_NOSE -> nosePaint
            else -> facePaint
        }
    }
}
