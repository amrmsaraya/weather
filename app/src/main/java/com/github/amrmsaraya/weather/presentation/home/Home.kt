package com.github.amrmsaraya.weather.presentation.home

import android.Manifest
import android.app.Activity
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.domain.model.Settings
import com.github.amrmsaraya.weather.domain.model.forecast.Current
import com.github.amrmsaraya.weather.domain.model.forecast.Daily
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.model.forecast.Hourly
import com.github.amrmsaraya.weather.presentation.components.*
import com.github.amrmsaraya.weather.presentation.theme.Cairo
import com.github.amrmsaraya.weather.presentation.theme.Spartan
import com.github.amrmsaraya.weather.util.ForecastIcons
import com.github.amrmsaraya.weather.util.ForecastIcons.*
import com.github.amrmsaraya.weather.util.GeocoderHelper
import com.github.amrmsaraya.weather.util.LocationHelper
import com.github.amrmsaraya.weather.util.WeatherIcons
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@ExperimentalAnimationApi
@ExperimentalPermissionsApi
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToMap: () -> Unit
) {
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    val forecast by viewModel.forecast
    var settings by viewModel.settings
    var latLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    // clear settings when leave composition so UI doesn't reload when revisit compose
    DisposableEffect(key1 = true) {
        onDispose {
            settings = null
        }
    }

    settings?.let { setting ->
        Scaffold(
            modifier = modifier,
            scaffoldState = scaffoldState
        ) {
            if (error.isNotEmpty()) {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(message = error)
                    viewModel.error.value = ""
                }
            }
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.isLoading.value = true
                    refresh(setting, latLng, viewModel)
                },
                indicator = { state, trigger ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = trigger,
                        scale = true,
                        contentColor = MaterialTheme.colors.secondary
                    )
                },
            ) {
                AnimatedVisibilityFade(forecast.current.weather.isEmpty()) {
                    LoadingIndicator()
                }
                when (setting.location) {
                    R.string.gps -> GPSLocation(
                        viewModel = viewModel,
                        forecast = forecast,
                        setting = setting,
                        onLocationChange = { latLng = it },
                        onNavigateToMap = onNavigateToMap,
                    )
                    else -> {
                        LaunchedEffect(key1 = true) {
                            viewModel.getForecast()
                        }
                        AnimatedVisibilityFade(forecast.current.weather.isNotEmpty()) {
                            HomeContent(forecast, setting)
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalPermissionsApi
@Composable
private fun GPSLocation(
    viewModel: HomeViewModel,
    forecast: Forecast,
    setting: Settings,
    onLocationChange: (LatLng) -> Unit,
    onNavigateToMap: () -> Unit,
) {
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    LocationPermission(
        permissionState = locationPermissionState,
        requestPermission = {
            RequestPermission(
                locationPermissionState,
                onNavigateToMap
            )
        },
        noPermission = { NoPermission() },
        hasPermission = {
            val location = LocationHelper(
                activity = LocalContext.current as Activity,
                onLocationChange = {
                    onLocationChange(LatLng(it.latitude, it.longitude))
                    viewModel.getForecast(it.latitude, it.longitude)
                }
            )
            DisposableEffect(key1 = true) {
                location.startLocationUpdates()
                onDispose {
                    location.stopLocationUpdates()
                }
            }
            AnimatedVisibilityFade(forecast.current.weather.isNotEmpty()) {
                HomeContent(forecast, setting)
            }
        })
}

@Composable
fun HomeContent(forecast: Forecast, settings: Settings) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .verticalScroll(state = ScrollState(0)),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            text = GeocoderHelper.getCity(LocalContext.current, forecast.lat, forecast.lon),
            maxLines = 1,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(16.dp))

        TemperatureBox(forecast.current, settings)
        Spacer(modifier = Modifier.size(26.dp))

        HourlyForecast(forecast.hourly.subList(0, 25), forecast.daily, settings)
        Spacer(modifier = Modifier.size(26.dp))

        DailyForecast(forecast.daily.subList(1, 7), settings)
        Spacer(modifier = Modifier.size(16.dp))

        ForecastDetails(forecast.current, settings)
        Spacer(modifier = Modifier.size(8.dp))
    }
}

@Composable
fun TemperatureBox(current: Current, settings: Settings) {
    Box(
        Modifier.wrapContentSize()
    ) {
        TempAndDescription(
            current.temp,
            current.weather[0].description.replaceFirstChar { it.uppercase() },
            settings
        )
        Image(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = (250 - (130 / 2)).dp)
                .size(130.dp),
            painter = painterResource(
                id = WeatherIcons.getCurrentIcon(
                    current.weather[0].main,
                    sunrise = current.sunrise,
                    sunset = current.sunset
                )
            ),
            contentDescription = "Clear day"
        )
    }
}

