package uz.ttpu.weatherpatterns.mvc.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import uz.ttpu.weatherpatterns.di.WeatherModule
import uz.ttpu.weatherpatterns.mvc.controller.WeatherController
import uz.ttpu.weatherpatterns.mvc.model.WeatherModel
import uz.ttpu.weatherpatterns.mvc.model.WeatherUiState
import uz.ttpu.weatherpatterns.ui.WeatherScreen
import uz.ttpu.weatherpatterns.ui.theme.WeatherPatternsTheme

class MvcActivity : ComponentActivity(), WeatherView {
    private lateinit var controller: WeatherController
    private var uiState by mutableStateOf<WeatherUiState>(WeatherUiState.Idle)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model = WeatherModel(WeatherModule.weatherRepository)
        controller = WeatherController(model, this, lifecycleScope)

        setContent {
            WeatherPatternsTheme {
                val state = uiState
                WeatherScreen(
                    patternTitle = "MVC Pattern",
                    isLoading = state is WeatherUiState.Loading,
                    weather = (state as? WeatherUiState.Success)?.weather,
                    errorMessage = (state as? WeatherUiState.Error)?.message,
                    onFetchWeather = { city -> controller.fetchWeather(city) }
                )
            }
        }
    }

    override fun render(state: WeatherUiState) {
        uiState = state
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.detachView()
    }
}
