package org.toch.rickmortytest.presentation.screen.characterdetail.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.presentation.viewmodel.characterdetail.CharacterDetailState

class CharacterDetailStateParameterProvider(override val values: Sequence<CharacterDetailState> = sequenceOf(
    CharacterDetailState(
        isLoading = true,
        character = null
    ),
    CharacterDetailState(
        isLoading = false,
        character = Character(
            1,
            "Rick chancez",
            "Alive",
            "Human",
            "Human",
            "Male",
            "Earth",
            "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        )
    ),
)) : PreviewParameterProvider<CharacterDetailState> {


}
