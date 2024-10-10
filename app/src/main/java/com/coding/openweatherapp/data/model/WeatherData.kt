package com.coding.openweatherapp.data.model


data class WeatherData(
    var name: String = "",
    var main: Main = Main(),
    var weather: List<Weather> = emptyList()
) {
    data class Main(
        var temp: Double = 0.0
    )

    data class Weather(
        var description: String = "",
        var icon: String = ""
    )
}