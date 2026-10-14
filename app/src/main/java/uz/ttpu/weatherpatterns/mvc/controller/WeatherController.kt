package uz.ttpu.weatherpatterns.mvc.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.ttpu.weatherpatterns.mvc.model.WeatherModel
import uz.ttpu.weatherpatterns.mvc.model.WeatherUiState
import uz.ttpu.weatherpatterns.mvc.view.WeatherView

class WeatherController(
    private val model: WeatherModel,
    private var view: WeatherView?,
    private val coroutineScope: CoroutineScope
) {
    fun attachView(view: WeatherView) {
        this.view = view
        view.render(model.currentState)
    }

    fun detachView() {
        this.view = null
    }

    fun fetchWeather(city: String) {
        view?.render(WeatherUiState.Loading)
        coroutineScope.launch(Dispatchers.Main) {
            val state = model.fetchWeather(city)
            view?.render(state)
        }
    }
}
