package com.katerina.weather.weather_main_screen.data.repositories

import com.katerina.weather.core.models.HourForecastModel
import com.katerina.weather.core.models.WeatherModel
import com.katerina.weather.core.models.WeeklyForecastModel
import com.katerina.weather.core.utils.Constants
import com.katerina.weather.weather_main_screen.domain.repositories.WeatherRepository
import org.json.JSONObject
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(): WeatherRepository {

    override fun parseCurrentDay(jsonObject: JSONObject): WeatherModel {
        return WeatherModel(
            location = jsonObject.getJSONObject("location").getString("name"),
            temp = jsonObject.getJSONObject("current").getString("temp_c").toFloat().toInt()
                .toString(),
            condition = jsonObject.getJSONObject("current").getJSONObject("condition")
                .getString("text"),
            country = jsonObject.getJSONObject("location").getString("country")
        )
    }

    override fun parseWeeklyForecast(jsonObject: JSONObject): List<WeeklyForecastModel> {
        val forecastJson = jsonObject.getJSONObject("forecast").getJSONArray("forecastday")
        val forecastDays = ArrayList<WeeklyForecastModel>()

        for (i in 0 until forecastJson.length()) {
            val item = WeeklyForecastModel(
                date = forecastJson.getJSONObject(i).getString("date"),
                maxTemp = forecastJson.getJSONObject(i).getJSONObject("day").getString("maxtemp_c")
                    .toFloat().toInt().toString(),
                minTemp = forecastJson.getJSONObject(i).getJSONObject("day").getString("mintemp_c")
                    .toFloat().toInt().toString(),
                condition = forecastJson.getJSONObject(i).getJSONObject("day")
                    .getJSONObject("condition").getString("text")
            )
            forecastDays.add(item)
        }

        return forecastDays
    }

    override fun parseHoursForecast(jsonObject: JSONObject): List<HourForecastModel> {
        val currentDay =
            jsonObject.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0)
        val forecastHoursJson = currentDay.getJSONArray("hour")
        val forecastHours = ArrayList<HourForecastModel>()

        for (i in 0 until forecastHoursJson.length()) {
            val item = HourForecastModel(
                time = forecastHoursJson.getJSONObject(i).getString("time"),
                temp = forecastHoursJson.getJSONObject(i).getString("temp_c").toFloat().toInt()
                    .toString(),
                condition = forecastHoursJson.getJSONObject(i).getJSONObject("condition")
                    .getString("text")
            )
            forecastHours.add(item)
        }

        return forecastHours
    }

    override fun getUrl(city: String): String {
        return "${Constants.BASE_URL}${Constants.API_KEY}&q=$city&days=7&aqi=no&alerts=no"
    }
}