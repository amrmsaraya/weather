package com.github.amrmsaraya.weather.presentation.map

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.util.GeocoderHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun MapsScreen(
    modifier: Modifier,
    isCurrent: Boolean,
    onBackPress: () -> Unit,
    viewModel: MapViewModel,
) {
    val uiState by viewModel.uiState
    val context = LocalContext.current
    val map = rememberMapViewWithLifecycle()
    val scope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var showBottomSheet by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(LatLng(uiState.forecast.lat, uiState.forecast.lon)) }
    var city by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

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
                Row(
                    modifier = Modifier.align(CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            isLoading = true
                            scope.launch {
                                if (isCurrent) {
                                    viewModel.intent.value = MapIntent.InsertForecast(
                                        uiState.forecast.copy(
                                            lat = location.latitude,
                                            lon = location.longitude
                                        )
                                    )
                                } else {
                                    viewModel.intent.value = MapIntent.GetForecastFromMap(
                                        location.latitude,
                                        location.longitude
                                    )
                                }
                                while (uiState.isLoading != null) {
                                    delay(500)
                                }
                                onBackPress()
                            }
                        }
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(start = 32.dp, end = 32.dp)
                                    .size(20.dp),
                                color = MaterialTheme.colors.surface,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                modifier = Modifier.padding(start = 32.dp, end = 32.dp),
                                text = stringResource(id = R.string.save)
                            )
                        }
                    }
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
            LaunchedEffect(map, uiState.forecast) {
                val googleMap = map.awaitMap()
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(uiState.forecast.lat, uiState.forecast.lon),
                        4f
                    )
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
