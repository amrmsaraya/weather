package com.github.amrmsaraya.weather.presenter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.databinding.BottomSheetFragmentBinding
import com.github.amrmsaraya.weather.presenter.viewModel.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetFragmentBinding
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_fragment, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        Toast.makeText(binding.root.context, sharedViewModel.latLng.value.toString(),Toast.LENGTH_SHORT).show()


        binding.btnSaveLocation.setOnClickListener {
            findNavController().navigate(R.id.action_mapsFragment_to_favoritesFragment)
        }
        return binding.root
    }
}
