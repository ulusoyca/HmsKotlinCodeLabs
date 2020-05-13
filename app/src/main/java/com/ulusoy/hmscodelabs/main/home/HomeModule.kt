package com.ulusoy.hmscodelabs.main.home

import androidx.lifecycle.ViewModel
import com.ulusoy.hmscodelabs.FragmentScope
import com.ulusoy.hmscodelabs.R
import com.ulusoy.hmscodelabs.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

const val NAMED_TOPIC_NAMES = "topic_names"

@Module
abstract class HomeModule {
    @FragmentScope
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    /* Note: the return type should be ViewModel */
    abstract fun bindViewModel(viewModel: HomeViewModel): ViewModel

    @Module
    companion object {
        @JvmStatic
        @Provides
        @Named(NAMED_TOPIC_NAMES)
        fun provideTopicNames(): List<Int> = listOf(
            R.string.topic_name_site_kit,
            R.string.topic_name_scan_kit,
            R.string.topic_name_ml_kit,
            R.string.topic_name_wallet_kit,
            R.string.topic_name_safety_detect,
            R.string.topic_name_account_kit,
            R.string.topic_name_location_kit,
            R.string.topic_name_map_kit,
            R.string.topic_name_iap,
            R.string.topic_name_dtm,
            R.string.topic_name_drive_kit,
            R.string.topic_name_hms_toolkit,
            R.string.topic_name_nearby_service,
            R.string.topic_name_agp_firebase,
            R.string.topic_name_drm_kit,
            R.string.topic_name_panorama_kit,
            R.string.topic_name_awareness_kit,
            R.string.topic_name_health_kit
        )
    }
}
