package org.toch.rickmortytest.presentation.viewmodel.localcharacter

import org.toch.rickmortytest.domain.model.Character

data class LocalCharacterState(
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    val errorMessage: String? = null
)