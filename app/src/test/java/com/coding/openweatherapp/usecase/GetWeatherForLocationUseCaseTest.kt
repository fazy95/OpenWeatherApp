package com.coding.openweatherapp.usecase

import com.coding.openweatherapp.data.model.WeatherData
import com.coding.openweatherapp.domain.repository.Repository
import com.coding.openweatherapp.domain.usecase.GetWeatherForLocationUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetWeatherForLocationUseCaseTest {
    @Mock
    private lateinit var weatherRepository: Repository

    private lateinit var getWeatherForLocationUseCase: GetWeatherForLocationUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getWeatherForLocationUseCase = GetWeatherForLocationUseCase(weatherRepository)
    }

    @Test
    fun `invoke returns success result when repository returns weather data`() = runBlocking {
        // Given
        val latitude = 40.7128
        val longitude = -74.0060
        val weatherData = WeatherData(/* ... */)
        `when`(weatherRepository.getWeatherForLocation(latitude, longitude)).thenReturn(Result.success(weatherData))

        // When
        val result = getWeatherForLocationUseCase(latitude, longitude)

        // Then
        assert(result.isSuccess)
        assert(result.getOrNull() == weatherData)
    }

    @Test
    fun `invoke returns failure result when repository throws exception`() = runBlocking {
        // Given
        val latitude = 0.0
        val longitude = 0.0
        val exception = Exception("Invalid location")
        `when`(weatherRepository.getWeatherForLocation(latitude, longitude)).thenReturn(Result.failure(exception))

        // When
        val result = getWeatherForLocationUseCase(latitude, longitude)

        // Then
        assert(result.isFailure)
        assert(result.exceptionOrNull() == exception)
    }
}