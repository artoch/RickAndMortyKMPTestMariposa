package org.toch.rickmortytest.presentation.screen.character.remote

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf
import org.koin.compose.viewmodel.koinViewModel
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.presentation.screen.character.remote.container.CharacterContainer
import org.toch.rickmortytest.presentation.screen.character.remote.observer.CharacterObserver
import org.toch.rickmortytest.presentation.screen.character.remote.preview.CharacterStateParameterProvider
import org.toch.rickmortytest.presentation.viewmodel.character.CharacterViewModel

@Composable
fun CharacterScreen(showSnackBar: (String) -> Unit) {
    val viewModel = koinViewModel<CharacterViewModel>()

    val lazyCharacters = viewModel.charactersFlow.collectAsLazyPagingItems()

    val favoriteOverrides by viewModel.favoriteOverrides.collectAsState()

    CharacterObserver(viewModel) { sideEffect ->
        showSnackBar(sideEffect)
    }

    CharacterContainer(
        lazyCharacters = lazyCharacters,
        favoriteOverrides = favoriteOverrides,
        onCharacterClick = viewModel::onCharacterClicked,
        onFavoriteClick = { character ->
            viewModel.toggleFavorite(character)
        }
    )
}


@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun CharacterDetailContentPreview(
    @PreviewParameter(CharacterStateParameterProvider::class)
    pagingData: PagingData<Character>
) {
    CharacterContainer(
        lazyCharacters = flowOf(pagingData).collectAsLazyPagingItems(),
        onCharacterClick = {},
        onFavoriteClick = {}
    )
}