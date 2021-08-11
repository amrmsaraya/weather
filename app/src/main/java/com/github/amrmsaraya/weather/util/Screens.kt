package com.github.amrmsaraya.weather.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screens(val activeIcon: ImageVector, val inactiveIcon: ImageVector) {
    Home(
        activeIcon = Icons.Filled.Home,
        inactiveIcon = Icons.Outlined.Home
    ),
    Favorites(
        activeIcon = Icons.Filled.Favorite,
        inactiveIcon = Icons.Filled.FavoriteBorder
    ),
    Alerts(
        activeIcon = Icons.Filled.Notifications,
        inactiveIcon = Icons.Outlined.Notifications
    ),
    Settings(
        activeIcon = Icons.Filled.Settings,
        inactiveIcon = Icons.Outlined.Settings
    )
}
