package com.github.amrmsaraya.weather.presentation.favorites

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.models.Forecast
import com.github.amrmsaraya.weather.presentation.component.AnimatedVisibilityFade
import com.github.amrmsaraya.weather.presentation.component.FABScaffold
import com.github.amrmsaraya.weather.presentation.home.Daily
import kotlinx.coroutines.delay

@ExperimentalAnimationApi
@Composable
fun Favorites(modifier: Modifier = Modifier) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    FABScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        onFABClick = { /*TODO*/ }
    ) {

        val favorites = remember {
            mutableStateListOf<Daily>()
        }
        LaunchedEffect(key1 = null) {
            delay(1000)
            favorites.add(
                Daily(
                    "32",
                    "16",
                    "Clear Sky",
                    "Tomorrow",
                    R.drawable.clear_day,
                    "Giza"
                )
            )
        }

        AnimatedVisibilityFade(favorites.isEmpty()) {
            EmptyListIndicator(Icons.Filled.FavoriteBorder)
        }
        AnimatedVisibilityFade(favorites.isNotEmpty()) {
            FavoritesList(
                list = favorites,
                onClick = { }
            )
        }

    }
}


@Composable
fun FavoritesList(list: List<Daily>, onClick: (Forecast) -> Unit) {
    val state = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = state
    ) {
        items(list) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clickable { onClick(Forecast(id = 4)) },
                elevation = 2.dp
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
                        text = it.name,
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
                            Text(text = "${it.tempMax}°C")
                            Text(
                                text = it.description,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Image(
                            modifier = Modifier
                                .size(40.dp)
                                .wrapContentSize(),
                            painter = painterResource(id = it.icon),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyListIndicator(image: ImageVector) {
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(75.dp),
                imageVector = image,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary)
            )
            Text(text = stringResource(id = R.string.no_favorites))
        }
    }


}
