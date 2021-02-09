package com.github.amrmsaraya.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_settings, container, false)

        MainActivity.binding.tvTitle.text = getString(R.string.settings)
        MainActivity.binding.navView.setCheckedItem(R.id.navSettings)
        return binding.root
    }

}
