package org.toch.rickmortytest.presentation.screen.characterdetail.observe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.toch.rickmortytest.presentation.navigation.GlobalNavController
import org.toch.rickmortytest.presentation.viewmodel.characterdetail.CharacterDetailSideEffect
import org.toch.rickmortytest.presentation.viewmodel.characterdetail.CharacterDetailViewModel

@Composable
fun CharacterDetailObserver(viewModel: CharacterDetailViewModel) {
    val navController = GlobalNavController.current

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                CharacterDetailSideEffect.NavigateBack -> navController.popBackStack()
            }
        }
    }
}