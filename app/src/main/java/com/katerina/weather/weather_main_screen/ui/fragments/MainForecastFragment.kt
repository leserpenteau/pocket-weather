package com.katerina.weather.weather_main_screen.ui.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.snackbar.Snackbar
import com.katerina.weather.WeatherApplication
import com.katerina.weather.core.adapters.WeatherDaysAdapter
import com.katerina.weather.core.adapters.WeatherHoursAdapter
import com.katerina.weather.core.api.NetworkService
import com.katerina.weather.core.models.HourForecastModel
import com.katerina.weather.core.models.WeatherModel
import com.katerina.weather.core.models.WeeklyForecastModel
import com.katerina.weather.core.utils.*
import com.katerina.weather.databinding.FragmentMainForecastBinding
import com.katerina.weather.weather_main_screen.ui.viewmodels.MainForecastViewModel
import org.json.JSONObject
import javax.inject.Inject

class MainForecastFragment : Fragment() {

    private lateinit var binding: FragmentMainForecastBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<MainForecastViewModel>
    lateinit var viewModel: MainForecastViewModel

    private lateinit var hoursAdapter: WeatherHoursAdapter
    private lateinit var daysAdapter: WeatherDaysAdapter

    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var fLocationClient: FusedLocationProviderClient

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as WeatherApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainForecastBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModelFactory.getViewModel(this)

        showCurrentProgressBar()
        showHoursProgressBar()
        showDaysProgressBar()

        fLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        checkPermission()
        checkGps()

        updateCurrentWeather()
        initRecyclers()
        updateHoursForecast()
        updateDaysForecast()

        binding.btnRefresh.setOnClickListener {
            showCurrentProgressBar()
            showHoursProgressBar()
            showDaysProgressBar()
            checkGps()
        }