@Composable
fun TempAndDescription(temp: Double, description: String, settings: Settings) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colors.secondary,
                        MaterialTheme.colors.primary
                    )
                )
            ),
    ) {
        val (tmp, desc) = createRefs()
        Text(
            modifier = Modifier.constrainAs(desc) {
                centerHorizontallyTo(parent)
                top.linkTo(parent.top, margin = 16.dp)
            },
            text = description,
            color = Color.White,
            fontSize = 20.sp
        )
        Temp(modifier = Modifier
            .constrainAs(tmp) {
                centerTo(parent)
            }
            .padding(bottom = 16.dp), temp, settings
        )
    }
}

@Composable
fun Temp(modifier: Modifier = Modifier, temp: Double, settings: Settings) {
    ConstraintLayout(modifier = modifier) {
        val (tmp, degree) = createRefs()
        Text(
            modifier = Modifier.constrainAs(tmp) { centerTo(parent) },
            text = getTemp(temp, settings.temperature),
            color = Color.White,
            style = MaterialTheme.typography.h1,
            fontFamily = if (settings.language == R.string.arabic) Cairo else Spartan
        )
        Text(
            modifier = Modifier
                .constrainAs(degree) { start.linkTo(tmp.end) }
                .padding(top = 20.dp),
            text = stringResource(id = getTempUnit(settings.temperature)),
            color = Color.White,
            style = MaterialTheme.typography.h4
        )
    }
}

@Composable
fun HourlyForecast(items: List<Hourly>, daily: List<Daily>, settings: Settings) {
    LazyRow(
        state = LazyListState(0),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items) { item ->
            Card(
                modifier = Modifier.padding(start = 2.dp, end = 8.dp),
                elevation = 2.dp,
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                        text = hourFormat(item.dt),
                        color = Color.Gray
                    )
                    Image(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(24.dp),
                        painter = painterResource(
                            id = WeatherIcons.getHourlyIcon(
                                weather = item.weather[0].main,
                                time = item.dt,
                                today = daily[0],
                                tomorrow = daily[1]
                            )
                        ),
                        contentDescription = "Weather Icon"
                    )
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "${
                            getTemp(
                                item.temp,
                                settings.temperature
                            )
                        } ${stringResource(id = getTempUnit(settings.temperature))}"
                    )
                }
            }
        }
    }
}

