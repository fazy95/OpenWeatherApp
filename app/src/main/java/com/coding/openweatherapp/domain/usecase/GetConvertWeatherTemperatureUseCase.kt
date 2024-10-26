package com.coding.openweatherapp.domain.usecase

class GetConvertWeatherTemperatureUseCase() {
    operator fun invoke(celsius: Double): Double {
        return (celsius * 9 / 5) + 32
    }
}