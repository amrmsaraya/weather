package com.github.amrmsaraya.weather.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.local.WeatherDatabase
import com.github.amrmsaraya.weather.data.models.Location
import com.github.amrmsaraya.weather.data.models.WeatherAnimation
import com.github.amrmsaraya.weather.databinding.FragmentFavoritesBinding
import com.github.amrmsaraya.weather.presentation.adapters.FavoritesAdapter
import com.github.amrmsaraya.weather.presentation.viewModel.LocationViewModel
import com.github.amrmsaraya.weather.presentation.viewModel.SharedViewModel
import com.github.amrmsaraya.weather.presentation.viewModel.WeatherViewModel
import com.github.amrmsaraya.weather.repositories.LocationRepo
import com.github.amrmsaraya.weather.repositories.WeatherRepo
import com.github.amrmsaraya.weather.utils.LocationViewModelFactory
import com.github.amrmsaraya.weather.utils.SharedViewModelFactory
import com.github.amrmsaraya.weather.utils.WeatherViewModelFactory
import com.github.matteobattilana.weather.PrecipType
import kotlinx.coroutines.flow.collect


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_favorites, container, false)

        val locationDao = WeatherDatabase.getInstance(requireActivity().application).locationDao()
        val weatherDao = WeatherDatabase.getInstance(requireActivity().application).weatherDao()

        val locationRepo = LocationRepo(locationDao)
        val weatherRepo = WeatherRepo(requireContext(), weatherDao)

        val sharedFactory = SharedViewModelFactory(requireContext())
        val locationFactory = LocationViewModelFactory(locationRepo)
        val weatherFactory = WeatherViewModelFactory(weatherRepo)

        sharedViewModel =
            ViewModelProvider(requireActivity(), sharedFactory).get(SharedViewModel::class.java)
        locationViewModel =
            ViewModelProvider(requireActivity(), locationFactory).get(LocationViewModel::class.java)
        weatherViewModel =
            ViewModelProvider(requireActivity(), weatherFactory).get(WeatherViewModel::class.java)

        // Reset animation after leaving favorite weather
        if (sharedViewModel.currentFragment.value == "FavoriteWeather") {
            sharedViewModel.setWeatherAnimation(WeatherAnimation(PrecipType.CLEAR, 100f))
        }

        sharedViewModel.setActionBarTitle(getString(R.string.favorites))
        sharedViewModel.setActionBarVisibility(true)
        sharedViewModel.setCurrentFragment("Favorites")
        sharedViewModel.setMapStatus("Favorites")

        binding.rvFavorites.adapter =
            FavoritesAdapter({ location: Location -> favoriteItemClicked(location) },
                { location: Location -> onDelete(location) })

        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        val favoritesAdapter = binding.rvFavorites.adapter as FavoritesAdapter

        lifecycleScope.launchWhenStarted {
            locationViewModel.getAllLocations().collect {
                val locations = it.toMutableList()
                locations.removeAt(0)
                if (locations.isEmpty()) {
                    binding.favoritesEmpty.visibility = View.VISIBLE
                } else {
                    binding.favoritesEmpty.visibility = View.GONE
                }
                favoritesAdapter.submitList(locations)
            }
        }

        binding.fabAddFavorite.setOnClickListener {
            findNavController().navigate(R.id.mapsFragment)
        }

        return binding.root
    }

    private fun favoriteItemClicked(location: Location) {
        sharedViewModel.setClickedFavoriteLocation(location)
        findNavController().navigate(R.id.favoritesWeatherFragment)
    }

    private fun onDelete(location: Location) {
        locationViewModel.delete(location)
        lifecycleScope.launchWhenStarted {
            try {
                weatherViewModel.delete(
                    weatherViewModel.getCachedLocationWeather(
                        location.lat,
                        location.lon
                    )
                )
            } catch (e: Exception) {
                Log.i("FavoritesFragment", "No Weather Saved for this location")
            }
        }
    }
}
