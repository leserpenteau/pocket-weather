package com.katerina.weather.weather_main_screen.domain.repositories

import com.katerina.weather.core.models.HourForecastModel
import com.katerina.weather.core.models.WeatherModel
import com.katerina.weather.core.models.WeeklyForecastModel
import org.json.JSONObject

interface WeatherRepository {
    fun parseCurrentDay(jsonObject: JSONObject): WeatherModel
    fun parseWeeklyForecast(jsonObject: JSONObject): List<WeeklyForecastModel>
    fun parseHoursForecast(jsonObject: JSONObject): List<HourForecastModel>
    fun getUrl(city: String): String
}