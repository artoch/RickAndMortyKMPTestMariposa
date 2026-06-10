package org.toch.rickmortytest.presentation.viewmodel.characterdetail

sealed class CharacterDetailSideEffect {
    data object NavigateBack : CharacterDetailSideEffect()
}