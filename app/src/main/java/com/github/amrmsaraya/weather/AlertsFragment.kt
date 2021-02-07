package com.github.amrmsaraya.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.amrmsaraya.weather.databinding.FragmentAlertsBinding
import com.github.amrmsaraya.weather.databinding.FragmentSettingsBinding

class AlertsFragment : Fragment() {
    private lateinit var binding: FragmentAlertsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_alerts, container, false)

        MainActivity.binding.tvTitle.text = getString(R.string.alerts)
        MainActivity.binding.navView.setCheckedItem(R.id.navAlerts)
        return binding.root
    }
}
