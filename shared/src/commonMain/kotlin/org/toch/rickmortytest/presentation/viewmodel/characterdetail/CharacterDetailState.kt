package org.toch.rickmortytest.presentation.viewmodel.characterdetail

import org.toch.rickmortytest.domain.model.Character

data class CharacterDetailState(
    val isLoading: Boolean = false,
    val character: Character? = null,
    val errorMessage: String? = null,
)