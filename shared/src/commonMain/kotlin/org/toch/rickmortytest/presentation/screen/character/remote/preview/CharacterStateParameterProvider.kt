package org.toch.rickmortytest.presentation.screen.character.remote.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.paging.PagingData
import kotlinx.coroutines.flow.flowOf
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.presentation.viewmodel.character.CharacterState

class CharacterStateParameterProvider(
    override val values: Sequence<PagingData<Character>> = sequenceOf(
        PagingData.from(
            listOf(
                Character(
                    id = 1,
                    name = "Rick",
                    status = "Alive",
                    "",
                    "",
                    "",
                    "",
                    image = ""
                ),
                Character(
                    id = 2,
                    name = "Morty",
                    status = "Alive",
                    "",
                    "",
                    "",
                    "",
                    image = ""
                )
            )
        )
    )
) : PreviewParameterProvider<PagingData<Character>>