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

package com.ulusoy.hmscodelabs.main.locationkit

import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huawei.hms.location.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber

class LocationKitViewModel
@Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val settingsClient: SettingsClient,
    private val mainLooper: Looper
) : ViewModel() {

    private val _locationAvailability = MutableLiveData<Boolean>()
    val locationAvailability: LiveData<Boolean>
        get() = _locationAvailability

    private val _locationUpdateRemove = MutableLiveData<Boolean>()
    val locationUpdateRemove: LiveData<Boolean>
        get() = _locationUpdateRemove

    private val _locations = MutableLiveData<List<Location>>()
    val locations: LiveData<List<Location>>
        get() = _locations

    private var locationCallback: LocationCallback? = null

    private val locationRequest = LocationRequest()
        .setInterval(10000)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    private val locationSettingsRequest = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
        .build()

    fun onLocationUpdateRequest() {
        viewModelScope.launch {
            val locations = getLocations()
            _locations.postValue(locations)
        }
    }

    private suspend fun getLocations(): List<Location> {
        return suspendCancellableCoroutine { continuation ->
            val task = settingsClient.checkLocationSettings(locationSettingsRequest)
            task.addOnSuccessListener {
                locationCallback = NewLocationCallback(
                    _locationAvailability,
                    continuation
                )
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    mainLooper
                )
            }
            task.addOnFailureListener {
                Timber.d("Error on location check: ${it.message}")
            }
        }
    }

    fun onLocationUpdateRemove() {
        viewModelScope.launch {
            removeLocationUpdates()
        }
    }

    private suspend fun removeLocationUpdates(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val task = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            task.addOnSuccessListener {
                continuation.resume(true)
            }
            task.addOnFailureListener {
                continuation.resume(false)
            }
        }
    }

    private class NewLocationCallback(
        val locationAvailabilityLiveData: MutableLiveData<Boolean>,
        val continuation: CancellableContinuation<List<Location>>
    ) : LocationCallback() {
        override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
            val isAvailable = locationAvailability?.isLocationAvailable ?: false
            locationAvailabilityLiveData.postValue(isAvailable)
        }

        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult != null) {
                continuation.resume(locationResult.locations)
            }
        }
    }
}
