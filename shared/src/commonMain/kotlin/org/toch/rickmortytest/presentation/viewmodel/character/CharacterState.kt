package org.toch.rickmortytest.presentation.viewmodel.character

import org.toch.rickmortytest.domain.model.Character

data class CharacterState(
    val isLoading: Boolean = false,
    val isLoadingNextPage: Boolean = false,
    val characters: List<Character> = emptyList(),
    val canLoadMore: Boolean = true,
    val page: Int = 1,
    val errorMessage: String? = null,
)