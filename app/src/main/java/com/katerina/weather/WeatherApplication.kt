package com.katerina.weather

import android.app.Application
import com.katerina.weather.di.AppComponent
import com.katerina.weather.di.DaggerAppComponent

class WeatherApplication: Application() {

    val appComponent: AppComponent = DaggerAppComponent.builder().application(this).build()

}