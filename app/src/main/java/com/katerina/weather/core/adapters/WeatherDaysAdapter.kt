package com.katerina.weather.core.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.katerina.weather.R
import com.katerina.weather.core.models.WeatherModel
import com.katerina.weather.core.models.WeeklyForecastModel
import com.katerina.weather.databinding.ItemDayBinding

class WeatherDaysAdapter() :
    ListAdapter<WeeklyForecastModel, WeatherDaysAdapter.Holder>(Comparator()) {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemDayBinding.bind(view)

        fun bind(item: WeeklyForecastModel) = with(binding) {
            val month = item.date.substring(8, 9)
            val day = item.date.substring(5, 6)
            val date = "$day.$month"

            txtDate.text = date
            txtTemp.text = "${item.maxTemp}°C/${item.minTemp}°C"

            when (item.condition) {
                "Sunny" -> { imgDay.setBackgroundResource(R.drawable.img_sunny) }
                "Clear" -> { imgDay.setBackgroundResource(R.drawable.img_sunny) }
                "Partly cloudy" -> { imgDay.setBackgroundResource(R.drawable.img_partly_cloudy) }
                "Cloudy" -> { imgDay.setBackgroundResource(R.drawable.img_cloudy) }
                "Overcast" -> { imgDay.setBackgroundResource(R.drawable.img_cloudy) }
                "Patchy rain possible" -> { imgDay.setBackgroundResource(R.drawable.img_rainy) }
                "Mist" -> { imgDay.setBackgroundResource(R.drawable.img_mist) }
                "Patchy snow possible" -> { imgDay.setBackgroundResource(R.drawable.img_snow) }
                "Patchy sleet possible" -> { imgDay.setBackgroundResource(R.drawable.img_snow) }
                "Patchy freezing drizzle possible" -> { imgDay.setBackgroundResource(R.drawable.img_snow) }
                "Blowing snow" -> { imgDay.setBackgroundResource(R.drawable.img_snow) }
                "Thundery outbreaks possible" -> { imgDay.setBackgroundResource(R.drawable.img_thunderstorm) }
                "Blizzard" -> { imgDay.setBackgroundResource(R.drawable.img_snow) }
                "Fog" -> { imgDay.setBackgroundResource(R.drawable.img_mist) }
                "Freezing fog" -> { imgDay.setBackgroundResource(R.drawable.img_mist) }
                "Patchy light drizzle" -> { imgDay.setBackgroundResource(R.drawable.img_rainy) }
                "Light drizzle" -> { imgDay.setBackgroundResource(R.drawable.img_rainy) }
                "Freezing drizzle" -> { imgDay.setBackgroundResource(R.drawable.img_rainy) }
                "Heavy freezing drizzle" -> { imgDay.setBackgroundResource(R.drawable.img_rainy) }
                "Patchy light rain" -> { imgDay.setBackgroundResource(R.drawable.img_rainy) }
                "Light rain" -> { imgDay.setBackgroundResource(R.drawable.img_rainy) }
                "Moderate rain at times" -> { imgDay.setBackgroundResource(R.drawable.img_rainy) }
                "Moderate rain" -> { imgDay.setBackgroundResource(R.drawable.img_rainy) }
                "Heavy rain at times" -> { imgDay.setBackgroundResource(R.drawable.img_rainy) }
                "Heavy rain" -> { imgDay.setBackgroundResource(R.drawable.img_rainy) }
                "Light freezing rain" -> { imgDay.setBackgroundResource(R.drawable.img_rainy) }
                "Moderate or heavy freezing rain" -> { imgDay.setBackgroundResource(R.drawable.img_rainy) }
                else -> { imgDay.setBackgroundResource(R.drawable.img_rainy) }
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<WeeklyForecastModel>() {

        override fun areItemsTheSame(oldItem: WeeklyForecastModel, newItem: WeeklyForecastModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeeklyForecastModel, newItem: WeeklyForecastModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

}