package com.katerina.weather.core.utils

import com.katerina.weather.core.models.HourForecastModel
import com.katerina.weather.core.models.WeatherModel
import com.katerina.weather.core.models.WeeklyForecastModel

sealed class ResponseStatus {
    class Loading() : ResponseStatus()
    class SuccessHoursForecast(val list: List<HourForecastModel>) : ResponseStatus()
    class SuccessWeeklyForecast(val list: List<WeeklyForecastModel>) : ResponseStatus()
    class SuccessWeather(val item: WeatherModel) : ResponseStatus()
    class Error(val throwable: Throwable) : ResponseStatus()
}