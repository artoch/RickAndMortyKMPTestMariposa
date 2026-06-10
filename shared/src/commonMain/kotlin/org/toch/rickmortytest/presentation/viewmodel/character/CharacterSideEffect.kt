package org.toch.rickmortytest.presentation.viewmodel.character

sealed class CharacterSideEffect {
    data class NavigateToCharacterDetail(
        val id: Int,
    ) : CharacterSideEffect()

    data class ShowSnackBar(
        val message: String,
    ) : CharacterSideEffect()
}