package com.github.amrmsaraya.weather.presenter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.databinding.FragmentMapBinding
import com.github.amrmsaraya.weather.presenter.viewModel.SharedViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.delay

class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var sharedViewModel: SharedViewModel
    private val bottomSheetFragment = BottomSheetFragment()

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.setOnMapClickListener {
            googleMap.clear()
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 17f))
            googleMap.addMarker(MarkerOptions().position(it).title("Marker in Sydney"))
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                delay(1500)
                if (sharedViewModel.mapStatus.value == "Default") {
                    bottomSheetFragment.show(childFragmentManager, "BottomSheetFragment")
                } else if (sharedViewModel.mapStatus.value == "Favorites") {
                    sharedViewModel.setLatLng(it)
                    bottomSheetFragment.show(childFragmentManager, "BottomSheetFragment")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_map, container, false)

        // Hide Action Bar
        sharedViewModel.setActionBarVisibility("Hide")
        sharedViewModel.setCurrentFragment("Map")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


}

