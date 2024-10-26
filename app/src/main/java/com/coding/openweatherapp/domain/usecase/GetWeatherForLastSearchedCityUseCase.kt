package com.coding.openweatherapp.domain.usecase

import com.coding.openweatherapp.domain.repository.WeatherRepository

class GetWeatherForLastSearchedCityUseCase(private val weatherRepository: WeatherRepository){
    operator fun invoke(): String? {
        return weatherRepository.getLastSearchedCity()
    }
}