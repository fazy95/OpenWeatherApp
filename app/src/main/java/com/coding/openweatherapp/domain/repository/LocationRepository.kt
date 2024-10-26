package com.coding.openweatherapp.domain.repository

import android.location.Location

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<Location>
    fun hasLocationPermission(): Boolean
}