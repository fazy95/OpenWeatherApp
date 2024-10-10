package com.coding.openweatherapp.domain.usecase

import com.coding.openweatherapp.data.model.WeatherData
import com.coding.openweatherapp.domain.repository.Repository

class GetWeatherForCityUseCase(private val weatherRepository: Repository) {
    suspend operator fun invoke(cityName: String): Result<WeatherData> {
        weatherRepository.saveLastSearchedCity(cityName)
        return weatherRepository.getWeatherForCity(cityName)
    }
}