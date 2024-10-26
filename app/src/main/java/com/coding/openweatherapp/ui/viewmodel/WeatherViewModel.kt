package com.coding.openweatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.openweatherapp.data.model.WeatherData
import com.coding.openweatherapp.domain.usecase.GetCheckPermissionUseCase
import com.coding.openweatherapp.domain.usecase.GetConvertWeatherTemperatureUseCase
import com.coding.openweatherapp.domain.usecase.GetCurrentLocationUseCase
import com.coding.openweatherapp.domain.usecase.GetWeatherForCityUseCase
import com.coding.openweatherapp.domain.usecase.GetWeatherForLastSearchedCityUseCase
import com.coding.openweatherapp.domain.usecase.GetWeatherForLocationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class WeatherViewModel(
    private val getWeatherForLastSearchedCity: GetWeatherForLastSearchedCityUseCase,
    private val getWeatherForCityUseCase: GetWeatherForCityUseCase,
    private val getWeatherForLocationUseCase: GetWeatherForLocationUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getCheckPermissionUseCase: GetCheckPermissionUseCase,
    private val getConvertWeatherTemperatureUseCase: GetConvertWeatherTemperatureUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Initial)
    val uiState: StateFlow<WeatherUiState> = _uiState

    init {
        loadLastSearchedCity()
    }

    fun loadLastSearchedCity() {
        if (getCheckPermissionUseCase.invoke()) {
            getWeatherForCurrentLocation()
        } else {
            viewModelScope.launch {
                val lastCity = getWeatherForLastSearchedCity()
                if (lastCity != null) {
                    if (lastCity.isNotEmpty()) {
                        getWeatherForCity(lastCity)
                    }
                } else {
                    _uiState.value = WeatherUiState.Initial
                }
            }
        }
    }

    fun onLocationPermissionResult(isGranted: Boolean) {
        if (isGranted) {

            getWeatherForCurrentLocation()
        } else {
            _uiState.value = WeatherUiState.Error("Location permission denied")
        }
    }

    private fun getWeatherForCurrentLocation() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            when {
                getCheckPermissionUseCase.invoke() -> {
                    fetchWeatherForCurrentLocation()
                }

                else -> {
                    _uiState.value = WeatherUiState.Error("Location permission denied")
                }
            }
        }
    }

    private suspend fun fetchWeatherForCurrentLocation() {
        getCurrentLocationUseCase().onSuccess { location ->
            getWeatherForLocation(location.latitude, location.longitude)
        }.onFailure { exception ->
            _uiState.value =
                WeatherUiState.Error("Failed to get current location: ${exception.message}")
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

    fun convertTemperature(celsius: Double): Double {
        return getConvertWeatherTemperatureUseCase(celsius)
    }
}

sealed class WeatherUiState {
    data object Initial : WeatherUiState()
    data object Loading : WeatherUiState()
    data class Success(val data: WeatherData) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}