package org.toch.rickmortytest.presentation.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController


val GlobalNavController = compositionLocalOf<NavController> { error("No NavController found!") }