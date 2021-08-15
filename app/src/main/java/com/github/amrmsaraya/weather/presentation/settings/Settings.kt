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

@Composable
fun Settings(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        var isLanguageExpanded by remember { mutableStateOf(false) }

        Text(text = "General", style = MaterialTheme.typography.h6)
        Row() {
            Text(text = "Language")
            Box(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart)) {

                Button(onClick = { isLanguageExpanded = true }) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "English")
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
                DropdownMenu(

                    expanded = isLanguageExpanded,
                    onDismissRequest = { isLanguageExpanded = false }) {
                    DropdownMenuItem(onClick = { /*TODO*/ }) {
                        Text(text = "English")
                    }
                    DropdownMenuItem(onClick = { /*TODO*/ }) {
                        Text(text = "Arabic")
                    }
                }
            }
        }
    }
}
