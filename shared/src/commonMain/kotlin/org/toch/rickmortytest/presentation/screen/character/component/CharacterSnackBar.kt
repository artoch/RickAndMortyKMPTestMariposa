package org.toch.rickmortytest.presentation.screen.character.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class SnackBarType {
    SUCCESS,
    DELETE
}

@Composable
fun CharacterSnackBar(
    message: String,
    type: SnackBarType,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (type) {
        SnackBarType.SUCCESS -> Color(0xFF4CAF50) // Verde
        SnackBarType.DELETE -> Color(0xFFE53935) // Rojo
    }

    Snackbar(
        modifier = modifier.padding(16.dp),
        containerColor = backgroundColor,
        contentColor = Color.White,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}