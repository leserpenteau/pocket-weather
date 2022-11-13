package com.katerina.weather.weather_main_screen.domain.interactors

import com.katerina.weather.core.models.HourForecastModel
import com.katerina.weather.core.models.WeatherModel
import com.katerina.weather.core.models.WeeklyForecastModel
import com.katerina.weather.weather_main_screen.domain.repositories.WeatherRepository
import org.json.JSONObject
import javax.inject.Inject

class WeatherInteractor @Inject constructor(
    private val weatherRepository: WeatherRepository
){

    fun parseCurrentDay(jsonObject: JSONObject): WeatherModel {
        return weatherRepository.parseCurrentDay(jsonObject)
    }

    fun parseWeeklyForecast(jsonObject: JSONObject): List<WeeklyForecastModel> {
        return weatherRepository.parseWeeklyForecast(jsonObject)
    }

    fun parseHoursForecast(jsonObject: JSONObject): List<HourForecastModel> {
        return weatherRepository.parseHoursForecast(jsonObject)
    }

    fun getUrl(city: String): String {
        return weatherRepository.getUrl(city)
    }
}