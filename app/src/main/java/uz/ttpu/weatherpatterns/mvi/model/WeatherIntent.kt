package uz.ttpu.weatherpatterns.mvi.model

sealed interface WeatherIntent {
    data class LoadWeather(val city: String) : WeatherIntent
    object ClearError : WeatherIntent
}
