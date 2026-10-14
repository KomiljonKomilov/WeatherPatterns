package uz.ttpu.weatherpatterns.mvp

import uz.ttpu.weatherpatterns.data.model.Weather

interface WeatherContract {
    interface View {
        fun showLoading()
        fun showWeather(weather: Weather)
        fun showError(message: String)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun fetchWeather(city: String)
        fun destroy()
    }
}
