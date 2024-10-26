package com.coding.openweatherapp.utils

import androidx.compose.ui.graphics.Color

fun Double.formatDecimalOnePlace(): String = String.format("%.1f", this)

fun getColorForWeatherCondition(condition: String): Color {
    return when (condition.lowercase()) {
        "clear", "clear sky" -> Color(0xFF00BFFF) // Deep Sky Blue
        "few clouds" -> Color(0xFFF0E68C) // Khaki
        "scattered clouds" -> Color(0xFFB0C4DE) // Light Steel Blue
        "broken clouds" -> Color(0xFFA9A9A9) // Dark Gray
        "shower rain" -> Color(0xFF1E90FF) // Dodger Blue
        "rain" -> Color(0xFF4682B4) // Steel Blue
        "thunderstorm" -> Color(0xFFFF4500) // Orange Red
        "snow" -> Color(0xFFFFFFFF) // White
        "mist" -> Color(0xFFD3D3D3) // Light Gray
        else -> Color.Black // Default to black for undefined conditions
    }
}