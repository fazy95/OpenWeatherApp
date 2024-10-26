package com.coding.openweatherapp.data.repository

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.coding.openweatherapp.domain.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LocationRepositoryImpl(
private val context: Context,
private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Result<Location> = withContext(Dispatchers.Main) {
        try {
            if (!hasLocationPermission()) {
                return@withContext Result.failure(Exception("Location permission not granted"))
            }

            val location = fusedLocationProviderClient.lastLocation.await()
            location?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Location is null"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
