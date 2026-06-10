package org.toch.rickmortytest.presentation.screen.character.remote.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.presentation.screen.character.component.CharacterItemSkeleton
import org.toch.rickmortytest.presentation.screen.character.remote.item.CharacterItem

@Composable
fun CharacterContainer(
    lazyCharacters: LazyPagingItems<Character>,
    favoriteOverrides: Map<Int, Boolean> = emptyMap(),
    onFavoriteClick: (Character) -> Unit,
    onCharacterClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // 1. Carga de la primera página completa
        if (lazyCharacters.loadState.refresh is LoadState.Loading) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(5) {
                    CharacterItemSkeleton()
                }
            }
            return
        }

        if (lazyCharacters.loadState.refresh is LoadState.Error) {
            val error = lazyCharacters.loadState.refresh as LoadState.Error

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        color = MaterialTheme.colorScheme.primary,
                        text = error.error.message ?: "Ocurrió un error al cargar los personajes."
                    )

                    Button(
                        onClick = {
                            lazyCharacters.retry()
                        }
                    ) {
                        Text("Reintentar")
                    }
                }
            }

            return
        }

        // 2. Render de la lista
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = lazyCharacters.itemCount,
                key = lazyCharacters.itemKey { it.id }
            ) { index ->
                val character = lazyCharacters[index]
                character?.let {
                    val isFav: Boolean = favoriteOverrides[it.id] ?: it.isFavorite
                    val displayCharacter = it.copy(
                        isFavorite = isFav
                    )
                    CharacterItem(
                        character = displayCharacter,
                        modifier = Modifier.animateItem(),
                        onCharacterClick = { id -> onCharacterClick(id) },
                        onFavoriteClick = { item -> onFavoriteClick(item) }
                    )
                }
            }

            // 3. Spinner al final de la lista cuando está paginando la siguiente hoja
            if (lazyCharacters.loadState.append is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
