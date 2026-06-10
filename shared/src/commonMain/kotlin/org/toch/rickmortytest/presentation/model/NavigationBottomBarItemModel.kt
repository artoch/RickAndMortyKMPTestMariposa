package org.toch.rickmortytest.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector
import org.toch.rickmortytest.presentation.navigation.Screen

data class NavigationBottomBarItemModel (
    val icon: ImageVector,
    val label: String,
    val route: Screen
)