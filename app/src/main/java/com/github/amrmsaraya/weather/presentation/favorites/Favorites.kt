package com.github.amrmsaraya.weather.presentation.favorites

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.domain.model.Settings
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.presentation.components.AddFAB
import com.github.amrmsaraya.weather.presentation.components.DeleteFAB
import com.github.amrmsaraya.weather.presentation.components.EmptyListIndicator
import com.github.amrmsaraya.weather.presentation.home.getTemp
import com.github.amrmsaraya.weather.presentation.home.getTempUnit
import com.github.amrmsaraya.weather.util.GeocoderHelper
import com.github.amrmsaraya.weather.util.WeatherIcons

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Favorites(
    modifier: Modifier = Modifier,
    onItemClick: (Double, Double) -> Unit,
    onNavigateToMap: () -> Unit,
    onBackPress: () -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {

    viewModel.getFavoriteForecasts()
    viewModel.restorePreferences()

    val scaffoldState = rememberScaffoldState()
    val selectedItems = remember { mutableStateListOf<Forecast>() }
    var selectMode by remember { mutableStateOf(false) }
    val favorites = viewModel.favorites
    val settings by viewModel.settings

    BackHandler {
        if (selectMode) {
            selectMode = false
            selectedItems.clear()
        } else {
            onBackPress()
        }
    }

    settings?.let { setting ->
        Scaffold(
            scaffoldState = scaffoldState,
            modifier = modifier,
            floatingActionButton = {
                when (selectMode) {
                    true -> DeleteFAB {
                        viewModel.deleteForecast(selectedItems.toList())
                        selectedItems.clear()
                        selectMode = false
                    }
                    false -> AddFAB { onNavigateToMap() }
                }
            },
            floatingActionButtonPosition = if (selectMode) FabPosition.Center else FabPosition.End
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            ) {

                if (favorites.isEmpty()) {
                    EmptyListIndicator(Icons.Filled.FavoriteBorder, R.string.no_favorites)
                } else {
                    FavoritesList(
                        items = favorites,
                        selectedItems = selectedItems,
                        selectMode = selectMode,
                        settings = setting,
                        onSelectMode = { selectMode = it },
                        onClick = {
                            onItemClick(it.lat, it.lon)
                            println("id = ${it.id}")
                        },
                        onSelect = { selectedItems.add(it) },
                        onUnselect = {
                            selectedItems.remove(it)
                            if (selectedItems.isEmpty()) {
                                selectMode = false
                            }
                        }
                    )
                }
            }
        }
    }

}

@ExperimentalFoundationApi
@Composable
fun FavoritesList(
    items: List<Forecast>,
    selectedItems: List<Forecast>,
    selectMode: Boolean,
    settings: Settings,
    onSelectMode: (Boolean) -> Unit,
    onClick: (Forecast) -> Unit,
    onSelect: (Forecast) -> Unit,
    onUnselect: (Forecast) -> Unit
) {
    val state = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = state
    ) {
        items(items) { item ->
            val isSelected = selectedItems.any { it == item }

            val backgroundColor by animateColorAsState(
                targetValue = when (isSelected) {
                    true -> MaterialTheme.colors.secondary
                    false -> MaterialTheme.colors.surface
                },
                animationSpec = tween(500)
            )
            FavoriteItem(
                item = item,
                settings = settings,
                backgroundColor = backgroundColor,
                isSelected = isSelected,
                selectMode = selectMode,
                onSelect = onSelect,
                onUnselect = onUnselect,
                onClick = onClick,
                onSelectMode = onSelectMode,
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun FavoriteItem(
    item: Forecast,
    settings: Settings,
    backgroundColor: Color,
    isSelected: Boolean,
    selectMode: Boolean,
    onSelect: (Forecast) -> Unit,
    onUnselect: (Forecast) -> Unit,
    onClick: (Forecast) -> Unit,
    onSelectMode: (Boolean) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .combinedClickable(
                onClick = {
                    if (selectMode) {
                        when (isSelected) {
                            true -> onUnselect(item)
                            false -> onSelect(item)
                        }
                    } else {
                        onClick(Forecast(id = item.id))
                    }
                },
                onLongClick = {
                    if (!selectMode) {
                        onSelectMode(true)
                        onSelect(item)
                    }
                }
            ),
        elevation = if (isSelected) 0.dp else 2.dp,
        backgroundColor = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = 16.dp),
                text = GeocoderHelper.getCity(LocalContext.current, item.lat, item.lon),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.weight(0.5f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    Modifier
                        .weight(0.5f)
                        .padding(end = 16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    val unknown = stringResource(id = R.string.unknown)
                    Text(
                        text = getTemp(
                            item.current.temp,
                            settings.temperature
                        ) + " " + stringResource(id = getTempUnit(settings.temperature))
                    )
                    Text(
                        text = when (item.current.weather.isEmpty()) {
                            true -> unknown
                            false -> item.current.weather[0].description.replaceFirstChar { it.uppercase() }
                        },
                        color = if (isSelected) MaterialTheme.colors.onSurface else Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .wrapContentSize(),
                    painter = painterResource(
                        id = when (item.current.weather.isEmpty()) {
                            true -> R.drawable.clear_day
                            false -> WeatherIcons.getCurrentIcon(
                                item.current.weather[0].main,
                                item.current.sunrise,
                                item.current.sunset
                            )
                        }
                    ),
                    contentDescription = null
                )
            }
        }
    }
}
