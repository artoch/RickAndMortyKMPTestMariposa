package org.toch.rickmortytest

import androidx.compose.ui.window.ComposeUIViewController
import org.toch.rickmortytest.di.initKoin
import org.toch.rickmortytest.presentation.App

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin {  }
    }
) { App() }