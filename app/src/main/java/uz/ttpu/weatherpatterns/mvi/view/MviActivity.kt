package uz.ttpu.weatherpatterns.mvi.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.ttpu.weatherpatterns.di.WeatherModule
import uz.ttpu.weatherpatterns.mvi.model.WeatherIntent
import uz.ttpu.weatherpatterns.mvi.model.WeatherState
import uz.ttpu.weatherpatterns.mvi.viewmodel.WeatherMviViewModel
import uz.ttpu.weatherpatterns.ui.WeatherScreen
import uz.ttpu.weatherpatterns.ui.theme.WeatherPatternsTheme

fun WeatherState.toStatusText(): String {
    return when {
        isLoading -> "Status: Fetching latest weather data..."
        error != null -> "Status: Error - $error"
        weather != null -> "Status: Loaded data for ${weather.city} (${weather.condition})"
        else -> "Status: Ready for input"
    }
}

class MviActivity : ComponentActivity() {

    private val viewModel: WeatherMviViewModel by viewModels {
        WeatherMviViewModel.Factory(WeatherModule.weatherRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherPatternsTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()

                Column(modifier = Modifier.fillMaxSize()) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = state.toStatusText(),
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    WeatherScreen(
                        patternTitle = "MVI Pattern",
                        isLoading = state.isLoading,
                        weather = state.weather,
                        errorMessage = state.error,
                        onFetchWeather = { city ->
                            viewModel.processIntent(WeatherIntent.LoadWeather(city))
                        },
                        onSwitchPattern = { targetActivityClass ->
                            if (targetActivityClass != MviActivity::class.java) {
                                startActivity(Intent(this@MviActivity, targetActivityClass))
                                finish()
                            }
                        }
                    )
                }
            }
        }
    }
}
