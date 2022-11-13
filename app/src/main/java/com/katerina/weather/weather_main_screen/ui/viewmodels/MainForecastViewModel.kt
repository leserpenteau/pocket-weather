package com.katerina.weather.weather_main_screen.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katerina.weather.core.models.HourForecastModel
import com.katerina.weather.core.models.WeatherModel
import com.katerina.weather.core.models.WeeklyForecastModel
import com.katerina.weather.core.utils.ResponseStatus
import com.katerina.weather.weather_main_screen.domain.interactors.WeatherInteractor
import org.json.JSONObject
import javax.inject.Inject

class MainForecastViewModel @Inject constructor(
    private val weatherInteractor: WeatherInteractor
): ViewModel() {

    // Текущая погода
    val liveDataCurrentWeather = MutableLiveData<ResponseStatus>()

    // Погода на неделю
    val liveWeeklyForecast = MutableLiveData<ResponseStatus>()

    // Погода по часам текущего дня
    val liveHoursForecast = MutableLiveData<ResponseStatus>()

    fun getUrl(city: String): String {
        return weatherInteractor.getUrl(city)
    }

    fun parseCurrentDay(jsonObject: JSONObject): WeatherModel {
        return weatherInteractor.parseCurrentDay(jsonObject)
    }

    fun parseWeeklyForecast(jsonObject: JSONObject): List<WeeklyForecastModel> {
        return weatherInteractor.parseWeeklyForecast(jsonObject)
    }

    fun parseHoursForecast(jsonObject: JSONObject): List<HourForecastModel> {
        return weatherInteractor.parseHoursForecast(jsonObject)
    }
}