package com.hyoseok.samplecleanarchitecture

import androidx.compose.runtime.Composable
import okhttp3.Route

internal enum class MainTab(
    val iconResId: Int,
    internal val contentDescription: String,
    val route: MainTabRoute,
) {
    SETTING(
        iconResId = R.drawable.ic_setting,
        contentDescription = "홈",
        MainTabRoute.Setting,
    ),
    HOME(
        iconResId = R.drawable.ic_home,
        contentDescription = "촬영",
        MainTabRoute.Home
    ),
    BOOKMARK(
        iconResId = R.drawable.ic_bookmark,
        contentDescription = "북마크",
        MainTabRoute.Bookmark,
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (MainTabRoute) -> Boolean): MainTab? {
            return entries.find { predicate(it.route) }
        }

        @Composable
        fun contains(predicate: @Composable (Route) -> Boolean): Boolean {
            return entries.map { it.route }.any { predicate(it) }
        }
    }
}
