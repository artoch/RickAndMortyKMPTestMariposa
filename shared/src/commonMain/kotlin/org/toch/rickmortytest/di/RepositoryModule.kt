package org.toch.rickmortytest.di

import org.koin.dsl.module
import org.toch.rickmortytest.data.repository.CharacterRepositoryImpl
import org.toch.rickmortytest.database.RickAndMortyDatabaseTest
import org.toch.rickmortytest.domain.repository.CharacterRepository

val repositoryModule = module {

    // 1. ⚠️ ESTO ES LO QUE TE FALTA: Decerle a Koin cómo crear la Base de Datos
    single<RickAndMortyDatabaseTest> {
        // Este get() buscará el 'SqlDriver' que provee cada plataforma (Android/iOS)
        RickAndMortyDatabaseTest(driver = get())
    }

    single<CharacterRepository> {
        CharacterRepositoryImpl(
            get(),
            get()
        )
    }
}