@Composable
fun DailyForecast(items: List<Daily>, settings: Settings) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (index in items.indices) {
            Card(
                modifier = if (index == 0) Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colors.secondary,
                                MaterialTheme.colors.primary
                            )
                        )
                    )
                else Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                elevation = if (index == 0) 0.dp else 2.dp,
                backgroundColor = if (index == 0) Color.Transparent else MaterialTheme.colors.surface,
            ) {
                ConstraintLayout(
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                ) {
                    val (day, icon, description, temp) = createRefs()
                    Text(
                        modifier = Modifier.constrainAs(day) {
                            centerVerticallyTo(parent)
                            start.linkTo(parent.start, margin = 16.dp)
                            end.linkTo(icon.start, 8.dp)
                            width = Dimension.fillToConstraints
                        },
                        text = when (index == 0) {
                            true -> stringResource(id = R.string.tomorrow)
                            false -> weekdayFormat(items[index].dt)
                        },
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        color = if (index == 0) Color.White else MaterialTheme.colors.onSurface
                    )
                    Image(
                        modifier = Modifier
                            .constrainAs(icon) {
                                centerVerticallyTo(parent)
                                end.linkTo(description.start, margin = 8.dp)
                            }
                            .size(24.dp),
                        painter = painterResource(id = WeatherIcons.getDailyIcon(items[index].weather[0].main)),
                        contentDescription = "Weather Icon"
                    )
                    Text(
                        modifier = Modifier
                            .requiredWidth(80.dp)
                            .constrainAs(description) {
                                centerVerticallyTo(parent)
                                end.linkTo(temp.start, margin = 8.dp)
                            },
                        text = items[index].weather[0].description.replaceFirstChar { it.uppercase() },
                        maxLines = 1,
                        textAlign = TextAlign.End,
                        color = if (index == 0) Color.White else MaterialTheme.colors.onSurface
                    )
                    Text(
                        modifier = Modifier
                            .requiredWidth(90.dp)
                            .constrainAs(temp) {
                                centerVerticallyTo(parent)
                                end.linkTo(parent.end, margin = 16.dp)
                            },
                        text = "${
                            getTemp(
                                items[index].temp.max,
                                settings.temperature
                            )
                        } / ${
                            getTemp(
                                items[index].temp.min,
                                settings.temperature
                            )
                        } ${stringResource(id = getTempUnit(settings.temperature))}",
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        color = if (index == 0) Color.White else MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun ForecastDetails(current: Current, settings: Settings) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround

        ) {
            repeat(3) {
                val range = when (it) {
                    0 -> Pair(0, 2)
                    1 -> Pair(2, 4)
                    else -> Pair(4, 6)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    for (item in ForecastIcons.values()
                        .copyOfRange(range.first, range.second)) {
                        ForecastDetailsItem(item = item, current = current, settings = settings)
                    }
                }
            }
        }
    }
}

@Composable
fun ForecastDetailsItem(item: ForecastIcons, current: Current, settings: Settings) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp),
            painter = painterResource(id = item.icon),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
            contentDescription = "Weather Icon"
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = when (item) {
                Pressure -> "${current.pressure.localize()} ${stringResource(id = R.string.hpa)}"
                Humidity -> "${current.humidity.localize()} %"
                Wind -> when (settings.windSpeed) {
                    R.string.meter_sec -> "${current.windSpeed.localize()} ${stringResource(id = R.string.m_s)}"
                    else -> "${(current.windSpeed * 2.236936).localize()} ${stringResource(id = R.string.mph)}"
                }
                Cloud -> "${current.clouds.localize()} %"
                UltraViolet -> current.uvi.localize()
                Visibility -> "${current.visibility.localize()} ${stringResource(id = R.string.meter)}"
            }
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = item.nameId),
            color = Color.Gray
        )
    }
}

private fun refresh(
    settings: Settings,
    latLng: LatLng,
    viewModel: HomeViewModel
) {
    when (settings.location) {
        R.string.gps -> {
            if (latLng.latitude != 0.0) {
                viewModel.getForecast(latLng.latitude, latLng.longitude)
            }
        }
        R.string.map -> viewModel.getForecast()
    }
}

private fun hourFormat(time: Int): String {
    return SimpleDateFormat("h a", Locale.getDefault()).format(time.toLong() * 1000)
}

private fun weekdayFormat(time: Int): String {
    return SimpleDateFormat("E", Locale.getDefault()).format(time.toLong() * 1000)
}

fun getTemp(temp: Double, @StringRes unit: Int): String {
    return when (unit) {
        R.string.celsius -> temp.roundToInt()
        R.string.kelvin -> (temp + 273.15).roundToInt()
        else -> ((temp * 1.8) + 32).roundToInt()
    }.localize()

}

fun getTempUnit(@StringRes unit: Int): Int {
    return when (unit) {
        R.string.celsius -> R.string.celsius_unit
        R.string.kelvin -> R.string.kelvin_unit
        else -> R.string.fahrenheit_unit
    }
}

fun Int.localize(): String {
    return String.format("%d", this)
}

fun Double.localize(): String {
    return String.format("%.1f", this)
}
