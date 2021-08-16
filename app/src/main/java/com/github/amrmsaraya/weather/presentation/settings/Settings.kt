package com.github.amrmsaraya.weather.presentation.settings

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.presentation.components.AnimatedVisibilityFade
import com.github.amrmsaraya.weather.presentation.theme.Blue
import com.github.amrmsaraya.weather.presentation.theme.Pink

@ExperimentalAnimationApi
@Composable
fun Settings(modifier: Modifier = Modifier, onMapClick: () -> Unit, isDarkTheme: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        val locationItems = listOf(
            stringResource(id = R.string.gps),
            stringResource(id = R.string.map)
        )
        val languageItems = listOf(
            stringResource(id = R.string.english),
            stringResource(id = R.string.arabic)
        )
        val temperatureItems = listOf(
            stringResource(id = R.string.celsius),
            stringResource(id = R.string.kelvin),
            stringResource(id = R.string.fahrenheit)
        )
        val windSpeedItems = listOf(
            stringResource(id = R.string.meter_sec),
            stringResource(id = R.string.mile_hour)
        )
        val themeItems = listOf(
            stringResource(id = R.string.default_),
            stringResource(id = R.string.light),
            stringResource(id = R.string.dark)
        )


        var expandedLocation by remember { mutableStateOf(false) }
        var expandedLanguage by remember { mutableStateOf(false) }
        var expandedTemperature by remember { mutableStateOf(false) }
        var expandedWindSpeed by remember { mutableStateOf(false) }
        var expandedTheme by remember { mutableStateOf(false) }
        var notificationsChecked by remember { mutableStateOf(true) }

        var location by remember { mutableStateOf(locationItems.first()) }
        var language by remember { mutableStateOf(languageItems.first()) }
        var temperature by remember { mutableStateOf(temperatureItems.first()) }
        var windSpeed by remember { mutableStateOf(windSpeedItems.first()) }
        var theme by remember { mutableStateOf(themeItems.first()) }

        isDarkTheme(theme)

        Text(
            text = stringResource(R.string.general),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.size(16.dp))

        DropdownRow(
            title = stringResource(id = R.string.location),
            items = locationItems,
            expanded = expandedLocation,
            onClick = { expandedLocation = true },
            onDismiss = { expandedLocation = false },
            onMapClick = onMapClick,
            selectedItem = location,
            onItemClick = { location = it }
        )
        Spacer(modifier = Modifier.size(8.dp))
        DropdownRow(
            title = stringResource(id = R.string.language),
            items = languageItems,
            expanded = expandedLanguage,
            onClick = { expandedLanguage = true },
            onDismiss = { expandedLanguage = false },
            onMapClick = { },
            selectedItem = language,
            onItemClick = { language = it }
        )
        Spacer(modifier = Modifier.size(8.dp))
        DropdownRow(
            title = stringResource(R.string.theme),
            items = themeItems,
            expanded = expandedTheme,
            onClick = { expandedTheme = true },
            onDismiss = { expandedTheme = false },
            onMapClick = { },
            selectedItem = theme,
            onItemClick = { theme = it }
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
                        Brush.linearGradient(listOf(Pink, Blue)),
                        shape = MaterialTheme.shapes.small
                    )
                    .clickable { }
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
                checked = notificationsChecked,
                onCheckedChange = { notificationsChecked = !notificationsChecked }
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
            items = temperatureItems,
            expanded = expandedTemperature,
            onClick = { expandedTemperature = true },
            onDismiss = { expandedTemperature = false },
            onMapClick = { },
            selectedItem = temperature,
            onItemClick = { temperature = it }
        )
        Spacer(modifier = Modifier.size(8.dp))
        DropdownRow(
            title = stringResource(id = R.string.wind_speed),
            items = windSpeedItems,
            expanded = expandedWindSpeed,
            onClick = { expandedWindSpeed = true },
            onMapClick = { },
            onDismiss = { expandedWindSpeed = false },
            selectedItem = windSpeed,
            onItemClick = { windSpeed = it }
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun DropdownRow(
    modifier: Modifier = Modifier,
    title: String,
    items: List<String>,
    expanded: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    onMapClick: () -> Unit,
    selectedItem: String,
    onItemClick: (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title)

        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedVisibilityFade(selectedItem == stringResource(id = R.string.map)) {
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
                items = items,
                expanded = expanded,
                onClick = onClick,
                onDismiss = onDismiss,
                selectedItem = selectedItem,
                onItemClick = { onItemClick(it) }
            )
        }
    }
}

@Composable
fun DropdownMenuBox(
    modifier: Modifier = Modifier,
    items: List<String>,
    expanded: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    selectedItem: String,
    onItemClick: (String) -> Unit
) {
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
            Text(text = selectedItem, fontWeight = FontWeight.Normal)
            Spacer(modifier = Modifier.size(8.dp))
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
            offset = DpOffset(0.dp, 4.dp)
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onDismiss()
                    onItemClick(item)
                }) {
                    Text(text = item)
                }
            }
        }
    }
}
