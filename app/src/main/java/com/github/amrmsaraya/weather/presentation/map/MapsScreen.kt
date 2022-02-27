package com.github.amrmsaraya.weather.presentation.map

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.util.GeocoderHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
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
    val scope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var showBottomSheet by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(LatLng(uiState.forecast.lat, uiState.forecast.lon)) }
    var city by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(uiState.forecast.lat, uiState.forecast.lon),
            0f
        )
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
        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                location = it
                city = GeocoderHelper.getCity(context, it.latitude, it.longitude)
                address = GeocoderHelper.getAddress(context, it.latitude, it.longitude)
                markerPosition = LatLng(it.latitude, it.longitude)
                scope.launch {
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 17f))
                }
                showBottomSheet = true
            }
        ) {
            markerPosition?.let { Marker(position = it) }
        }
    }
}