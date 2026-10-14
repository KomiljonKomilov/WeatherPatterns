package uz.ttpu.weatherpatterns.mvvm.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.ttpu.weatherpatterns.di.WeatherModule
import uz.ttpu.weatherpatterns.mvvm.viewmodel.WeatherUiState
import uz.ttpu.weatherpatterns.mvvm.viewmodel.WeatherViewModel
import uz.ttpu.weatherpatterns.ui.WeatherScreen
import uz.ttpu.weatherpatterns.ui.theme.WeatherPatternsTheme

class MvvmActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModel.Factory(WeatherModule.weatherRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherPatternsTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                WeatherScreen(
                    patternTitle = "MVVM Pattern",
                    isLoading = uiState is WeatherUiState.Loading,
                    weather = (uiState as? WeatherUiState.Success)?.weather,
                    errorMessage = (uiState as? WeatherUiState.Error)?.message,
                    onFetchWeather = { city -> viewModel.fetchWeather(city) },
                    onSwitchPattern = { targetActivityClass ->
                        if (targetActivityClass != MvvmActivity::class.java) {
                            startActivity(Intent(this@MvvmActivity, targetActivityClass))
                            finish()
                        }
                    }
                )
            }
        }
    }
}
