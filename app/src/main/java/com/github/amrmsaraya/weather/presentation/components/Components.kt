package com.github.amrmsaraya.weather.presentation.components

import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun AddFAB(onClick: () -> Unit) {
    FloatingActionButton(
        contentColor = MaterialTheme.colors.surface,
        onClick = onClick
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
}

@Composable
fun DeleteFAB(onClick: () -> Unit) {
    FloatingActionButton(
        contentColor = MaterialTheme.colors.secondary,
        backgroundColor = MaterialTheme.colors.surface,
        onClick = onClick
    ) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedVisibilityFade(
    visible: Boolean,
    durationMillis: Int = 500,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis)),
        exit = fadeOut(animationSpec = tween(durationMillis)),
        content = content
    )
}

@Composable
fun EmptyListIndicator(image: ImageVector, @StringRes stringId: Int) {
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
            Text(text = stringResource(stringId))
        }
    }
}
