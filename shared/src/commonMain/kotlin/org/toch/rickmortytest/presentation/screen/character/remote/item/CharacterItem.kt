package org.toch.rickmortytest.presentation.screen.character.remote.item

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.presentation.utils.statusColor
import rickandmortytest.shared.generated.resources.Res
import rickandmortytest.shared.generated.resources.character_detail_screen_location_unknown

@Composable
fun CharacterItem(
    character: Character,
    onCharacterClick: (Int) -> Unit,
    onFavoriteClick: (Character) -> Unit,
    modifier: Modifier = Modifier
) {

    val scale by animateFloatAsState(
        targetValue = if (character.isFavorite) 1.2f else 1f,
        label = ""
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)

            .clickable { onCharacterClick(character.id) },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {

            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.4f to Color.Transparent,
                                0.7f to MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                1.0f to MaterialTheme.colorScheme.onSurface.copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            IconButton(
                onClick = { onFavoriteClick(character) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.35f))
            ) {
                Icon(
                    imageVector = if (character.isFavorite)
                        Icons.Default.Favorite
                    else
                        Icons.Default.FavoriteBorder,
                    tint = if (character.isFavorite)
                        Color.Red
                    else
                        Color.White,
                    modifier = Modifier.scale(scale),
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = character.name,
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = MaterialTheme.colorScheme.onSurface,
                            offset = Offset(2f, 2f),
                            blurRadius = 8f
                        )
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(character.statusColor())
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = character.status,
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = " • ${character.species}",
                        color = MaterialTheme.colorScheme.surface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(Res.string.character_detail_screen_location_unknown),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )

                Text(
                    text = character.location,
                    color = MaterialTheme.colorScheme.surface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}