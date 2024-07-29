package com.hyoseok.samplecleanarchitecture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.navigation.NavDestination.Companion.hasRoute


internal class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = MainTab.HOME.route

    val currentTab: MainTab?
        @Composable get() = MainTab.find { tab ->
            currentDestination?.hasRoute(tab::class) == true
        }

    fun navigate(tab: MainTab) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (tab) {
            MainTab.SETTING -> navController.navigateSetting(navOptions)
            MainTab.HOME -> navController.navigateHome(navOptions)
            MainTab.BOOKMARK -> navController.navigateBookmark(navOptions)
        }
    }

    fun navigateContributor() {
        navController.navigateContributor()
    }

    fun navigateSession() {
        navController.navigateSession()
    }

    fun navigateSessionDetail(sessionId: String) {
        navController.navigateSessionDetail(sessionId)
    }

    private fun popBackStack() {
        navController.popBackStack()
    }

    fun popBackStackIfNotHome() {
        if (!isSameCurrentDestination<MainTabRoute.Home>()) {
            popBackStack()
        }
    }

    private inline fun <reified T : MainTabRoute> isSameCurrentDestination(): Boolean {
        return navController.currentDestination?.hasRoute<T>() == true
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
