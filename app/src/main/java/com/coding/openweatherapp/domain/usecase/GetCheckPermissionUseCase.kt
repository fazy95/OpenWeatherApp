package com.coding.openweatherapp.domain.usecase

import com.coding.openweatherapp.domain.repository.LocationRepository

class GetCheckPermissionUseCase(private val locationRepository: LocationRepository) {
    operator fun invoke(): Boolean {
        return locationRepository.hasLocationPermission()
    }
}