package com.github.amrmsaraya.weather.presentation.alerts

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.domain.model.Alerts
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.presentation.components.AddFAB
import com.github.amrmsaraya.weather.presentation.components.DeleteFAB
import com.github.amrmsaraya.weather.presentation.components.EmptyListIndicator
import com.github.amrmsaraya.weather.service.AlertWorker
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Alert(
    modifier: Modifier = Modifier,
    viewModel: AlertsViewModel = hiltViewModel(),
    onBackPress: () -> Unit
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val alerts = viewModel.alerts
    val selectedItems = remember { mutableStateListOf<Alerts>() }
    var selectMode by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    viewModel.getPreference("accent")
    viewModel.getAlerts()

    BackHandler {
        if (selectMode) {
            selectMode = false
            selectedItems.clear()
        } else {
            onBackPress()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier,
        floatingActionButton = {
            when (selectMode) {
                true -> DeleteFAB {
                    viewModel.deleteAlerts(selectedItems.toList())
                    selectedItems.forEach {
                        WorkManager.getInstance(context).cancelWorkById(UUID.fromString(it.workId))
                    }
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

            if (showDialog) {
                NewAlertDialog(
                    accent = viewModel.accent.value,
                    onConfirm = { from, to, alarm ->
                        if (!Settings.canDrawOverlays(context)) {
                            val permissionIntent = Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + context.packageName)
                            )
                            permissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(permissionIntent)
                        }

                        viewModel.insetAlert(
                            Alerts(
                                from = from,
                                to = to,
                                isAlarm = alarm,
                                workId = scheduleAlert(
                                    context = context,
                                    triggerAt = from
                                ).toString()
                            )
                        )
                    },
                    onDismiss = { showDialog = false })
            }

            if (alerts.isEmpty()) {
                EmptyListIndicator(Icons.Outlined.NotificationsOff, R.string.no_alerts)
            } else {
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
    items: List<Alerts>,
    selectedItems: List<Alerts>,
    selectMode: Boolean,
    onSelectMode: (Boolean) -> Unit,
    onClick: (Forecast) -> Unit,
    onSelect: (Alerts) -> Unit,
    onUnselect: (Alerts) -> Unit
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
                    true -> MaterialTheme.colors.secondary
                    false -> MaterialTheme.colors.surface
                },
                animationSpec = tween(500)
            )

            AlertItem(
                item = item,
                backgroundColor = backgroundColor,
                isSelected = isSelected,
                selectMode = selectMode,
                onClick = onClick,
                onSelect = onSelect,
                onUnselect = onUnselect,
                onSelectMode = onSelectMode,
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun AlertItem(
    item: Alerts,
    backgroundColor: Color,
    isSelected: Boolean,
    selectMode: Boolean,
    onClick: (Forecast) -> Unit,
    onSelect: (Alerts) -> Unit,
    onUnselect: (Alerts) -> Unit,
    onSelectMode: (Boolean) -> Unit,
) {
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
            Text(text = millisToFullDate(item.from), maxLines = 1)
            Text(text = "-", maxLines = 1)
            Text(text = millisToFullDate(item.to), maxLines = 1)
        }
    }
}

@Composable
fun NewAlertDialog(accent: Int, onConfirm: (Long, Long, Boolean) -> Unit, onDismiss: () -> Unit) {

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
        title = { Text(stringResource(R.string.new_alert)) },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DateTimeOutlinedTextField(
                        modifier = Modifier.weight(0.5f),
                        label = stringResource(R.string.from),
                        value = millisToDate(fromDate.timeInMillis),
                        onClick = { showDatePicker(context, accent) { fromDate = it } }
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    DateTimeOutlinedTextField(
                        modifier = Modifier.weight(0.5f),
                        label = stringResource(R.string.from),
                        value = millisToTime(fromTime.timeInMillis),
                        onClick = { showTimePicker(context, accent) { fromTime = it } }
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DateTimeOutlinedTextField(
                        modifier = Modifier.weight(0.5f),
                        label = stringResource(R.string.to),
                        value = millisToDate(toDate.timeInMillis),
                        onClick = { showDatePicker(context, accent) { toDate = it } }
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    DateTimeOutlinedTextField(
                        modifier = Modifier.weight(0.5f),
                        label = stringResource(R.string.to),
                        value = millisToTime(toTime.timeInMillis),
                        onClick = { showTimePicker(context, accent) { toTime = it } }
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(id = R.string.alarm))
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
                        onConfirm(from.timeInMillis, to.timeInMillis, alarmState)
                        onDismiss()
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


private fun showDatePicker(context: Context, accent: Int, onDateChange: (Calendar) -> Unit) {
    val cal = Calendar.getInstance()
    cal.timeInMillis = System.currentTimeMillis()
    val dialog = DatePickerDialog(
        context,
        theme(accent),
        { _, year, month, dayOfMonth ->
            cal.set(year, month, dayOfMonth)
            onDateChange(cal)

        }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]
    )
    dialog.datePicker.minDate = System.currentTimeMillis()
    dialog.show()
}

private fun showTimePicker(context: Context, accent: Int, onTimeChange: (Calendar) -> Unit) {
    val cal = Calendar.getInstance()
    cal.timeInMillis = System.currentTimeMillis()
    val dialog =
        TimePickerDialog(
            context,
            theme(accent),
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
    return SimpleDateFormat("dd-MM-y", Locale.getDefault()).format(timeMillis)
}

private fun millisToTime(timeMillis: Long): String {
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(timeMillis)
}

private fun millisToFullDate(timeMillis: Long): String {
    return SimpleDateFormat("dd MMM  hh:mm a", Locale.getDefault()).format(timeMillis)
}

private fun scheduleAlert(context: Context, triggerAt: Long): UUID {

    val alertWorkRequest = OneTimeWorkRequestBuilder<AlertWorker>()
        .setInitialDelay(triggerAt - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(alertWorkRequest)

    return alertWorkRequest.id
}

private fun theme(color: Int): Int {
    return when (color) {
        1 -> R.style.Dialog1
        2 -> R.style.Dialog2
        3 -> R.style.Dialog3
        4 -> R.style.Dialog4
        5 -> R.style.Dialog5
        else -> R.style.Dialog
    }
}
