package com.coding.openweatherapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.openweatherapp.data.model.WeatherData
import com.coding.openweatherapp.domain.usecase.GetWeatherForCityUseCase
import com.coding.openweatherapp.domain.usecase.GetWeatherForLastSearchedCityUseCase
import com.coding.openweatherapp.domain.usecase.GetWeatherForLocationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class WeatherViewModel(
    private val getWeatherForLastSearchedCity: GetWeatherForLastSearchedCityUseCase,
    private val getWeatherForCityUseCase: GetWeatherForCityUseCase,
    private val getWeatherForLocationUseCase: GetWeatherForLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Initial)
    val uiState: StateFlow<WeatherUiState> = _uiState

    init {
        loadLastSearchedCity()
    }

    private fun loadLastSearchedCity() {
        viewModelScope.launch {
            val lastCity = getWeatherForLastSearchedCity()
            if (lastCity != null) {
                Log.d("CheckThread",Thread.currentThread().toString())
                getWeatherForCity(lastCity)
            }else{
                _uiState.value = WeatherUiState.Initial
            }
        }
    }

    fun getWeatherForCity(cityName: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            val result = getWeatherForCityUseCase(cityName)
            _uiState.value = when {
                result.isSuccess -> WeatherUiState.Success(result.getOrNull()!!)
                else -> WeatherUiState.Error("An error occurred: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun getWeatherForLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            val result = getWeatherForLocationUseCase(latitude, longitude)
            _uiState.value = when {
                result.isSuccess -> WeatherUiState.Success(result.getOrNull()!!)
                else -> WeatherUiState.Error("An error occurred: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}

sealed class WeatherUiState {
    object Initial : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val data: WeatherData) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}