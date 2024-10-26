package com.coding.openweatherapp.usecase

import com.coding.openweatherapp.data.model.WeatherData
import com.coding.openweatherapp.domain.repository.WeatherRepository
import com.coding.openweatherapp.domain.usecase.GetWeatherForCityUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetWeatherForCityUseCaseTest {
    @Mock
    private lateinit var weatherRepository: WeatherRepository

    private lateinit var getWeatherForCityUseCase: GetWeatherForCityUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getWeatherForCityUseCase = GetWeatherForCityUseCase(weatherRepository)
    }

    @Test
    fun `invoke returns success result when repository returns weather data`() = runBlocking {
        // Given
        val cityName = "New York"
        val weatherData = WeatherData(/* ... */)
        `when`(weatherRepository.getWeatherForCity(cityName)).thenReturn(Result.success(weatherData))

        // When
        val result = getWeatherForCityUseCase(cityName)

        // Then
        assert(result.isSuccess)
        assert(result.getOrNull() == weatherData)
    }

    @Test
    fun `invoke returns failure result when repository throws exception`() = runBlocking {
        // Given
        val cityName = "Invalid City"
        val exception = Exception("City not found")
        `when`(weatherRepository.getWeatherForCity(cityName)).thenReturn(Result.failure(exception))

        // When
        val result = getWeatherForCityUseCase(cityName)

        // Then
        assert(result.isFailure)
        assert(result.exceptionOrNull() == exception)
    }
}