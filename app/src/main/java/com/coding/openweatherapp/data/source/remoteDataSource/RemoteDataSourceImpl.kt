package com.coding.openweatherapp.data.source.remoteDataSource

import com.coding.openweatherapp.data.model.WeatherData
import com.coding.openweatherapp.data.api.ApiService

class RemoteDataSourceImpl(private val api: ApiService) : RemoteDataSource {
    override suspend fun getWeatherForCity(cityName: String): Result<WeatherData> {
        return try {
            val response = api.getCurrentWeather(cityName, "9c10df011d0203dccc2932a10047bf8f")
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error fetching weather data: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWeatherForLocation(latitude: Double, longitude: Double): Result<WeatherData> {
        return try {
            val response = api.getCurrentWeatherByLocation(latitude, longitude, "9c10df011d0203dccc2932a10047bf8f")
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error fetching weather data: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}