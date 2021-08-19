package com.github.amrmsaraya.weather.presentation.favorites

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.models.Forecast
import com.github.amrmsaraya.weather.presentation.components.AddFAB
import com.github.amrmsaraya.weather.presentation.components.AnimatedVisibilityFade
import com.github.amrmsaraya.weather.presentation.components.DeleteFAB
import com.github.amrmsaraya.weather.presentation.components.EmptyListIndicator
import com.github.amrmsaraya.weather.presentation.theme.LightPink

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Favorites(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {}
//    val scaffoldState = rememberScaffoldState()
//
//    val favorites = remember {
//        mutableStateListOf<Daily>()
//    }
//
//    val selectedItems = remember {
//        mutableStateListOf<Daily>()
//    }
//
//    var selectMode by remember {
//        mutableStateOf(false)
//    }
//
//    BackHandler {
//        if (selectMode) {
//            selectMode = false
//            selectedItems.clear()
//        }
//    }
//
//    Scaffold(
//        scaffoldState = scaffoldState,
//        modifier = modifier,
//        floatingActionButton = {
//            when (selectMode) {
//                true -> DeleteFAB {
//                    favorites.removeAll(selectedItems)
//                    selectedItems.clear()
//                    selectMode = false
//                }
//                false -> AddFAB { }
//            }
//        },
//        floatingActionButtonPosition = if (selectMode) FabPosition.Center else FabPosition.End
//    ) { innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
//        ) {
//
//            AnimatedVisibilityFade(favorites.isEmpty()) {
//                EmptyListIndicator(Icons.Filled.FavoriteBorder, R.string.no_favorites)
//            }
//
//            AnimatedVisibilityFade(favorites.isNotEmpty()) {
//                FavoritesList(
//                    items = favorites,
//                    selectedItems = selectedItems,
//                    selectMode = selectMode,
//                    onSelectMode = { selectMode = it },
//                    onClick = { },
//                    onSelect = { selectedItems.add(it) },
//                    onUnselect = {
//                        selectedItems.remove(it)
//                        if (selectedItems.isEmpty()) {
//                            selectMode = false
//                        }
//                    }
//                )
//            }
//        }
//    }
//}
//
//@ExperimentalFoundationApi
//@Composable
//fun FavoritesList(
//    items: List<Daily>,
//    selectedItems: List<Daily>,
//    selectMode: Boolean,
//    onSelectMode: (Boolean) -> Unit,
//    onClick: (Forecast) -> Unit,
//    onSelect: (Daily) -> Unit,
//    onUnselect: (Daily) -> Unit
//) {
//    val state = rememberLazyListState()
//
//    LazyColumn(
//        modifier = Modifier.fillMaxWidth(),
//        state = state
//    ) {
//        items(items) { item ->
//            val isSelected = selectedItems.any { it == item }
//
//            val backgroundColor by animateColorAsState(
//                targetValue = when (isSelected) {
//                    true -> if (isSystemInDarkTheme()) LightPink.copy(0.8f) else LightPink
//                    false -> MaterialTheme.colors.surface
//                },
//                animationSpec = tween(500)
//            )
//
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 8.dp, bottom = 8.dp)
//                    .combinedClickable(
//                        onClick = {
//                            onClick(Forecast(id = 4))
//                            if (selectMode) {
//                                when (isSelected) {
//                                    true -> onUnselect(item)
//                                    false -> onSelect(item)
//                                }
//                            }
//                        },
//                        onLongClick = {
//                            if (!selectMode) {
//                                onSelectMode(true)
//                                onSelect(item)
//                            }
//                        }
//                    ),
//                elevation = if (isSelected) 0.dp else 2.dp,
//                backgroundColor = backgroundColor
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(
//                        modifier = Modifier
//                            .weight(0.5f)
//                            .padding(end = 16.dp),
//                        text = item.name,
//                        maxLines = 2,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                    Row(
//                        modifier = Modifier.weight(0.5f),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Column(
//                            Modifier
//                                .weight(0.5f)
//                                .padding(end = 16.dp),
//                            horizontalAlignment = Alignment.End
//                        ) {
//                            Text(text = "${item.tempMax}°C")
//                            Text(
//                                text = item.description,
//                                color = if (isSelected) MaterialTheme.colors.onSurface else Color.Gray,
//                                maxLines = 1,
//                                overflow = TextOverflow.Ellipsis
//                            )
//                        }
//                        Image(
//                            modifier = Modifier
//                                .size(40.dp)
//                                .wrapContentSize(),
//                            painter = painterResource(id = item.icon),
//                            contentDescription = null
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
