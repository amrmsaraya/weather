package com.github.amrmsaraya.weather.presentation.favorites

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Favorites(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = { AddFavorite() },
        floatingActionButtonPosition = FabPosition.End
    ) {


    }
}

@Composable
fun AddFavorite() {
    FloatingActionButton(
        contentColor = MaterialTheme.colors.surface,
        onClick = {  }
    ) {
        IconButton(onClick = {  }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Favorite")
        }

    }
}
