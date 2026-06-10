package org.toch.rickmortytest.presentation.screen.character.local.container

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.presentation.screen.character.remote.item.CharacterItem
import org.toch.rickmortytest.presentation.viewmodel.localcharacter.LocalCharacterState

@Composable
fun LocalCharacterContainer(
    state: LocalCharacterState,
    onCharacterClick: (Int) -> Unit,
    onRemoveFavorite: (Character) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Estado de carga inicial
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Estado de pantalla vacía (Por si el usuario no tiene favoritos guardados)
        if (!state.isLoading && state.characters.isEmpty()) {
            Text(
                text = "No tienes personajes guardados en la caché local.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Center).padding(16.dp)
            )
        }

        // Render de la lista local (Clásica, sin Paging 3)
        if (state.characters.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = state.characters,
                    key = { it.id }
                ) { character ->
                    CharacterItem(
                        character = character,
                        // animateItem() anima la salida del item (fade + shrink)
                        // y el reordenamiento de los restantes de forma automática
                        modifier = Modifier.animateItem(
                            fadeOutSpec = tween(durationMillis = 300),
                            placementSpec = tween(durationMillis = 300)
                        ),
                        onCharacterClick = onCharacterClick,
                        onFavoriteClick = { onRemoveFavorite(character) }
                    )
                }
            }
        }
    }
}