package org.toch.rickmortytest.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.toch.rickmortytest.presentation.viewmodel.character.CharacterViewModel
import org.toch.rickmortytest.presentation.viewmodel.characterdetail.CharacterDetailViewModel
import org.toch.rickmortytest.presentation.viewmodel.localcharacter.LocalCharacterViewModel

val viewModelModule = module {
    viewModel { CharacterViewModel(get()) }
    viewModel {
        CharacterDetailViewModel(
            characterRepository = get(),
            savedStateHandle = get(),
        )
    }
    viewModel { LocalCharacterViewModel(get()) }
}