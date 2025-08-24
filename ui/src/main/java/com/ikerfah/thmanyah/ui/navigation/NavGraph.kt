package com.ikerfah.thmanyah.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.ikerfah.thmanyah.ui.home.HomeScreen

sealed class NavigationRoute {
    data object HomeNavigationRoute : NavigationRoute()
}

@Composable
fun AppNavigation() {
    val backStack = remember { mutableStateListOf<NavigationRoute>(NavigationRoute.HomeNavigationRoute) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is NavigationRoute.HomeNavigationRoute -> NavEntry(key) {
                    HomeScreen()
                }
            }
        }
    )
}