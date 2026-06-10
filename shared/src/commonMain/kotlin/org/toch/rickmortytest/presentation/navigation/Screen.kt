package org.toch.rickmortytest.presentation.navigation

import kotlinx.serialization.Serializable
sealed class Screen {

    @Serializable
    object Home : Screen()

    @Serializable
    data class CharacterDetail(val id: Int): Screen()
    @Serializable
    object Characters: Screen()

    @Serializable
    object Saved: Screen()

}