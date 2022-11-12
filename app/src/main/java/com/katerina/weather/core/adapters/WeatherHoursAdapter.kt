package com.katerina.weather.core.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.katerina.weather.R
import com.katerina.weather.core.models.HourForecastModel
import com.katerina.weather.databinding.ItemHourBinding

class WeatherHoursAdapter() :
    ListAdapter<HourForecastModel, WeatherHoursAdapter.Holder>(Comparator()) {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemHourBinding.bind(view)

        fun bind(item: HourForecastModel) = with(binding) {
            txtHour.text = item.time.substringAfter(" ")

            when (item.condition) {
                "Sunny" -> { imgHour.setBackgroundResource(R.drawable.img_sunny) }
                "Clear" -> { imgHour.setBackgroundResource(R.drawable.img_sunny) }
                "Partly cloudy" -> { imgHour.setBackgroundResource(R.drawable.img_partly_cloudy) }
                "Cloudy" -> { imgHour.setBackgroundResource(R.drawable.img_cloudy) }
                "Overcast" -> { imgHour.setBackgroundResource(R.drawable.img_cloudy) }
                "Patchy rain possible" -> { imgHour.setBackgroundResource(R.drawable.img_rainy) }
                "Mist" -> { imgHour.setBackgroundResource(R.drawable.img_mist) }
                "Patchy snow possible" -> { imgHour.setBackgroundResource(R.drawable.img_snow) }
                "Patchy sleet possible" -> { imgHour.setBackgroundResource(R.drawable.img_snow) }
                "Patchy freezing drizzle possible" -> { imgHour.setBackgroundResource(R.drawable.img_snow) }
                "Blowing snow" -> { imgHour.setBackgroundResource(R.drawable.img_snow) }
                "Thundery outbreaks possible" -> { imgHour.setBackgroundResource(R.drawable.img_thunderstorm) }
                "Blizzard" -> { imgHour.setBackgroundResource(R.drawable.img_snow) }
                "Fog" -> { imgHour.setBackgroundResource(R.drawable.img_mist) }
                "Freezing fog" -> { imgHour.setBackgroundResource(R.drawable.img_mist) }
                "Patchy light drizzle" -> { imgHour.setBackgroundResource(R.drawable.img_rainy) }
                "Light drizzle" -> { imgHour.setBackgroundResource(R.drawable.img_rainy) }
                "Freezing drizzle" -> { imgHour.setBackgroundResource(R.drawable.img_rainy) }
                "Heavy freezing drizzle" -> { imgHour.setBackgroundResource(R.drawable.img_rainy) }
                "Patchy light rain" -> { imgHour.setBackgroundResource(R.drawable.img_rainy) }
                "Light rain" -> { imgHour.setBackgroundResource(R.drawable.img_rainy) }
                "Moderate rain at times" -> { imgHour.setBackgroundResource(R.drawable.img_rainy) }
                "Moderate rain" -> { imgHour.setBackgroundResource(R.drawable.img_rainy) }
                "Heavy rain at times" -> { imgHour.setBackgroundResource(R.drawable.img_rainy) }
                "Heavy rain" -> { imgHour.setBackgroundResource(R.drawable.img_rainy) }
                "Light freezing rain" -> { imgHour.setBackgroundResource(R.drawable.img_rainy) }
                "Moderate or heavy freezing rain" -> { imgHour.setBackgroundResource(R.drawable.img_rainy) }
                else -> { imgHour.setBackgroundResource(R.drawable.img_rainy) }
            }

            txtHourTemperature.text = "${item.temp.toFloat().toInt().toString()} C"
        }
    }

    class Comparator : DiffUtil.ItemCallback<HourForecastModel>() {

        override fun areItemsTheSame(oldItem: HourForecastModel, newItem: HourForecastModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: HourForecastModel, newItem: HourForecastModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hour, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))

    }

}