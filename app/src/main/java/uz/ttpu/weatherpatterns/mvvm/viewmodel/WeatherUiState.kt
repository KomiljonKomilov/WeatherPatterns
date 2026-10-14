package uz.ttpu.weatherpatterns.mvvm.viewmodel

import uz.ttpu.weatherpatterns.data.model.Weather

sealed interface WeatherUiState {
    object Idle : WeatherUiState
    object Loading : WeatherUiState
    data class Success(val weather: Weather) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}
