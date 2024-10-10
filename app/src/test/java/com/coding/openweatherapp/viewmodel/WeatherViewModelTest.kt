package com.coding.openweatherapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.coding.openweatherapp.data.model.WeatherData
import com.coding.openweatherapp.domain.repository.Repository
import com.coding.openweatherapp.domain.usecase.GetWeatherForCityUseCase
import com.coding.openweatherapp.domain.usecase.GetWeatherForLastSearchedCityUseCase
import com.coding.openweatherapp.domain.usecase.GetWeatherForLocationUseCase
import com.coding.openweatherapp.ui.viewmodel.WeatherUiState
import com.coding.openweatherapp.ui.viewmodel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class WeatherViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Mock
    private lateinit var weatherRepository: Repository

    private lateinit var viewModel: WeatherViewModel

    private lateinit var getWeatherForCityUseCase: GetWeatherForCityUseCase

    private lateinit var getWeatherForLocationUseCase: GetWeatherForLocationUseCase

    private lateinit var getWeatherForLastSearchedCity: GetWeatherForLastSearchedCityUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        getWeatherForCityUseCase = GetWeatherForCityUseCase(weatherRepository)
        getWeatherForLocationUseCase = GetWeatherForLocationUseCase(weatherRepository)
        getWeatherForLastSearchedCity = GetWeatherForLastSearchedCityUseCase(weatherRepository)
        viewModel = WeatherViewModel(getWeatherForLastSearchedCity, getWeatherForCityUseCase, getWeatherForLocationUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getWeatherForCity success`() = testScope.runTest {
        // Given
        val cityName = "New York"
        val weatherData = WeatherData(/* ... */)
        `when`(weatherRepository.getWeatherForCity(cityName)).thenReturn(Result.success(weatherData))

        // When
        viewModel.getWeatherForCity(cityName)

        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.first()
        assert(uiState is WeatherUiState.Success)
        assert((uiState as WeatherUiState.Success).data == weatherData)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getWeatherForCity error`() = testScope.runTest {
        // Given
        val cityName = "Invalid City"
        `when`(weatherRepository.getWeatherForCity(cityName)).thenReturn(Result.failure(Exception("City not found")))

        // When
        viewModel.getWeatherForCity(cityName)

        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.first()
        assert(uiState is WeatherUiState.Error)
        assert((uiState as WeatherUiState.Error).message.contains("City not found"))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getWeatherForLocation success`() = testScope.runTest {
        // Given
        val latitude = 40.7128
        val longitude = -74.0060
        val weatherData = WeatherData(/* ... */)
        `when`(weatherRepository.getWeatherForLocation(latitude, longitude)).thenReturn(Result.success(weatherData))

        // When
        viewModel.getWeatherForLocation(latitude, longitude)

        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.first()
        assert(uiState is WeatherUiState.Success)
        assert((uiState as WeatherUiState.Success).data == weatherData)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getWeatherForLocation error`() = testScope.runTest {
        // Given
        val latitude = 0.0
        val longitude = 0.0
        `when`(weatherRepository.getWeatherForLocation(latitude, longitude)).thenReturn(Result.failure(Exception("Invalid location")))

        // When
        viewModel.getWeatherForLocation(latitude, longitude)

        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.first()
        assert(uiState is WeatherUiState.Error)
        assert((uiState as WeatherUiState.Error).message.contains("Invalid location"))
    }
}