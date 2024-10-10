package com.coding.openweatherapp.data.repository

import com.coding.openweatherapp.data.model.WeatherData
import com.coding.openweatherapp.data.source.localDataSource.LocalDataSource
import com.coding.openweatherapp.data.source.remoteDataSource.RemoteDataSource
import com.coding.openweatherapp.domain.repository.Repository

class RepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localWeatherDataSource: LocalDataSource
) : Repository {


    override suspend fun getWeatherForCity(cityName: String): Result<WeatherData> {
        return remoteDataSource.getWeatherForCity(cityName)
    }

    override suspend fun getWeatherForLocation(latitude: Double, longitude: Double): Result<WeatherData> {
        return remoteDataSource.getWeatherForLocation(latitude, longitude)
    }

    override fun getLastSearchedCity(): String? {
        return localWeatherDataSource.getLastSearchedCity()
    }

    override fun saveLastSearchedCity(cityName: String) {
        localWeatherDataSource.saveLastSearchedCity(cityName)
    }
}