package com.katerina.weather.di

import com.katerina.weather.weather_main_screen.data.repositories.WeatherRepositoryImpl
import com.katerina.weather.weather_main_screen.domain.repositories.WeatherRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoriesModule {

    @Provides
    fun provideWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl)
            : WeatherRepository = weatherRepositoryImpl
}