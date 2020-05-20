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

import android.content.Context
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.LensEngine
import com.huawei.hms.mlsdk.face.MLFaceAnalyzer
import com.huawei.hms.mlsdk.face.MLFaceAnalyzerSetting
import com.ulusoy.hmscodelabs.FragmentScope
import dagger.Module
import dagger.Provides

@Module
abstract class MlKitModule {

    companion object {
        @Provides
        @FragmentScope
        fun provideFaceAnalyzer(): MLFaceAnalyzer = MLAnalyzerFactory.getInstance()
            .getFaceAnalyzer(
                MLFaceAnalyzerSetting.Factory()
                    .setFeatureType(MLFaceAnalyzerSetting.TYPE_FEATURES)
                    .setPerformanceType(MLFaceAnalyzerSetting.TYPE_SPEED)
                    .allowTracing()
                    .create()
            )

        @Provides
        @FragmentScope
        fun provideLensEngine(
            context: Context,
            analyzer: MLFaceAnalyzer
        ): LensEngine = LensEngine.Creator(context, analyzer)
            .setLensType(LensEngine.FRONT_LENS)
            .applyDisplayDimension(1600, 1024)
            .applyFps(25.0f)
            .enableAutomaticFocus(true)
            .create()
    }
}
