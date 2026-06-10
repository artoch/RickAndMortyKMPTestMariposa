package org.toch.rickmortytest.domain.model

data class CharacterPaging(
    val characters: List<Character>,
    val canLoadMore: Boolean,
)