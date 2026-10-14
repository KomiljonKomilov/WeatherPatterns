package uz.ttpu.weatherpatterns.mvc.view

import uz.ttpu.weatherpatterns.mvc.model.WeatherUiState

interface WeatherView {
    fun render(state: WeatherUiState)
}
