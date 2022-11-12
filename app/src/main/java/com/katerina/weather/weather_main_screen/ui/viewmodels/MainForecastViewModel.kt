package com.katerina.weather.weather_main_screen.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katerina.weather.core.models.HourForecastModel
import com.katerina.weather.core.models.WeatherModel
import com.katerina.weather.core.models.WeeklyForecastModel
import com.katerina.weather.core.utils.Constants
import com.katerina.weather.core.utils.ResponseStatus
import org.json.JSONObject

class MainForecastViewModel: ViewModel() {

    // Текущая погода
    val liveDataCurrentWeather = MutableLiveData<ResponseStatus>()

    // Погода на неделю
    val liveWeeklyForecast = MutableLiveData<ResponseStatus>()

    // Погода по часам текущего дня
    val liveHoursForecast = MutableLiveData<ResponseStatus>()

    // Создаем URL
    fun getUrl(city: String): String {
        return "${Constants.BASE_URL}${Constants.API_KEY}&q=$city&days=7&aqi=no&alerts=no"
    }

    // Получаем погоду текущего дня
    fun parseCurrentDay(jsonObject: JSONObject): WeatherModel {
        return WeatherModel(
            location = jsonObject.getJSONObject("location").getString("name"),
            temp = jsonObject.getJSONObject("current").getString("temp_c").toFloat().toInt().toString(),
            condition = jsonObject.getJSONObject("current").getJSONObject("condition")
                .getString("text")
        )
    }

    // Получаем список прогноза погоды по дням
    fun parseWeeklyForecast(jsonObject: JSONObject): List<WeeklyForecastModel> {
        val forecastJson = jsonObject.getJSONObject("forecast").getJSONArray("forecastday")
        val forecastDays = ArrayList<WeeklyForecastModel>()

        for (i in 0 until forecastJson.length()) {
            val item = WeeklyForecastModel(
                date = forecastJson.getJSONObject(i).getString("date"),
                maxTemp = forecastJson.getJSONObject(i).getJSONObject("day").getString("maxtemp_c").toFloat().toInt().toString(),
                minTemp = forecastJson.getJSONObject(i).getJSONObject("day").getString("mintemp_c").toFloat().toInt().toString(),
                condition = forecastJson.getJSONObject(i).getJSONObject("day")
                    .getJSONObject("condition").getString("text")
            )
            forecastDays.add(item)
        }

        return forecastDays
    }

    // Получаем список прогноза погоды текущего дня по часам
    fun parseHoursForecast(jsonObject: JSONObject): List<HourForecastModel> {
        val currentDay =
            jsonObject.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0)
        val forecastHoursJson = currentDay.getJSONArray("hour")
        val forecastHours = ArrayList<HourForecastModel>()

        for (i in 0 until forecastHoursJson.length()) {
            val item = HourForecastModel(
                time = forecastHoursJson.getJSONObject(i).getString("time"),
                temp = forecastHoursJson.getJSONObject(i).getString("temp_c").toFloat().toInt().toString(),
                condition = forecastHoursJson.getJSONObject(i).getJSONObject("condition")
                    .getString("text")
            )
            forecastHours.add(item)
        }

        return forecastHours
    }

}