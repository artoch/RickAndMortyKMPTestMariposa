package org.toch.rickmortytest.presentation.screen.character.remote.observer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.toch.rickmortytest.presentation.navigation.GlobalNavController
import org.toch.rickmortytest.presentation.navigation.Screen
import org.toch.rickmortytest.presentation.viewmodel.character.CharacterSideEffect
import org.toch.rickmortytest.presentation.viewmodel.character.CharacterViewModel
import org.toch.rickmortytest.presentation.viewmodel.viewmodelbehavior.CharacterEffectProvider

@Composable
fun CharacterObserver(
    viewModel: CharacterEffectProvider,
    showSnackBar: (String) -> Unit,
) {
    val navController = GlobalNavController.current

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is CharacterSideEffect.NavigateToCharacterDetail -> {
                    navController.navigate(Screen.CharacterDetail(sideEffect.id))
                }

                is CharacterSideEffect.ShowSnackBar -> {
                    showSnackBar(sideEffect.message)
                }
            }
        }
    }
}