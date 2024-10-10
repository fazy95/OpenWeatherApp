package com.coding.openweatherapp.utils


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices


object LocationUtils {

    private const val LOCATION_PERMISSION_REQUEST_CODE = 1001

    fun getCurrentLocation(context: Context, onResult: (Result<Location>) -> Unit) {
        if (hasLocationPermission(context)) {
            getLastKnownLocation(context, onResult)
        } else {
            requestLocationPermission(context)
            onResult(Result.failure(Exception("Location permission not granted")))
        }
    }

    private fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getLastKnownLocation(context: Context, onResult: (Result<Location>) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        onResult(Result.success(location))
                    } else {
                        onResult(Result.failure(Exception("Location is null")))
                    }
                }
                .addOnFailureListener { e ->
                    onResult(Result.failure(e))
                }
        } catch (e: SecurityException) {
            onResult(Result.failure(e))
        }
    }

    private fun requestLocationPermission(context: Context) {
        if (context is Activity) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        context: Context,
        onResult: (Result<Location>) -> Unit
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation(context, onResult)
            } else {
                onResult(Result.failure(Exception("Location permission denied")))
            }
        }
    }
}