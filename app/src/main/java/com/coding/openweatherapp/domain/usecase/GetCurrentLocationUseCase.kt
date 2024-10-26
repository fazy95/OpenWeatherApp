package com.coding.openweatherapp.domain.usecase

import android.location.Location
import com.coding.openweatherapp.domain.repository.LocationRepository

class GetCurrentLocationUseCase (private val locationRepository: LocationRepository) {
    suspend operator fun invoke(): Result<Location> {
        return locationRepository.getCurrentLocation()
    }

}