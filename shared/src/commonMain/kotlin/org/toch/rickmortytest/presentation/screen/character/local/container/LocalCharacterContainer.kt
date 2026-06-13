package org.toch.rickmortytest.presentation.screen.character.local.container

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.presentation.screen.character.remote.item.CharacterItem
import org.toch.rickmortytest.presentation.utils.ui.SearchBarComponent
import org.toch.rickmortytest.presentation.viewmodel.localcharacter.LocalCharacterState
import rickandmortytest.shared.generated.resources.Res
import rickandmortytest.shared.generated.resources.local_character_screen_title

@Composable
fun LocalCharacterContainer(
    state: LocalCharacterState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCharacterClick: (Int) -> Unit,
    onRemoveFavorite: (Character) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(

                title = {
                    Column {
                        Text(
                            stringResource(Res.string.local_character_screen_title),
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))

                        if ((searchQuery.isNotEmpty() && state.characters.isEmpty()) || state.characters.isNotEmpty()) {
                            SearchBarComponent(
                                searchQuery = searchQuery,
                                onSearchQueryChange = onSearchQueryChange
                            )
                        }
                    }


                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                if (!state.isLoading && state.characters.isEmpty()) {
                    val emptyMessage = if (searchQuery.isNotEmpty()) {
                        "No se encontraron personajes que coincidan con \"$searchQuery\"."
                    } else {
                        "No tienes personajes guardados en la caché local."
                    }

                    Text(
                        text = emptyMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                // Render de la lista local (Ya no lleva el SearchBar adentro como item)
                if (!state.isLoading && state.characters.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.characters,
                            key = { it.id }
                        ) { character ->
                            CharacterItem(
                                character = character,
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
    }
}