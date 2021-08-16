package com.github.amrmsaraya.weather.presentation.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.presentation.theme.Spartan
import com.github.amrmsaraya.weather.util.ForecastIcons
import com.github.amrmsaraya.weather.util.ForecastIcons.*
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    SwipeRefresh(
        modifier = modifier,
        state = swipeRefreshState,
        onRefresh = { isRefreshing = true },
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                scale = true,
                contentColor = MaterialTheme.colors.secondary
            )
        },
    ) {
        HomeContent()
    }
}

@Composable
fun HomeContent() {
    val state = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .verticalScroll(state = state),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .placeholder(
                    visible = false,
                    color = Color.LightGray,
                    highlight = PlaceholderHighlight.fade()
                ),
            text = "Al Omraneyah Al Gharbeyah",
            maxLines = 1,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(16.dp))

        TemperatureBox()
        Spacer(modifier = Modifier.size(26.dp))

        HourlyForecast()
        Spacer(modifier = Modifier.size(26.dp))

        DailyForecast()
        Spacer(modifier = Modifier.size(16.dp))

        ForecastDetails()
        Spacer(modifier = Modifier.size(8.dp))

    }
}

@Composable
fun TemperatureBox() {
    Box(
        Modifier.wrapContentSize()
    ) {
        TempAndDescription()
        Image(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = (250 - (130 / 2)).dp)
                .size(130.dp)
                .clip(CircleShape)
                .placeholder(
                    visible = false,
                    color = Color.LightGray,
                    highlight = PlaceholderHighlight.fade()
                ),
            painter = painterResource(id = R.drawable.clear_day),
            contentDescription = "Clear day"
        )
    }
}

@Composable
fun TempAndDescription() {
    Column(
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
            )
            .placeholder(
                visible = false,
                color = Color.LightGray,
                highlight = PlaceholderHighlight.fade(),
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Clear Sky",
            color = Color.White,
            fontSize = 20.sp
        )
        Temp()
    }
}

@Composable
fun Temp(temp: Int = 39) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        ConstraintLayout {
            val (tmp, degree) = createRefs()
            Text(
                modifier = Modifier.constrainAs(tmp) { centerTo(parent) },
                text = temp.toString(),
                color = Color.White,
                style = MaterialTheme.typography.h1,
                fontFamily = Spartan
            )
            Text(
                modifier = Modifier
                    .constrainAs(degree) { start.linkTo(tmp.end) }
                    .padding(top = 20.dp),
                text = "°C",
                color = Color.White,
                style = MaterialTheme.typography.h4
            )
        }
    }
}

@Composable
fun HourlyForecast() {
    val list = List(24) { Hourly("32°C", "1 AM", R.drawable.clear_day_24) }
    LazyRow(verticalAlignment = Alignment.CenterVertically) {
        items(list) {
            Card(
                modifier = Modifier
                    .padding(start = 2.dp, end = 8.dp)
                    .placeholder(
                        visible = false,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.fade()
                    ),
                elevation = 2.dp,
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = it.hour,
                        color = Color.Gray
                    )
                    Image(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(24.dp),
                        painter = painterResource(id = it.icon),
                        contentDescription = "Weather Icon"
                    )
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = it.temp
                    )
                }
            }
        }
    }
}

@Composable
fun DailyForecast() {
    val list = MutableList(6) {
        Daily(
            "32",
            "16",
            "Clear Sky",
            "Tomorrow",
            R.drawable.clear_day_24
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (item in list.indices) {
            Card(
                modifier = if (item == 0) Modifier
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
                    .placeholder(
                        visible = false,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.fade(),
                        shape = MaterialTheme.shapes.medium
                    )
                else Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .placeholder(
                        visible = false,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.fade(),
                        shape = MaterialTheme.shapes.medium
                    ),
                elevation = if (item == 0) 0.dp else 2.dp,
                backgroundColor = if (item == 0) Color.Transparent else MaterialTheme.colors.surface,
            ) {
                ConstraintLayout(
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                ) {
                    val (day, icon, description, temp) = createRefs()
                    Text(
                        modifier = Modifier.constrainAs(day) {
                            centerVerticallyTo(parent)
                            start.linkTo(parent.start, margin = 16.dp)
                        },
                        text = list[item].day,
                        maxLines = 1,
                        color = if (item == 0) Color.White else MaterialTheme.colors.onSurface
                    )
                    Image(
                        modifier = Modifier
                            .constrainAs(icon) {
                                centerVerticallyTo(parent)
                                end.linkTo(description.start, margin = 8.dp)
                            }
                            .size(24.dp),
                        painter = painterResource(id = list[item].icon),
                        contentDescription = "Weather Icon"
                    )
                    Text(
                        modifier = Modifier
                            .requiredWidth(90.dp)
                            .constrainAs(description) {
                                centerVerticallyTo(parent)
                                end.linkTo(temp.start, margin = 8.dp)
                            },
                        text = list[item].description,
                        maxLines = 1,
                        textAlign = TextAlign.End,
                        color = if (item == 0) Color.White else MaterialTheme.colors.onSurface
                    )
                    Text(
                        modifier = Modifier
                            .requiredWidth(76.dp)
                            .constrainAs(temp) {
                                centerVerticallyTo(parent)
                                end.linkTo(parent.end, margin = 16.dp)
                            },
                        text = "${list[item].tempMax} / ${list[item].tempMin}°C",
                        textAlign = TextAlign.End,
                        color = if (item == 0) Color.White else MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun ForecastDetails() {
    val details = ForecastDetails(
        pressure = "1006 hpa",
        humidity = "21 %",
        wind = "3.55 m/s",
        cloud = "0 %",
        ultraViolet = "9.32",
        visibility = "10000 m"
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .placeholder(
                visible = false,
                color = Color.LightGray,
                highlight = PlaceholderHighlight.fade(),
                shape = MaterialTheme.shapes.medium
            ),
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
                        ForecastDetailsItem(item = item, details = details)
                    }
                }
            }
        }
    }
}

@Composable
fun ForecastDetailsItem(item: ForecastIcons, details: ForecastDetails) {
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
                Pressure -> details.pressure
                Humidity -> details.humidity
                Wind -> details.wind
                Cloud -> details.cloud
                UltraViolet -> details.ultraViolet
                Visibility -> details.visibility
            }
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = item.nameId),
            color = Color.Gray
        )
    }
}

data class Hourly(
    val temp: String,
    val hour: String,
    @DrawableRes
    val icon: Int
)

data class Daily(
    val tempMax: String,
    val tempMin: String,
    val description: String,
    val day: String,
    @DrawableRes
    val icon: Int,
    val name: String = "Place",
    val id: Int = 0,
)

data class ForecastDetails(
    val pressure: String,
    val humidity: String,
    val wind: String,
    val cloud: String,
    val ultraViolet: String,
    val visibility: String,
)
