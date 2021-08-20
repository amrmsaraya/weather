package com.github.amrmsaraya.weather.presentation.map

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.github.amrmsaraya.weather.R
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch

@Composable
fun Maps(
    modifier: Modifier,
    viewModel: MapViewModel = hiltViewModel()
) {
    val map = rememberMapViewWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        LaunchedEffect(map) {
            val googleMap = map.awaitMap()
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(-35.016, 143.321)))
            googleMap.addMarker(
                MarkerOptions().position(LatLng(-35.016, 143.321))
            )
        }

        val coroutineScope = rememberCoroutineScope()
        AndroidView({ map }) { mapView ->
            coroutineScope.launch {
                val googleMap = mapView.awaitMap()

                googleMap.setOnMapClickListener {
                    googleMap.clear()
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 17f))
                    googleMap.addMarker(
                        MarkerOptions().position(it)
                    )
                    viewModel.savePreference("location", R.string.map)
                }
            }
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(key1 = lifecycle, key2 = mapView) {
        // Make MapView follow the current lifecycle
        val lifecycleObserver = getMapLifecycleObserver(mapView)
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

private fun getMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
            Lifecycle.Event.ON_START -> mapView.onStart()
            Lifecycle.Event.ON_RESUME -> mapView.onResume()
            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            Lifecycle.Event.ON_STOP -> mapView.onStop()
            Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
            else -> throw IllegalStateException()
        }
    }
