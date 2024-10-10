package com.coding.openweatherapp.data.source.localDataSource

interface LocalDataSource {
    fun getLastSearchedCity(): String?
    fun saveLastSearchedCity(cityName: String)
}