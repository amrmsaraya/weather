package com.github.amrmsaraya.weather.presentation.favorites

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.models.Forecast
import com.github.amrmsaraya.weather.presentation.components.AddFAB
import com.github.amrmsaraya.weather.presentation.components.AnimatedVisibilityFade
import com.github.amrmsaraya.weather.presentation.components.DeleteFAB
import com.github.amrmsaraya.weather.presentation.components.EmptyListIndicator
import com.github.amrmsaraya.weather.presentation.home.Daily

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Favorites(modifier: Modifier = Modifier) {
    val scaffoldState = rememberScaffoldState()

    val selectedItems = remember {
        mutableStateListOf<Daily>()
    }

    var selectMode by remember {
        mutableStateOf(false)
    }

    BackHandler {
        if (selectMode) {
            selectMode = false
            selectedItems.clear()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier,
        floatingActionButton = {
            when (selectMode) {
                true -> DeleteFAB { }
                false -> AddFAB { }
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
            val favorites = remember {
                mutableStateListOf<Daily>()
            }

            favorites.clear()
            favorites.addAll(MutableList(20) {
                Daily(
                    "32",
                    "16",
                    "Clear Sky",
                    "Tomorrow",
                    R.drawable.clear_day,
                    "Talkha",
                    id = it
                )
            })

            AnimatedVisibilityFade(favorites.isEmpty()) {
                EmptyListIndicator(Icons.Filled.FavoriteBorder, R.string.no_favorites)
            }

            AnimatedVisibilityFade(favorites.isNotEmpty()) {
                FavoritesList(
                    items = favorites,
                    selectedItems = selectedItems,
                    selectMode = selectMode,
                    onSelectMode = { selectMode = it },
                    onClick = { },
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

@ExperimentalFoundationApi
@Composable
fun FavoritesList(
    items: List<Daily>,
    selectedItems: List<Daily>,
    selectMode: Boolean,
    onSelectMode: (Boolean) -> Unit,
    onClick: (Forecast) -> Unit,
    onSelect: (Daily) -> Unit,
    onUnselect: (Daily) -> Unit
) {
    val state = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = state
    ) {
        items(items) { item ->
            val isSelected = selectedItems.any { it == item }

            val backgroundColor = when (isSelected) {
                true -> MaterialTheme.colors.secondary.copy(alpha = 0.7f)
                false -> MaterialTheme.colors.surface
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .combinedClickable(
                        onClick = {
                            onClick(Forecast(id = 4))
                            if (selectMode) {
                                when (isSelected) {
                                    true -> onUnselect(item)
                                    false -> onSelect(item)
                                }
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
                        text = item.name,
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
                            Text(text = "${item.tempMax}°C")
                            Text(
                                text = item.description,
                                color = if (isSelected) MaterialTheme.colors.onSurface else Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Image(
                            modifier = Modifier
                                .size(40.dp)
                                .wrapContentSize(),
                            painter = painterResource(id = item.icon),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
