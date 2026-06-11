package org.toch.rickmortytest.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
actual fun AppTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean, // Se ignora en Desktop
    content: @Composable () -> Unit
) {
    // Usamos las paletas que ya tienes definidas en commonMain
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}