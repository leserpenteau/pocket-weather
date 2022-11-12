package com.katerina.weather.weather_main_screen.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.katerina.weather.databinding.FragmentMainForecastBinding

class MainForecastFragment : Fragment() {

    private lateinit var binding: FragmentMainForecastBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainForecastBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}