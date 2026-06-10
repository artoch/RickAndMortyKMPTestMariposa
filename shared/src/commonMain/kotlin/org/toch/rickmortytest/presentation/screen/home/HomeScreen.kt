package org.toch.rickmortytest.presentation.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.toch.rickmortytest.presentation.model.NavigationBottomBarItemModel
import org.toch.rickmortytest.presentation.navigation.Screen
import org.toch.rickmortytest.presentation.screen.character.component.CharacterSnackBar
import org.toch.rickmortytest.presentation.screen.character.local.LocalCharacterScreen
import org.toch.rickmortytest.presentation.screen.character.remote.CharacterScreen
import org.toch.rickmortytest.theme.AppTheme

@Composable
fun HomeScreen() {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = navBackStackEntry?.destination

    val snackBarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    val items = listOf(
        NavigationBottomBarItemModel(
            icon = Icons.Default.Person,
            label = "Character",//stringResource(Res.string.home_screen_characters),
            route = Screen.Characters
        ),
        NavigationBottomBarItemModel(
            icon = Icons.Filled.FavoriteBorder,
            label = "Saved",//stringResource(Res.string.home_screen_locations),
            route = Screen.Saved
        ),
    )



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true,
                        label = {
                            Text(item.label)
                        },
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                item.icon, contentDescription = null
                            )
                        }
                    )
                }
            }

        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Characters,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Screen.Characters> {
                CharacterScreen { message ->
                    scope.launch {
                        snackBarHostState.showSnackbar(message)
                    }
                }
            }
            composable<Screen.Saved> {
                LocalCharacterScreen { message ->
                    scope.launch {
                        snackBarHostState.showSnackbar(message)
                    }
                }
            }
        }
    }
}