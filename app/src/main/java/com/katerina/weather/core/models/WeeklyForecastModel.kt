package com.katerina.weather.core.models

data class WeeklyForecastModel (
    val date: String,
    val maxTemp: String,
    val minTemp: String,
    val condition: String
)