package com.coding.openweatherapp


import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.coding.openweatherapp.ui.theme.OpenWeatherAppTheme
import com.coding.openweatherapp.ui.view.WeatherScreen
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : ComponentActivity() {

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinAndroidContext {
                OpenWeatherAppTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val configuration = LocalConfiguration.current
                        val scrollState = rememberScrollState()

                        when (configuration.orientation) {
                            Configuration.ORIENTATION_LANDSCAPE -> {
                                WeatherScreen(modifier = Modifier.verticalScroll(scrollState))
                            }
                            Configuration.ORIENTATION_PORTRAIT -> {
                                WeatherScreen(modifier = Modifier)
                            }
                            else -> {
                                Text("Undefined Orientation")
                            }
                        }
                    }
                }
            }
        }
    }
}