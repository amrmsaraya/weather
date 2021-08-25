package com.github.amrmsaraya.weather.presentation.components

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.amrmsaraya.weather.BuildConfig
import com.github.amrmsaraya.weather.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

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

@ExperimentalPermissionsApi
@Composable
fun LocationPermission(
    permissionState: PermissionState,
    hasPermission: @Composable () -> Unit,
    noPermission: @Composable () -> Unit,
    requestPermission: @Composable () -> Unit
) {
    // Track if the user doesn't want to see the rationale any more.
//    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }

    when {
        permissionState.hasPermission -> hasPermission()
        permissionState.shouldShowRationale || !permissionState.permissionRequested -> {
            requestPermission()
        }
        else -> noPermission()
    }
}

@ExperimentalPermissionsApi
@Composable
fun RequestPermission(permissionState: PermissionState, onNavigateToMap: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.we_can_t_fetch_your_location),
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = stringResource(id = R.string.please_allow_weather_to_access_your_location),
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.size(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        onClick = { permissionState.launchPermissionRequest() }
                    ) {
                        Text(text = stringResource(id = R.string.allow))
                    }

                    Spacer(modifier = Modifier.size(8.dp))

                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        onClick = { onNavigateToMap() }
                    ) {
                        Text(text = stringResource(R.string.from_map))
                    }
                }
            }
        }
    }
}

@Composable
fun NoPermission() {
    var showSettings by remember { mutableStateOf(false) }
    if (showSettings) {
        LaunchAppSettingsScreen()
        showSettings = false
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.we_can_t_fetch_your_location),
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = stringResource(id = R.string.please_allow_weather_to_access_your_location),
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.size(8.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showSettings = true }
                ) {
                    Text(text = stringResource(id = R.string.settings))
                }
            }
        }
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
