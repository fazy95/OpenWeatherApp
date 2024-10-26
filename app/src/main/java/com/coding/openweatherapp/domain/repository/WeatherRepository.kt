package com.coding.openweatherapp.domain.repository

import com.coding.openweatherapp.data.model.WeatherData

interface WeatherRepository {
    suspend fun getWeatherForCity(cityName: String): Result<WeatherData>
    suspend fun getWeatherForLocation(latitude: Double, longitude: Double): Result<WeatherData>
    fun getLastSearchedCity(): String?
    fun saveLastSearchedCity(cityName: String)
}