package com.github.amrmsaraya.weather.presenter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.databinding.FragmentFavoritesBinding
import com.github.amrmsaraya.weather.presenter.viewModel.SharedViewModel


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_favorites, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        sharedViewModel.setActionBarTitle("Favorites")
        sharedViewModel.setActionBarVisibility("Show")
        sharedViewModel.setCurrentFragment("Favorites")
        sharedViewModel.setMapStatus("Favorites")

        binding.fabAddFavorite.setOnClickListener {
            it.findNavController().navigate(R.id.action_favoritesFragment_to_mapsFragment)
        }

        return binding.root
    }

}
