package uz.ttpu.weatherpatterns.data.api

import kotlinx.coroutines.delay
import uz.ttpu.weatherpatterns.data.model.Weather

class FakeWeatherApi : WeatherApi {
    override suspend fun fetchWeather(city: String): Weather {
        delay(1000) // Simulate network latency
        return when (city.trim().lowercase()) {
            "tashkent" -> Weather("Tashkent", 28, "Sunny")
            "samarkand" -> Weather("Samarkand", 24, "Partly Cloudy")
            "bukhara" -> Weather("Bukhara", 31, "Clear")
            "atlantis" -> throw IllegalArgumentException("City 'Atlantis' not found on Earth!")
            else -> Weather(
                city.trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                20,
                "Mild"
            )
        }
    }
}
