package uz.ttpu.weatherpatterns.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository
import uz.ttpu.weatherpatterns.mvi.model.WeatherIntent
import uz.ttpu.weatherpatterns.mvi.model.WeatherState

class WeatherMviViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state.asStateFlow()

    fun processIntent(intent: WeatherIntent) {
        when (intent) {
            is WeatherIntent.LoadWeather -> fetchWeather(intent.city)
            is WeatherIntent.ClearError -> _state.update { it.copy(error = null) }
        }
    }

    private fun fetchWeather(city: String) {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = repository.getWeather(city)
            result.fold(
                onSuccess = { weather ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            weather = weather,
                            error = null
                        )
                    }
                },
                onFailure = { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            weather = null,
                            error = throwable.message ?: "Unknown error occurred"
                        )
                    }
                }
            )
        }
    }

    class Factory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WeatherMviViewModel(repository) as T
        }
    }
}
