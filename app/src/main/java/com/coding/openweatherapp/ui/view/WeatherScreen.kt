package com.coding.openweatherapp.ui.view

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.coding.openweatherapp.data.model.WeatherData
import com.coding.openweatherapp.ui.viewmodel.WeatherUiState
import com.coding.openweatherapp.ui.viewmodel.WeatherViewModel
import com.coding.openweatherapp.utils.formatDecimalOnePlace
import com.coding.openweatherapp.utils.getColorForWeatherCondition
import org.koin.androidx.compose.koinViewModel


@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = koinViewModel(),
    modifier: Modifier

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var cityName by remember { mutableStateOf("") }
    var isCelsius by remember { mutableStateOf(true) }
    val backgroundColor = remember { mutableStateOf(Color.White) }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        viewModel.onLocationPermissionResult(isGranted)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor.value)
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
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { isCelsius = !isCelsius },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Convert")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Get current location")
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (val state = uiState) {
            is WeatherUiState.Loading -> CircularProgressIndicator()
            is WeatherUiState.Success -> {WeatherInfo(data = state.data, isCelsius = isCelsius, viewModel)
                backgroundColor.value = getColorForWeatherCondition(state.data.weather.firstOrNull()?.description ?: "")}
            is WeatherUiState.Error -> Text(
                text = state.message,
                color = MaterialTheme.colorScheme.error
            )
            is WeatherUiState.Initial -> Text("Enter a city name to search for weather")
        }
    }
}


@Composable
fun WeatherInfo(data: WeatherData, isCelsius: Boolean, viewModel: WeatherViewModel) {
    Column {
        Text(
            text = data.name,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = if (isCelsius) {"${data.main.temp.formatDecimalOnePlace()}°C"}
                    else "${viewModel.convertTemperature(data.main.temp).formatDecimalOnePlace()}°F",
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