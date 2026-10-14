package uz.ttpu.weatherpatterns.mvp.presenter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository
import uz.ttpu.weatherpatterns.mvp.WeatherContract

class WeatherPresenter(
    private val repository: WeatherRepository
) : WeatherContract.Presenter {

    private var view: WeatherContract.View? = null
    private val presenterJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + presenterJob)

    override fun attachView(view: WeatherContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun fetchWeather(city: String) {
        view?.showLoading()
        coroutineScope.launch {
            val result = repository.getWeather(city)
            result.fold(
                onSuccess = { weather ->
                    view?.showWeather(weather)
                },
                onFailure = { error ->
                    view?.showError(error.message ?: "Unknown error occurred")
                }
            )
        }
    }

    override fun destroy() {
        detachView()
        coroutineScope.cancel()
    }
}
