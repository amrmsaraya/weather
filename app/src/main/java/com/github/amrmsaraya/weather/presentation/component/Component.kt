package com.github.amrmsaraya.weather.presentation.component

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun FABScaffold(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    onFABClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier,
        floatingActionButton = { AddFAB { onFABClick() } },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            content = content
        )
    }
}

@Composable
fun AddFAB(onClick: () -> Unit) {
    FloatingActionButton(
        contentColor = MaterialTheme.colors.surface,
        onClick = { }
    ) {
        IconButton(onClick = onClick) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }

    }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedVisibilityFade(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(1000)),
        exit = fadeOut(animationSpec = tween(1000)),
        content = content
    )
}
