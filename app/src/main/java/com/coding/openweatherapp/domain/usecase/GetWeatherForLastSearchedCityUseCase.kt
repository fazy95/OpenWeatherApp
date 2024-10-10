package com.coding.openweatherapp.domain.usecase

import com.coding.openweatherapp.domain.repository.Repository

class GetWeatherForLastSearchedCityUseCase(private val weatherRepository: Repository){
    operator fun invoke(): String? {
        return weatherRepository.getLastSearchedCity()
    }
}