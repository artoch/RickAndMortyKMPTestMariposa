package org.toch.rickmortytest.presentation.screen.character.remote.container

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import org.jetbrains.compose.resources.stringResource
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.presentation.screen.character.component.CharacterItemSkeleton
import org.toch.rickmortytest.presentation.screen.character.remote.item.CharacterItem
import org.toch.rickmortytest.presentation.utils.ui.SearchBarComponent
import rickandmortytest.shared.generated.resources.Res
import rickandmortytest.shared.generated.resources.character_screen_title
import rickandmortytest.shared.generated.resources.local_character_screen_title
import rickandmortytest.shared.generated.resources.retry


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterContainer(
    lazyCharacters: LazyPagingItems<Character>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    favoriteOverrides: Map<Int, Boolean> = emptyMap(),
    onFavoriteClick: (Character) -> Unit,
    onCharacterClick: (Int) -> Unit
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
                            stringResource(Res.string.character_screen_title),
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))

                        SearchBarComponent(
                            searchQuery = searchQuery,
                            onSearchQueryChange = onSearchQueryChange
                        )
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            when (lazyCharacters.loadState.refresh) {
                is LoadState.Loading -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(5) {
                            CharacterItemSkeleton()
                        }
                    }
                }

                // Estado de error en la primera carga
                is LoadState.Error -> {
                    val error = lazyCharacters.loadState.refresh as LoadState.Error
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
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
                                Text(stringResource(Res.string.retry))
                            }
                        }
                    }
                }

                // 2. Render de la lista principal
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp),
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
        }
    }
}