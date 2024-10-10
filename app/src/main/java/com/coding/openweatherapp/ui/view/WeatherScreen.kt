package com.coding.openweatherapp.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.coding.openweatherapp.data.model.WeatherData
import com.coding.openweatherapp.ui.viewmodel.WeatherUiState
import com.coding.openweatherapp.ui.viewmodel.WeatherViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var cityName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text("Enter city name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { viewModel.getWeatherForCity(cityName) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Search")
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (val state = uiState) {
            is WeatherUiState.Loading -> CircularProgressIndicator()
            is WeatherUiState.Success -> WeatherInfo(state.data)
            is WeatherUiState.Error -> Text(
                text = state.message,
                color = MaterialTheme.colorScheme.error
            )
            is WeatherUiState.Initial -> Text("Enter a city name to search for weather")
        }
    }
}


@Composable
fun WeatherInfo(data: WeatherData) {

    Column {
        Text(
            text = data.name,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "${data.main.temp}°C",
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = data.weather.firstOrNull()?.description ?: "",
            style = MaterialTheme.typography.bodyLarge
        )
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${data.weather.firstOrNull()?.icon}@2x.png",
            contentDescription = "Weather icon",
            modifier = Modifier.size(100.dp)
        )
    }
}