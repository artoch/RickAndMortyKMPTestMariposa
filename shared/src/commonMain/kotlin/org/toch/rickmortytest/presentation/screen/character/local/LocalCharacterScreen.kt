package org.toch.rickmortytest.presentation.screen.character.local

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.toch.rickmortytest.presentation.screen.character.local.container.LocalCharacterContainer
import org.toch.rickmortytest.presentation.screen.character.remote.observer.CharacterObserver
import org.toch.rickmortytest.presentation.viewmodel.localcharacter.LocalCharacterViewModel

@Composable
fun LocalCharacterScreen(showSnackBar: (String) -> Unit) {

    val viewModel = koinViewModel<LocalCharacterViewModel>()


    val state by viewModel.state.collectAsStateWithLifecycle()


    CharacterObserver(viewModel) {
        showSnackBar(it)
     }

    LocalCharacterContainer(
        state = state,
        onCharacterClick = viewModel::onCharacterClicked,
        onRemoveFavorite = { character -> viewModel.removeFromFavorites(character) }
    )
}