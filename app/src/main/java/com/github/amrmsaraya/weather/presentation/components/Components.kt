package com.github.amrmsaraya.weather.presentation.components

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.amrmsaraya.weather.BuildConfig
import com.github.amrmsaraya.weather.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState
import kotlinx.coroutines.delay

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
        contentColor = MaterialTheme.colors.surface,
        onClick = onClick
    ) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedVisibilityFade(
    visible: Boolean,
    durationMillis: Int = 1000,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis)),
        exit = fadeOut(animationSpec = tween(durationMillis)),
        content = content
    )
}

@ExperimentalAnimationApi
@Composable
fun AnimatedVisibilitySlide(
    visible: Boolean,
    durationMillis: Int = 500,
    delay: Int = 0,
    reduceOffsetYBy: Int = 1,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    var execute by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = visible) {
        delay(delay.toLong() * durationMillis)
        execute = visible
    }
    AnimatedVisibility(
        visible = execute,
        enter = slideInVertically(
            initialOffsetY = { it / reduceOffsetYBy },
            animationSpec = tween(durationMillis)
        ),
        exit = slideOutVertically(animationSpec = tween(durationMillis)),
        content = content
    )
}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colors.secondary)
    }
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

@ExperimentalAnimationApi
@ExperimentalPermissionsApi
@Composable
fun LocationPermission(
    permissionState: PermissionState,
    hasPermission: @Composable () -> Unit,
    noPermission: @Composable () -> Unit,
    requestPermission: @Composable () -> Unit
) {
    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = { requestPermission() },
        permissionNotAvailableContent = { noPermission() }
    ) {
        hasPermission()
    }
}

@ExperimentalPermissionsApi
@Composable
fun RequestPermission(
    permissionState: PermissionState,
    noPermission: Boolean,
    onNavigateToMap: () -> Unit
) {
    var showSettings by remember { mutableStateOf(false) }
    if (showSettings) {
        LaunchAppSettingsScreen()
        showSettings = false
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                modifier = Modifier.size(100.dp),
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(id = R.string.we_can_t_fetch_your_location),
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = when (noPermission) {
                    true -> stringResource(R.string.denied_permission_many_times)
                    false -> stringResource(id = R.string.allow_weather_to_access_location)
                },
                style = MaterialTheme.typography.body1,
                color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LocationSelection(
                    text = stringResource(R.string.gps),
                    icon = Icons.Default.GpsFixed,
                    onClick = {
                        when (noPermission) {
                            true -> showSettings = true
                            false -> permissionState.launchPermissionRequest()
                        }
                    }
                )
                LocationSelection(
                    text = stringResource(R.string.map),
                    icon = Icons.Default.Map,
                    onClick = { onNavigateToMap() }
                )
            }
        }
    }
}

@Composable
private fun LocationSelection(text: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colors.primary, CircleShape)
                .padding(16.dp)
                .size(42.dp),
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = text,
            color = MaterialTheme.colors.primary
        )
    }
}

@Composable
fun LaunchAppSettingsScreen() {
    val intent = Intent()
    val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
    intent.apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = uri
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    LocalContext.current.startActivity(intent)
}
