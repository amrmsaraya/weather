package com.github.amrmsaraya.weather.presentation.alerts

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.models.Forecast
import com.github.amrmsaraya.weather.presentation.components.AddFAB
import com.github.amrmsaraya.weather.presentation.components.AnimatedVisibilityFade
import com.github.amrmsaraya.weather.presentation.components.DeleteFAB
import com.github.amrmsaraya.weather.presentation.components.EmptyListIndicator
import com.github.amrmsaraya.weather.presentation.home.Daily
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Alert(modifier: Modifier = Modifier) {
    val scaffoldState = rememberScaffoldState()

    val selectedItems = remember {
        mutableStateListOf<Daily>()
    }

    var selectMode by remember {
        mutableStateOf(false)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }
    BackHandler {
        if (selectMode) {
            selectMode = false
            selectedItems.clear()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier,
        floatingActionButton = {
            when (selectMode) {
                true -> DeleteFAB { }
                false -> AddFAB { showDialog = true }
            }
        },
        floatingActionButtonPosition = if (selectMode) FabPosition.Center else FabPosition.End
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
        ) {
            val alerts = remember {
                mutableStateListOf<Daily>()
            }

            AddAlertDialog(showDialog = showDialog, onDismiss = { showDialog = false })

            alerts.clear()
            alerts.addAll(MutableList(20) {
                Daily(
                    "32",
                    "16",
                    "Clear Sky",
                    "Tomorrow",
                    R.drawable.clear_day,
                    "Talkha",
                    id = it
                )
            })

            AnimatedVisibilityFade(alerts.isEmpty()) {
                EmptyListIndicator(Icons.Outlined.NotificationsOff, R.string.no_alerts)
            }

            AnimatedVisibilityFade(alerts.isNotEmpty()) {
                AlertList(
                    items = alerts,
                    selectedItems = selectedItems,
                    selectMode = selectMode,
                    onSelectMode = { selectMode = it },
                    onClick = { },
                    onSelect = { selectedItems.add(it) },
                    onUnselect = {
                        selectedItems.remove(it)
                        if (selectedItems.isEmpty()) {
                            selectMode = false
                        }
                    }
                )
            }
        }
    }
}


@ExperimentalFoundationApi
@Composable
fun AlertList(
    items: List<Daily>,
    selectedItems: List<Daily>,
    selectMode: Boolean,
    onSelectMode: (Boolean) -> Unit,
    onClick: (Forecast) -> Unit,
    onSelect: (Daily) -> Unit,
    onUnselect: (Daily) -> Unit
) {
    val state = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = state
    ) {
        items(items) { item ->
            val isSelected = selectedItems.any { it == item }

            val backgroundColor = when (isSelected) {
                true -> MaterialTheme.colors.secondary.copy(alpha = 0.7f)
                false -> MaterialTheme.colors.surface
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .combinedClickable(
                        onClick = {
                            onClick(Forecast(id = 4))
                            if (selectMode) {
                                when (isSelected) {
                                    true -> onUnselect(item)
                                    false -> onSelect(item)
                                }
                            }
                        },
                        onLongClick = {
                            if (!selectMode) {
                                onSelectMode(true)
                                onSelect(item)
                            }
                        }
                    ),
                elevation = if (isSelected) 0.dp else 2.dp,
                backgroundColor = backgroundColor
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row() {
                        Text(
                            text = "14 Aug,",
                            maxLines = 1,
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "12:18 AM",
                            maxLines = 1,
                        )
                    }
                    Text(
                        text = "-",
                        maxLines = 1,
                    )
                    Row {
                        Text(
                            text = "14 Aug,",
                            maxLines = 1,
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "12:18 AM",
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddAlertDialog(showDialog: Boolean, onDismiss: () -> Unit) {
    var state by remember {
        mutableStateOf(true)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("New alert") },
            text = {
                Column() {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            modifier = Modifier.weight(0.5f),
                            label = { Text(text = "From Date") },
                            value = millisToDate(System.currentTimeMillis()),
                            onValueChange = {},
                            readOnly = true
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        OutlinedTextField(
                            modifier = Modifier.weight(0.5f),
                            label = { Text(text = "From Time") },
                            value = millisToTime(System.currentTimeMillis()),
                            onValueChange = {},
                            readOnly = true
                        )
                    }

                    Spacer(modifier = Modifier.size(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            modifier = Modifier.weight(0.5f),
                            label = { Text(text = "To Date") },
                            value = millisToDate(System.currentTimeMillis()),
                            onValueChange = {},
                            readOnly = true
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        OutlinedTextField(
                            modifier = Modifier.weight(0.5f),
                            label = { Text(text = "To Time") },
                            value = millisToTime(System.currentTimeMillis()),
                            onValueChange = {},
                            readOnly = true
                        )
                    }

                    Spacer(modifier = Modifier.size(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Alarm")
                        Switch(
                            checked = state,
                            onCheckedChange = { state = !state },
                            colors = SwitchDefaults.colors()
                        )
                    }

                }
            },
            buttons = {
                Row(
                    Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f),
                        onClick = onDismiss
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onDismiss
                    ) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                }
            }
        )
    }

}


@Composable
fun DatePicker(show: Boolean) {
    val dialog = remember { MaterialDialog() }
    dialog.build(
        buttons = {
            positiveButton(stringResource(id = R.string.ok))
            negativeButton(stringResource(id = R.string.cancel))
        }
    ) {
        datepicker() { date ->
            // Do stuff with java.time.LocalDate object which is passed in
        }
    }
    if (show) {
        dialog.show()
    }
}

@Composable
fun TimePicker() {
    val dialog = remember { MaterialDialog() }
    dialog.build(buttons = {
        positiveButton("Ok")
        negativeButton("Cancel")
    }) {
        timepicker() { time ->
            // Do stuff with java.time.LocalDate object which is passed in
        }
    }
    dialog.show()
}


private fun millisToDate(timeMillis: Long): String {
    return SimpleDateFormat("y-MM-dd", Locale.getDefault()).format(timeMillis)
}

private fun millisToTime(timeMillis: Long): String {
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(timeMillis)
}
