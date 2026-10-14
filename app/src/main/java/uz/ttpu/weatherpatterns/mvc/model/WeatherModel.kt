package uz.ttpu.weatherpatterns.mvc.model

import uz.ttpu.weatherpatterns.data.model.Weather
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository

sealed interface WeatherUiState {
    object Idle : WeatherUiState
    object Loading : WeatherUiState
    data class Success(val weather: Weather) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}

class WeatherModel(private val repository: WeatherRepository) {
    var currentState: WeatherUiState = WeatherUiState.Idle
        private set

    suspend fun fetchWeather(city: String): WeatherUiState {
        currentState = WeatherUiState.Loading
        val result = repository.getWeather(city)
        currentState = result.fold(
            onSuccess = { WeatherUiState.Success(it) },
            onFailure = { WeatherUiState.Error(it.message ?: "Unknown error occurred") }
        )
        return currentState
    }
}
