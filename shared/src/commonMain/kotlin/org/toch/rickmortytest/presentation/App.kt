package org.toch.rickmortytest.presentation


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import org.toch.rickmortytest.presentation.navigation.GlobalNavController
import org.toch.rickmortytest.presentation.navigation.Screen
import org.toch.rickmortytest.presentation.screen.characterdetail.CharacterDetailScreen
import org.toch.rickmortytest.presentation.screen.home.HomeScreen
import org.toch.rickmortytest.presentation.screen.splash.SplashScreen
import org.toch.rickmortytest.theme.AppTheme


@Composable
@Preview
fun App() {
    AppTheme {

        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .components {
                    add(KtorNetworkFetcherFactory())
                }
                .build()
        }

        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background // Usa el color de fondo galáctico
        ) {
            CompositionLocalProvider(GlobalNavController provides navController) {

                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash
                ) {
                    composable<Screen.Splash> {
                        SplashScreen()
                    }
                    composable<Screen.Home> {
                        HomeScreen()
                    }
                    composable<Screen.CharacterDetail> {
                        CharacterDetailScreen()
                    }
                }

            }
        }

    }
}