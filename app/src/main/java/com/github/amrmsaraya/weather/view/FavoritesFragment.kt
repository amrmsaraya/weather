package com.github.amrmsaraya.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.databinding.FragmentFavoritesBinding


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_favorites, container, false)
        MainActivity.binding.navView.setCheckedItem(R.id.navFavorites)
        MainActivity.binding.tvTitle.text = getString(R.string.favorites)
        val x = requireActivity() as AppCompatActivity
        x.supportActionBar?.show()
        binding.fabAddFavorite.setOnClickListener {
            it.findNavController().navigate(R.id.action_favoritesFragment_to_mapsFragment)
        }

        return binding.root
    }

}
