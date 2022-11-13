package com.katerina.weather.di

import android.app.Application
import com.katerina.weather.weather_main_screen.ui.fragments.MainForecastFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(fragment: MainForecastFragment)
}