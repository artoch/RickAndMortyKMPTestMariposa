package org.toch.rickmortytest.presentation.screen.characterdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.presentation.screen.characterdetail.observe.CharacterDetailObserver
import org.toch.rickmortytest.presentation.screen.characterdetail.preview.CharacterDetailStateParameterProvider
import org.toch.rickmortytest.presentation.utils.statusColor
import org.toch.rickmortytest.presentation.viewmodel.characterdetail.CharacterDetailState
import org.toch.rickmortytest.presentation.viewmodel.characterdetail.CharacterDetailViewModel
import rickandmortytest.shared.generated.resources.Res
import rickandmortytest.shared.generated.resources.character_detail_screen_gender
import rickandmortytest.shared.generated.resources.character_detail_screen_location
import rickandmortytest.shared.generated.resources.character_detail_screen_species

@Composable
fun CharacterDetailScreen() {

    val viewModel = koinViewModel<CharacterDetailViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    CharacterDetailObserver(viewModel)

    CharacterDetailContent(
        state = state,
        onNavigateBack = viewModel::navigateBack,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun CharacterDetailContent(
    state: CharacterDetailState,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CharacterDetailTopBar(
                title = state.character?.name.orEmpty(),
                onNavigateBack = onNavigateBack,
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            state.character?.let { character ->
                CharacterDetail(character = character)
            }
            state.errorMessage?.let { message ->
                Text(
                    message,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun CharacterDetailTopBar(
    title: String,
    onNavigateBack: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                color = MaterialTheme.colorScheme.onSurface,
                text = title,
                fontWeight = FontWeight.Bold,
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",//stringResource(Res.string.character_detail_screen_toolbar_back_navigation),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    )
}

@Composable
private fun CharacterDetail(character: Character) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.onSurface,
                            ),
                        ),
                    ).padding(24.dp),
            ) {
                Text(
                    text = character.name,
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(character.statusColor()),
                    )
                    Text(
                        text = "${character.status} - ${character.species}",
                        color = MaterialTheme.colorScheme.surface,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            InfoCard(
                icon = Icons.Default.Transgender,
                title = stringResource(Res.string.character_detail_screen_gender),
                description = character.gender,
            )
            InfoCard(
                icon = Icons.Default.Person,
                title = stringResource(Res.string.character_detail_screen_species),
                description = character.species,
            )
            InfoCard(
                icon = Icons.Default.LocationOn,
                title = stringResource(Res.string.character_detail_screen_location),
                description = character.location,
            )
        }
    }
}

@Composable
private fun InfoCard(
    icon: ImageVector,
    title: String,
    description: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 14.sp,
                )
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
fun CharacterDetailContainer(state: CharacterDetailState) {
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            if (state.character == null) {
                Text("Theres nothin to show")
            } else { //character is not null
                val character = state.character

                Box(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                ) {
                    AsyncImage(
                        model = character.image,
                        contentDescription = character.name,
                        modifier =
                            Modifier.fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop,
                    )

                    Box(
                        modifier = Modifier.matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colorStops = arrayOf(
                                        0.0f to Color.Transparent,
                                        0.5f to Color.Transparent,
                                        1.0f to Color.Black.copy(alpha = 0.75f)
                                    )
                                )
                            )
                    )

                    Text(
                        text = character.name,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }


                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(character.statusColor())
                        )

                        Text(
                            text = "${character.status} - ${character.species}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Gray
                            )
                        )

                    }

                    Text(
                        text = "Last known location:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Text(
                        text = character.location,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun CharacterDetailContentPreview(
    @PreviewParameter(CharacterDetailStateParameterProvider::class) state: CharacterDetailState
) {
    CharacterDetailContainer(state)
}