        binding.btnSearch.setOnClickListener {
            DialogManager.searchCityWeatherDialog(
                requireContext(),
                object : DialogManager.Listener {
                    override fun onClick(name: String?) {
                        name?.let { name ->
                            val url = viewModel.getUrl(name)
                            NetworkService.getInstance(requireContext())
                                .addToRequestQueue(createRequest(url))
                        }
                    }
                })
        }
    }

    override fun onResume() {
        super.onResume()
        checkGps()
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            pLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {
                if (!it) Snackbar.make(
                    binding.root,
                    "You deny a permission. Weather forecast is not available",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun checkGps() {
        if (isLocationEnabled()) {
            val ct = CancellationTokenSource()
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            fLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
                .addOnCompleteListener {
                    val url = viewModel.getUrl("${it.result.latitude},${it.result.longitude}")
                    NetworkService.getInstance(requireContext())
                        .addToRequestQueue(createRequest(url))
                }
        } else {
            DialogManager.locationSettingsDialog(requireContext(), object : DialogManager.Listener {
                override fun onClick(name: String?) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
        }
    }

    private fun isLocationEnabled(): Boolean {
        val lm = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun createRequest(url: String): JsonObjectRequest {
        return JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                val currentWeather = parseCurrentDay(response)
                val hoursWeather = parseHoursForecast(response)
                val weeklyWeather = parseWeeklyForecast(response)

                viewModel.liveHoursForecast.value =
                    ResponseStatus.SuccessHoursForecast(hoursWeather)
                viewModel.liveDataCurrentWeather.value =
                    ResponseStatus.SuccessWeather(currentWeather)
                viewModel.liveWeeklyForecast.value =
                    ResponseStatus.SuccessWeeklyForecast(weeklyWeather)
            },
            {
                if (!NetworkUtils.isOnline(requireContext())) {
                    Snackbar.make(
                        binding.root,
                        "No internet connection.",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    Snackbar.make(
                        binding.root,
                        "No matching location found. Please try another one.",
                        Snackbar.LENGTH_LONG
                    ).setAction("Retry") {
                        DialogManager.searchCityWeatherDialog(
                            requireContext(),
                            object : DialogManager.Listener {
                                override fun onClick(name: String?) {
                                    name?.let { name ->
                                        val url = viewModel.getUrl(name)
                                        NetworkService.getInstance(requireContext())
                                            .addToRequestQueue(createRequest(url))
                                    }
                                }
                            })
                    }.show()
                }
            }
        )
    }

    private fun updateCurrentWeather() = with(binding) {
        viewModel.liveDataCurrentWeather.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseStatus.Loading -> {
                    showCurrentProgressBar()
                }

                is ResponseStatus.SuccessWeather -> {
                    hideCurrentProgressBar()
                    txtCurrentLocation.text = it.item.location
                    txtCurrentTemp.text = "${it.item.temp}??C"
                    txtCondition.text = it.item.condition
                    txtCurrentLocationCountry.text = it.item.country
                }

                is ResponseStatus.Error -> {
                    showCurrentProgressBar()
                    Log.d(
                        "MyLog",
                        "Something is wrong with updateCurrentWeather: ${it.throwable}"
                    )
                }
                else -> {}
            }
        }
    }

    private fun updateHoursForecast() {
        viewModel.liveHoursForecast.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseStatus.Loading -> {
                    showHoursProgressBar()
                }

                is ResponseStatus.SuccessHoursForecast -> {
                    hideHoursProgressBar()
                    hoursAdapter.submitList(it.list)
                }

                is ResponseStatus.Error -> {
                    showHoursProgressBar()
                    Log.d(
                        "MyLog",
                        "Something is wrong with updateHoursForecast: ${it.throwable}"
                    )
                }
                else -> {}
            }
        }
    }

    private fun updateDaysForecast() {
        viewModel.liveWeeklyForecast.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseStatus.Loading -> {
                    showDaysProgressBar()
                }

                is ResponseStatus.SuccessWeeklyForecast -> {
                    hideDaysProgressBar()
                    daysAdapter.submitList(it.list)
                }

                is ResponseStatus.Error -> {
                    showDaysProgressBar()
                    Log.d(
                        "MyLog",
                        "Something is wrong with updateDaysForecast: ${it.throwable}"
                    )
                }

                else -> {}
            }
        }
    }

    private fun parseCurrentDay(jsonObject: JSONObject): WeatherModel {
        return viewModel.parseCurrentDay(jsonObject)
    }

    private fun parseWeeklyForecast(jsonObject: JSONObject): List<WeeklyForecastModel> {
        return viewModel.parseWeeklyForecast(jsonObject)
    }

    private fun parseHoursForecast(jsonObject: JSONObject): List<HourForecastModel> {
        return viewModel.parseHoursForecast(jsonObject)
    }

    private fun initRecyclers() {
        binding.rvHours.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        hoursAdapter = WeatherHoursAdapter()
        binding.rvHours.adapter = hoursAdapter

        binding.rvDays.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        daysAdapter = WeatherDaysAdapter()
        binding.rvDays.adapter = daysAdapter
    }

    private fun showHoursProgressBar() = with(binding) {
        pbHours.visibility = View.VISIBLE
        rvHours.visibility = View.GONE
        txtCurrentLocationCountry.visibility = View.GONE
        rvDays.visibility = View.GONE
        btnRefresh.visibility = View.GONE
        btnSearch.visibility = View.GONE
        txtCondition.visibility = View.GONE
        txtCurrentTemp.visibility = View.GONE
        txtCurrentLocation.visibility = View.GONE
        txtWeeklyForecast.visibility = View.GONE
        imgCondition.visibility = View.GONE
        imgWeeklyForecast.visibility = View.GONE
    }

    private fun hideHoursProgressBar() = with(binding) {
        pbHours.visibility = View.GONE
        rvHours.visibility = View.VISIBLE
        rvDays.visibility = View.VISIBLE
        txtCurrentLocationCountry.visibility = View.VISIBLE
        btnRefresh.visibility = View.VISIBLE
        btnSearch.visibility = View.VISIBLE
        txtCondition.visibility = View.VISIBLE
        txtCurrentTemp.visibility = View.VISIBLE
        txtCurrentLocation.visibility = View.VISIBLE
        txtWeeklyForecast.visibility = View.VISIBLE
        imgCondition.visibility = View.VISIBLE
        imgWeeklyForecast.visibility = View.VISIBLE
    }

    private fun showDaysProgressBar() = with(binding) {
        pbDays.visibility = View.VISIBLE
        rvHours.visibility = View.GONE
        rvDays.visibility = View.GONE
        txtCurrentLocationCountry.visibility = View.GONE
        btnRefresh.visibility = View.GONE
        btnSearch.visibility = View.GONE
        txtCondition.visibility = View.GONE
        txtCurrentTemp.visibility = View.GONE
        txtCurrentLocation.visibility = View.GONE
        txtWeeklyForecast.visibility = View.GONE
        imgCondition.visibility = View.GONE
        imgWeeklyForecast.visibility = View.GONE
    }

    private fun hideDaysProgressBar() = with(binding) {
        pbDays.visibility = View.GONE
        rvHours.visibility = View.VISIBLE
        rvDays.visibility = View.VISIBLE
        txtCurrentLocationCountry.visibility = View.VISIBLE
        btnRefresh.visibility = View.VISIBLE
        btnSearch.visibility = View.VISIBLE
        txtCondition.visibility = View.VISIBLE
        txtCurrentTemp.visibility = View.VISIBLE
        txtCurrentLocation.visibility = View.VISIBLE
        txtWeeklyForecast.visibility = View.VISIBLE
        imgCondition.visibility = View.VISIBLE
        imgWeeklyForecast.visibility = View.VISIBLE
    }

    private fun showCurrentProgressBar() = with(binding) {
        pbCurrent.visibility = View.VISIBLE
        rvHours.visibility = View.GONE
        rvDays.visibility = View.GONE
        btnRefresh.visibility = View.GONE
        txtCurrentLocationCountry.visibility = View.GONE
        btnSearch.visibility = View.GONE
        txtCondition.visibility = View.GONE
        txtCurrentTemp.visibility = View.GONE
        txtCurrentLocation.visibility = View.GONE
        txtWeeklyForecast.visibility = View.GONE
        imgCondition.visibility = View.GONE
        imgWeeklyForecast.visibility = View.GONE

    }

    private fun hideCurrentProgressBar() = with(binding) {
        pbCurrent.visibility = View.GONE
        rvHours.visibility = View.VISIBLE
        rvDays.visibility = View.VISIBLE
        btnRefresh.visibility = View.VISIBLE
        btnSearch.visibility = View.VISIBLE
        txtCurrentLocationCountry.visibility = View.VISIBLE
        txtCondition.visibility = View.VISIBLE
        txtCurrentTemp.visibility = View.VISIBLE
        txtCurrentLocation.visibility = View.VISIBLE
        txtWeeklyForecast.visibility = View.VISIBLE
        imgCondition.visibility = View.VISIBLE
        imgWeeklyForecast.visibility = View.VISIBLE
    }
}