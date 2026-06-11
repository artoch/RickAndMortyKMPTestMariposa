package org.toch.rickmortytest.presentation.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.toch.rickmortytest.presentation.navigation.GlobalNavController
import org.toch.rickmortytest.presentation.navigation.Screen
import org.toch.rickmortytest.presentation.viewmodel.character.CharacterViewModel
import org.toch.rickmortytest.presentation.viewmodel.splash.SplashViewModel
import rickandmortytest.shared.generated.resources.Res
import rickandmortytest.shared.generated.resources.bg_rickmorty

@Composable
fun SplashScreen() {

    val navController = GlobalNavController.current

    val viewModel = koinViewModel<SplashViewModel>()

    val isReady by viewModel.isReady.collectAsState()

    LaunchedEffect(isReady) {
        if (isReady) {
            navController.navigate(Screen.Home)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(Res.drawable.bg_rickmorty),
            contentDescription = "Splash Background",
            modifier = Modifier.fillMaxSize(),

            contentScale = ContentScale.Crop
        )

    }
}