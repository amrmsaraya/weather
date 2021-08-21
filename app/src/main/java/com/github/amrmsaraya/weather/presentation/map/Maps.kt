package com.github.amrmsaraya.weather.presentation.map

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.models.Forecast
import com.github.amrmsaraya.weather.data.models.ForecastRequest
import com.github.amrmsaraya.weather.util.GeocoderHelper
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun Maps(
    modifier: Modifier,
    isCurrent: Boolean = true,
    onBackPress: () -> Unit,
    viewModel: MapViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val map = rememberMapViewWithLifecycle()
    val scope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var showBottomSheet by remember { mutableStateOf(false) }
    val currentForecast by viewModel.currentForecast
    var location by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var city by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    if (isCurrent) {
        viewModel.getCurrentForecast()
    }

    BackHandler {
        if (bottomSheetState.isVisible) {
            scope.launch { bottomSheetState.hide() }
        } else {
            onBackPress()
        }
    }

    ModalBottomSheetLayout(
        modifier = modifier.fillMaxSize(),
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetElevation = 0.dp,
        sheetState = bottomSheetState,
        sheetContent = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 16.dp)
            ) {
                Text(
                    text = city,
                    maxLines = 1,
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = address,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    modifier = Modifier
                        .align(CenterHorizontally),
                    onClick = {
                        if (isCurrent) {
                            viewModel.insertForecast(
                                currentForecast.copy(
                                    lat = location.latitude,
                                    lon = location.longitude
                                )
                            )
                            viewModel.savePreference("location", R.string.map)
                        } else {
                            viewModel.insertForecast(
                                Forecast(lat = location.latitude, lon = location.longitude)
                            )
                            viewModel.getCurrentForecast(
                                ForecastRequest(lat = location.latitude, lon = location.longitude)
                            )
                        }
                        scope.launch { bottomSheetState.hide() }
                        onBackPress()
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(start = 32.dp, end = 32.dp),
                        text = stringResource(id = R.string.save)
                    )
                }
            }
        }
    ) {
        if (showBottomSheet) {
            scope.launch {
                showBottomSheet = false
                bottomSheetState.show()
            }
        }
        Box(modifier = modifier.fillMaxSize()) {
            LaunchedEffect(map) {
                val googleMap = map.awaitMap()
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(-35.016, 143.321)))
                googleMap.addMarker(
                    MarkerOptions().position(LatLng(-35.016, 143.321))
                )
            }
            AndroidView({ map }) { mapView ->
                scope.launch {
                    val googleMap = mapView.awaitMap()

                    googleMap.setOnMapClickListener {
                        location = it
                        city = GeocoderHelper.getCity(context, it.latitude, it.longitude)
                        address = GeocoderHelper.getAddress(context, it.latitude, it.longitude)
                        googleMap.clear()
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 17f))
                        googleMap.addMarker(
                            MarkerOptions().position(it)
                        )
                        showBottomSheet = true
                    }
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
