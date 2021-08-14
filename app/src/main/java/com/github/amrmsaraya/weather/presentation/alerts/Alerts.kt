package com.github.amrmsaraya.weather.presentation.alerts

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.models.Forecast
import com.github.amrmsaraya.weather.presentation.components.AddFAB
import com.github.amrmsaraya.weather.presentation.components.AnimatedVisibilityFade
import com.github.amrmsaraya.weather.presentation.components.DeleteFAB
import com.github.amrmsaraya.weather.presentation.components.EmptyListIndicator
import com.github.amrmsaraya.weather.presentation.home.Daily
import com.github.amrmsaraya.weather.presentation.theme.LightPink
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Alert(modifier: Modifier = Modifier) {

    val scaffoldState = rememberScaffoldState()
    val alerts = remember { mutableStateListOf<Daily>() }
    val selectedItems = remember { mutableStateListOf<Daily>() }
    var selectMode by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

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
                true -> DeleteFAB {
                    alerts.removeAll(selectedItems)
                    selectedItems.clear()
                    selectMode = false

                }
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

            if(showDialog) {
                NewAlertDialog(onDismiss = { showDialog = false })
            }

//            LaunchedEffect(true){
//                alerts.clear()
//                alerts.addAll(MutableList(20) {
//                    Daily(
//                        "32",
//                        "16",
//                        "Clear Sky",
//                        "Tomorrow",
//                        R.drawable.clear_day,
//                        "Talkha",
//                        id = it
//                    )
//                })
//            }

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
        modifier = Modifier.fillMaxSize(),
        state = state
    ) {
        items(items) { item ->
            val isSelected = selectedItems.any { it == item }

            val backgroundColor by animateColorAsState(
                targetValue = when (isSelected) {
                    true -> if (isSystemInDarkTheme()) LightPink.copy(0.8f) else LightPink
                    false -> MaterialTheme.colors.surface
                },
                animationSpec = tween(750)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
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
fun NewAlertDialog(onDismiss: () -> Unit) {

    val context = LocalContext.current
    var alarmState by remember { mutableStateOf(true) }

    var fromDate by remember { mutableStateOf(Calendar.getInstance()) }
    var fromTime by remember { mutableStateOf(Calendar.getInstance()) }
    var toDate by remember { mutableStateOf(Calendar.getInstance()) }
    var toTime by remember { mutableStateOf(Calendar.getInstance()) }

    val from = Calendar.getInstance()
    from.set(
        fromDate[Calendar.YEAR],
        fromDate[Calendar.MONTH],
        fromDate[Calendar.DAY_OF_MONTH],
        fromTime[Calendar.HOUR_OF_DAY],
        fromTime[Calendar.MINUTE],
    )

    val to = Calendar.getInstance()
    to.set(
        toDate[Calendar.YEAR],
        toDate[Calendar.MONTH],
        toDate[Calendar.DAY_OF_MONTH],
        toTime[Calendar.HOUR_OF_DAY],
        toTime[Calendar.MINUTE],
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New alert") },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DateTimeOutlinedTextField(
                        modifier = Modifier.weight(0.5f),
                        label = "From Date",
                        value = millisToDate(fromDate.timeInMillis),
                        onClick = { showDatePicker(context) { fromDate = it } }
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    DateTimeOutlinedTextField(
                        modifier = Modifier.weight(0.5f),
                        label = "From Time",
                        value = millisToTime(fromTime.timeInMillis),
                        onClick = { showTimePicker(context) { fromTime = it } }
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DateTimeOutlinedTextField(
                        modifier = Modifier.weight(0.5f),
                        label = "To Date",
                        value = millisToDate(toDate.timeInMillis),
                        onClick = { showDatePicker(context) { toDate = it } }
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    DateTimeOutlinedTextField(
                        modifier = Modifier.weight(0.5f),
                        label = "To Time",
                        value = millisToTime(toTime.timeInMillis),
                        onClick = { showTimePicker(context) { toTime = it } }
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
                        checked = alarmState,
                        onCheckedChange = { alarmState = !alarmState },
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
                    onClick = {
                        onDismiss()
                        println(
                            "DATETIME -> ${millisToDate(from.timeInMillis)}, ${
                                millisToTime(
                                    from.timeInMillis
                                )
                            }"
                        )
                        println("DATETIME -> ${millisToDate(to.timeInMillis)}, ${millisToTime(to.timeInMillis)}")
                    }
                ) {
                    Text(text = stringResource(id = R.string.ok))
                }
            }
        }
    )
}

@Composable
private fun DateTimeOutlinedTextField(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String,
    value: String
) {
    OutlinedTextField(
        modifier = modifier.clickable(onClick = onClick),
        label = { Text(label) },
        value = value,
        onValueChange = {},
        readOnly = true,
        enabled = false,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
            disabledLabelColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
            disabledBorderColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
        )
    )
}


private fun showDatePicker(context: Context, onDateChange: (Calendar) -> Unit) {
    val cal = Calendar.getInstance()
    cal.timeInMillis = System.currentTimeMillis()
    val dialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            cal.set(year, month, dayOfMonth)
            onDateChange(cal)

        }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]
    )
    dialog.show()
}

private fun showTimePicker(context: Context, onTimeChange: (Calendar) -> Unit) {
    val cal = Calendar.getInstance()
    cal.timeInMillis = System.currentTimeMillis()
    val dialog =
        TimePickerDialog(
            context,
            { _, hour, minute ->
                cal.set(
                    cal[Calendar.YEAR],
                    cal[Calendar.MONTH],
                    cal[Calendar.DAY_OF_MONTH],
                    hour,
                    minute
                )
                onTimeChange(cal)

            }, cal[Calendar.HOUR_OF_DAY], cal[Calendar.MINUTE], false
        )
    dialog.show()
}


private fun millisToDate(timeMillis: Long): String {
    return SimpleDateFormat("y-MM-dd", Locale.getDefault()).format(timeMillis)
}

private fun millisToTime(timeMillis: Long): String {
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(timeMillis)
}
