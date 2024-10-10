package com.coding.openweatherapp.data.source.remoteDataSource

import com.coding.openweatherapp.data.model.WeatherData

interface RemoteDataSource {
    suspend fun getWeatherForCity(cityName: String): Result<WeatherData>
    suspend fun getWeatherForLocation(latitude: Double, longitude: Double): Result<WeatherData>
}