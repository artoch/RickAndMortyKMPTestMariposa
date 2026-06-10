package org.toch.rickmortytest.presentation.viewmodel.viewmodelbehavior

import kotlinx.coroutines.flow.Flow
import org.toch.rickmortytest.presentation.viewmodel.character.CharacterSideEffect

interface CharacterEffectProvider {
    val sideEffect: Flow<CharacterSideEffect>
}