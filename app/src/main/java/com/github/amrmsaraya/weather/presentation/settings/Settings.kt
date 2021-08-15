package com.github.amrmsaraya.weather.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.github.amrmsaraya.weather.R

@Composable
fun Settings(modifier: Modifier = Modifier, onMapClick: () -> Unit) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        val locationItems =
            listOf(
                stringResource(id = R.string.gps),
                stringResource(id = R.string.map)
            )
        val languageItems =
            listOf(
                stringResource(id = R.string.english),
                stringResource(id = R.string.arabic)
            )
        val temperatureItems = listOf(
            stringResource(id = R.string.celsius),
            stringResource(id = R.string.kelvin),
            stringResource(id = R.string.fahrenheit)
        )
        val windSpeedItems =
            listOf(
                stringResource(id = R.string.meter_sec),
                stringResource(id = R.string.mile_hour)
            )

        var expandedLocation by remember { mutableStateOf(false) }
        var expandedLanguage by remember { mutableStateOf(false) }
        var expandedTemperature by remember { mutableStateOf(false) }
        var expandedWindSpeed by remember { mutableStateOf(false) }
        var notificationsChecked by remember { mutableStateOf(true) }

        var location by remember { mutableStateOf(locationItems.first()) }
        var language by remember { mutableStateOf(languageItems.first()) }
        var temperature by remember { mutableStateOf(temperatureItems.first()) }
        var windSpeed by remember { mutableStateOf(windSpeedItems.first()) }

        if (location == stringResource(id = R.string.map)) {
            onMapClick()
        }
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
            selectedItem = language,
            onItemClick = { language = it }
        )
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
            selectedItem = temperature,
            onItemClick = { temperature = it }
        )
        Spacer(modifier = Modifier.size(8.dp))
        DropdownRow(
            title = stringResource(id = R.string.wind_speed),
            items = windSpeedItems,
            expanded = expandedWindSpeed,
            onClick = { expandedWindSpeed = true },
            onDismiss = { expandedWindSpeed = false },
            selectedItem = windSpeed,
            onItemClick = { windSpeed = it }
        )
    }
}

@Composable
fun DropdownRow(
    modifier: Modifier = Modifier,
    title: String,
    items: List<String>,
    expanded: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    selectedItem: String,
    onItemClick: (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title)
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
    Box(modifier = modifier.wrapContentSize(Alignment.TopEnd)) {
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor =
                MaterialTheme.colors.surface
            ),
            onClick = onClick
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedItem, fontWeight = FontWeight.Normal)
                Spacer(modifier = Modifier.size(4.dp))
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }
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
