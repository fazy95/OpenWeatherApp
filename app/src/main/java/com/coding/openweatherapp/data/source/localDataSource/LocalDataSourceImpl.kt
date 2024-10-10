package com.coding.openweatherapp.data.source.localDataSource

import android.content.Context

class LocalDataSourceImpl(context: Context): LocalDataSource {

    private val sharedPreferences = context.getSharedPreferences("WeatherPrefs", Context.MODE_PRIVATE)

    override fun getLastSearchedCity(): String? {
        return sharedPreferences.getString("lastSearchedCity", null)
    }

    override fun saveLastSearchedCity(cityName: String) {
        sharedPreferences.edit().putString("lastSearchedCity", cityName).apply()
    }
}