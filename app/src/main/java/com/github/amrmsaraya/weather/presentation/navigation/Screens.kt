package com.github.amrmsaraya.weather.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.amrmsaraya.weather.R

sealed class Screens(
    val route: String,
    @StringRes val stringId: Int,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
) {
    object Home : Screens(
        route = "home",
        stringId = R.string.home,
        activeIcon = Icons.Filled.Home,
        inactiveIcon = Icons.Outlined.Home
    )

    object Favorites : Screens(
        route = "favourites",
        stringId = R.string.favorites,
        activeIcon = Icons.Filled.Favorite,
        inactiveIcon = Icons.Outlined.FavoriteBorder
    )

    object Alerts : Screens(
        route = "alerts",
        stringId = R.string.alerts,
        activeIcon = Icons.Filled.Notifications,
        inactiveIcon = Icons.Outlined.Notifications
    )

    object Settings : Screens(
        route = "settings",
        stringId = R.string.settings,
        activeIcon = Icons.Filled.Settings,
        inactiveIcon = Icons.Outlined.Settings
    )

    object Maps : Screens(
        route = "map",
        stringId = R.string.map,
        activeIcon = Icons.Filled.Map,
        inactiveIcon = Icons.Outlined.Map
    )
}
