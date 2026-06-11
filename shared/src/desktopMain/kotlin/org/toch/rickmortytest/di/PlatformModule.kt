package org.toch.rickmortytest.di

import org.toch.rickmortytest.data.local.DriverFactory

import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    // Provee la fábrica del driver de base de datos para escritorio
    single { DriverFactory() }
}