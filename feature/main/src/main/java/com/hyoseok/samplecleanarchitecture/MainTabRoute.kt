package com.hyoseok.samplecleanarchitecture

import kotlinx.serialization.Serializable

sealed interface MainTabRoute{
    @Serializable
    data object Home : MainTabRoute

    @Serializable
    data object Setting : MainTabRoute

    @Serializable
    data object Bookmark : MainTabRoute
}