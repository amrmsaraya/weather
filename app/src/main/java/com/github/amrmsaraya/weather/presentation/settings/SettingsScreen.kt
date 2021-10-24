package com.github.amrmsaraya.weather.presentation.settings

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.presentation.components.AnimatedVisibilityFade
import com.github.amrmsaraya.weather.presentation.theme.colorsList

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onMapClick: () -> Unit,
    viewModel: SettingsViewModel
) {
    val scrollState = rememberScrollState()

    val settings by remember { viewModel.settings }

    settings?.let { setting ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            val locationItems = listOf(
                R.string.gps,
                R.string.map
            )
            val languageItems = listOf(
                R.string.english,
                R.string.arabic
            )
            val temperatureItems = listOf(
                R.string.celsius,
                R.string.kelvin,
                R.string.fahrenheit
            )
            val windSpeedItems = listOf(
                R.string.meter_sec,
                R.string.mile_hour
            )
            val themeItems = listOf(
                R.string.default_,
                R.string.light,
                R.string.dark
            )

            var expandedLocation by remember { mutableStateOf(false) }
            var expandedLanguage by remember { mutableStateOf(false) }
            var expandedTemperature by remember { mutableStateOf(false) }
            var expandedWindSpeed by remember { mutableStateOf(false) }
            var expandedTheme by remember { mutableStateOf(false) }

            var showAccentDialog by remember { mutableStateOf(false) }

            AccentDialog(
                visible = showAccentDialog,
                onDismiss = { showAccentDialog = false },
                onClick = { viewModel.savePreference("accent", it) },
                selectedColor = setting.accent
            )

            Text(
                text = stringResource(R.string.general),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.primary
            )

            Spacer(modifier = Modifier.size(16.dp))
            DropdownRow(
                title = stringResource(id = R.string.location),
                itemsIds = locationItems,
                expanded = expandedLocation,
                onClick = { expandedLocation = true },
                onDismiss = { expandedLocation = false },
                onMapClick = onMapClick,
                selectedItemId = setting.location,
                onItemClick = {
                    when (it) {
                        R.string.gps -> viewModel.savePreference("location", it)
                        else -> onMapClick()
                    }
                }
            )

            Spacer(modifier = Modifier.size(8.dp))
            DropdownRow(
                title = stringResource(id = R.string.language),
                itemsIds = languageItems,
                expanded = expandedLanguage,
                onClick = { expandedLanguage = true },
                onDismiss = { expandedLanguage = false },
                onMapClick = { },
                selectedItemId = setting.language,
                onItemClick = { viewModel.savePreference("language", it) }
            )

            Spacer(modifier = Modifier.size(8.dp))
            DropdownRow(
                title = stringResource(R.string.theme),
                itemsIds = themeItems,
                expanded = expandedTheme,
                onClick = { expandedTheme = true },
                onDismiss = { expandedTheme = false },
                onMapClick = { },
                selectedItemId = setting.theme,
                onItemClick = { viewModel.savePreference("theme", it) }
            )

            Spacer(modifier = Modifier.size(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(id = R.string.accent))
                Box(
                    modifier = Modifier
                        .size(60.dp, 40.dp)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colors.secondary,
                                    MaterialTheme.colors.primary
                                )
                            ),
                            shape = MaterialTheme.shapes.small
                        )
                        .clickable { showAccentDialog = true }
                )
            }

            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.notifications))
                Switch(
                    checked = setting.notifications,
                    onCheckedChange = { viewModel.savePreference("notifications", it) }
                )
            }

            Spacer(modifier = Modifier.size(16.dp))
            Divider(thickness = 0.5.dp)
            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = stringResource(R.string.units),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.primary
            )

            Spacer(modifier = Modifier.size(16.dp))
            DropdownRow(
                title = stringResource(id = R.string.temperature),
                itemsIds = temperatureItems,
                expanded = expandedTemperature,
                onClick = { expandedTemperature = true },
                onDismiss = { expandedTemperature = false },
                onMapClick = { },
                selectedItemId = setting.temperature,
                onItemClick = { viewModel.savePreference("temperature", it) }
            )

            Spacer(modifier = Modifier.size(8.dp))
            DropdownRow(
                title = stringResource(id = R.string.wind_speed),
                itemsIds = windSpeedItems,
                expanded = expandedWindSpeed,
                onClick = { expandedWindSpeed = true },
                onMapClick = { },
                onDismiss = { expandedWindSpeed = false },
                selectedItemId = setting.windSpeed,
                onItemClick = { viewModel.savePreference("windSpeed", it) }
            )
        }
    }

}

@ExperimentalAnimationApi
@Composable
fun DropdownRow(
    modifier: Modifier = Modifier,
    title: String,
    itemsIds: List<Int>,
    expanded: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    onMapClick: () -> Unit,
    @StringRes selectedItemId: Int,
    onItemClick: (Int) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title)

        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedVisibilityFade(selectedItemId == R.string.map) {
                Button(
                    modifier = Modifier.padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
                    onClick = { onMapClick() }) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        tint = MaterialTheme.colors.secondary,
                        contentDescription = null
                    )
                }
            }
            DropdownMenuBox(
                itemsIds = itemsIds,
                expanded = expanded,
                onClick = onClick,
                onDismiss = onDismiss,
                selectedItemId = selectedItemId,
                onItemClick = { onItemClick(it) }
            )
        }
    }
}

@Composable
fun DropdownMenuBox(
    modifier: Modifier = Modifier,
    itemsIds: List<Int>,
    expanded: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    @StringRes selectedItemId: Int,
    onItemClick: (Int) -> Unit
) {
    Box {
        Card(
            modifier = modifier
                .wrapContentSize(Alignment.Center)
                .clickable { onClick() },
            elevation = 2.dp,
            backgroundColor = MaterialTheme.colors.surface,
            shape = MaterialTheme.shapes.small
        ) {
            Row(
                Modifier.padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = selectedItemId), fontWeight = FontWeight.Normal)
                Spacer(modifier = Modifier.size(8.dp))
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
            offset = DpOffset(0.dp, 4.dp)
        ) {
            itemsIds.forEach { itemId ->
                DropdownMenuItem(onClick = {
                    onDismiss()
                    onItemClick(itemId)
                }) {
                    Text(text = stringResource(id = itemId))
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun AccentDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onClick: (Int) -> Unit,
    selectedColor: Int
) {
    val state = rememberLazyListState()
    if (visible) {
        Dialog(
            onDismissRequest = onDismiss, properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
                    .background(
                        MaterialTheme.colors.surface,
                        shape = MaterialTheme.shapes.medium
                    )
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxWidth(),
                    cells = GridCells.Fixed(2),
                    state = state,
                    contentPadding = PaddingValues(8.dp)
                ) {
                    itemsIndexed(colorsList.toList()) { index, item ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(100.dp, 75.dp)
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            item.lightPalette.secondary,
                                            item.lightPalette.primary
                                        )
                                    ),
                                    shape = MaterialTheme.shapes.small
                                )
                                .clickable {
                                    onClick(index)
                                    onDismiss()
                                }
                        ) {
                            if (index == selectedColor) {
                                Icon(
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(4.dp),
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.surface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
