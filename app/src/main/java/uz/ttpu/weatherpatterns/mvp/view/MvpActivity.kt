package uz.ttpu.weatherpatterns.mvp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import uz.ttpu.weatherpatterns.data.model.Weather
import uz.ttpu.weatherpatterns.di.WeatherModule
import uz.ttpu.weatherpatterns.mvp.WeatherContract
import uz.ttpu.weatherpatterns.mvp.presenter.WeatherPresenter
import uz.ttpu.weatherpatterns.ui.WeatherScreen
import uz.ttpu.weatherpatterns.ui.theme.WeatherPatternsTheme

class MvpActivity : ComponentActivity(), WeatherContract.View {

    private lateinit var presenter: WeatherPresenter

    private var isLoading by mutableStateOf(false)
    private var weather by mutableStateOf<Weather?>(null)
    private var errorMessage by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = WeatherPresenter(WeatherModule.weatherRepository)
        presenter.attachView(this)

        setContent {
            WeatherPatternsTheme {
                WeatherScreen(
                    patternTitle = "MVP Pattern",
                    isLoading = isLoading,
                    weather = weather,
                    errorMessage = errorMessage,
                    onFetchWeather = { city -> presenter.fetchWeather(city) }
                )
            }
        }
    }

    override fun showLoading() {
        isLoading = true
        weather = null
        errorMessage = null
    }

    override fun showWeather(weather: Weather) {
        this.isLoading = false
        this.weather = weather
        this.errorMessage = null
    }

    override fun showError(message: String) {
        this.isLoading = false
        this.weather = null
        this.errorMessage = message
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}
