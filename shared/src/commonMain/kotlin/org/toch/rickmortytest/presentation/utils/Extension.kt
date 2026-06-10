package org.toch.rickmortytest.presentation.utils

import androidx.compose.ui.graphics.Color
import org.toch.rickmortytest.domain.model.Character

fun Character.statusColor(): Color =
    when (this.status.lowercase()) {
        "alive" -> Color.Green
        "dead" -> Color.Red
        else -> Color.Gray
    }