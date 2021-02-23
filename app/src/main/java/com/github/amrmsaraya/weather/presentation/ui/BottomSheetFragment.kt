package com.github.amrmsaraya.weather.presentation.ui

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.local.WeatherDatabase
import com.github.amrmsaraya.weather.data.models.Location
import com.github.amrmsaraya.weather.databinding.BottomSheetFragmentBinding
import com.github.amrmsaraya.weather.presentation.viewModel.LocationViewModel
import com.github.amrmsaraya.weather.presentation.viewModel.SharedViewModel
import com.github.amrmsaraya.weather.repositories.LocationRepo
import com.github.amrmsaraya.weather.utils.LocationViewModelFactory
import com.github.amrmsaraya.weather.utils.SharedViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetFragmentBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var geocoder: Geocoder
    private var addresses = mutableListOf<Address>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var city = getString(R.string.unknown)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_fragment, container, false)

        val locationDao = WeatherDatabase.getInstance(requireActivity().application).locationDao()
        val locationRepo = LocationRepo(locationDao)

        val sharedFactory = SharedViewModelFactory(requireContext())
        val locationFactory = LocationViewModelFactory(locationRepo)

        sharedViewModel =
            ViewModelProvider(requireActivity(), sharedFactory).get(SharedViewModel::class.java)
        locationViewModel =
            ViewModelProvider(requireActivity(), locationFactory).get(LocationViewModel::class.java)

        val lat = roundDouble(sharedViewModel.latLng.value.latitude)
        val lon = roundDouble(sharedViewModel.latLng.value.longitude)
        geocoder = Geocoder(context, Locale.getDefault())

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                city = if (!addresses[0].locality.isNullOrEmpty()) {
                    addresses[0].locality
                } else if (!addresses[0].adminArea.isNullOrEmpty()) {
                    addresses[0].adminArea
                } else if (!addresses[0].getAddressLine(0).isNullOrEmpty()) {
                    addresses[0].getAddressLine(0)
                } else {
                    getString(R.string.unknown)
                }.toString()

                binding.tvCity.text = city

                if (!addresses[0].getAddressLine(0).isNullOrEmpty()) {
                    binding.tvAddress.text = addresses[0].getAddressLine(0)
                } else {
                    binding.tvAddress.text = getString(R.string.unknown)
                }
            } else {
                binding.tvCity.text = getString(R.string.unknown)
                binding.tvAddress.text = getString(R.string.unknown)
                city = getString(R.string.unknown)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.btnSaveLocation.setOnClickListener {
            when (sharedViewModel.mapStatus.value) {
                "Current" -> {
                    locationViewModel.insert(Location(lat, lon, city, 1))
                    lifecycleScope.launchWhenStarted {
                        sharedViewModel.setDefaultSettings("Map")
                    }
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.homeFragment)
                }
                "Favorites" -> {
                    locationViewModel.insert(Location(lat, lon, city))
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.favoritesFragment)
                }
            }
        }
        return binding.root
    }

    private fun roundDouble(double: Double): Double {
        val d = DecimalFormat("#.####")
        return BigDecimal(double).setScale(4, RoundingMode.HALF_UP).toDouble()
//        return "%.4f".format(double).toDouble()
    }
}
