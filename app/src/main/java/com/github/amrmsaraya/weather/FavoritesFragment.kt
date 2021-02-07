package com.github.amrmsaraya.weather

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import com.github.amrmsaraya.weather.databinding.FragmentFavoritesBinding
import com.github.amrmsaraya.weather.databinding.FragmentHomeBinding


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_favorites, container, false)

        binding.fabAddFavorite.setOnClickListener {
            it.findNavController().navigate(R.id.action_favoritesFragment_to_mapsFragment)

        }
        return binding.root
    }

}
