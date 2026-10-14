package uz.ttpu.weatherpatterns.di

import uz.ttpu.weatherpatterns.data.api.FakeWeatherApi
import uz.ttpu.weatherpatterns.data.api.WeatherApi
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository

object WeatherModule {
    val weatherApi: WeatherApi by lazy { FakeWeatherApi() }
    val weatherRepository: WeatherRepository by lazy { WeatherRepository(weatherApi) }
}
