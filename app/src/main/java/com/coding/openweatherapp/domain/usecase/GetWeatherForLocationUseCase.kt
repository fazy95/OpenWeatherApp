package com.coding.openweatherapp.domain.usecase

import com.coding.openweatherapp.data.model.WeatherData
import com.coding.openweatherapp.domain.repository.Repository

class GetWeatherForLocationUseCase(private val weatherRepository: Repository) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Result<WeatherData> {
        return weatherRepository.getWeatherForLocation(latitude, longitude)
    }
}