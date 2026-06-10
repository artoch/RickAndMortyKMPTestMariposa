package org.toch.rickmortytest.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ==========================================
// PALETA DE COLORES (Inspiración Rick & Morty / Sci-Fi)
// ==========================================

// Verdes Portal y Fluorescentes
val PortalGreen = Color(0xFF00FF66)      // El clásico verde brillante del portal
val DarkPortalGreen = Color(0xFF00A340)  // Verde más oscuro para contrastes

// Tonos Interdimensionales (Púrpuras/Magentas)
val DimensionPurple = Color(0xFF9D4EDD)  // Púrpura de fluidos alienígenas
val SpaceCyan = Color(0xFF00F0FF)        // Azul de pantallas holográficas

// Fondos y Superficies (Modo Oscuro - Espacio Profundo)
val DeepSpaceBlack = Color(0xFF0B0C10)   // Fondo casi negro
val ShipMetalGray = Color(0xFF1F2833)    // Gris de la nave de Rick (Superficies)

// Fondos y Superficies (Modo Claro - Laboratorio)
val LabWhite = Color(0xFFF4F6F9)         // Blanco frío/azulado de laboratorio
val LabConsoleGray = Color(0xFFE2E8F0)   // Gris claro para tarjetas


// ==========================================
// CONFIGURACIÓN DE LOS COLOR SCHEMES
// ==========================================

val LightColorScheme = lightColorScheme(
    primary = DarkPortalGreen,          // Verde legible en fondos claros
    secondary = DimensionPurple,        // Púrpura para elementos secundarios
    tertiary = SpaceCyan,               // Azul holográfico para detalles/acentos
    background = LabWhite,              // Fondo de la app
    surface = Color.White,              // Tarjetas y menús
    onPrimary = Color.White,            // Texto sobre el verde principal
    onSecondary = Color.White,          // Texto sobre el púrpura
    onBackground = DeepSpaceBlack,      // Texto principal de la app
    onSurface = DeepSpaceBlack          // Texto sobre tarjetas
)

val DarkColorScheme = darkColorScheme(
    primary = PortalGreen,              // ¡Verde neón brillante en la oscuridad!
    secondary = SpaceCyan,              // El cian resalta increíble en modo oscuro
    tertiary = DimensionPurple,         // Púrpura para botones terciarios o estados
    background = DeepSpaceBlack,         // Fondo del espacio
    surface = ShipMetalGray,            // Tarjetas color metal de la nave
    onPrimary = DeepSpaceBlack,         // Texto negro sobre el verde brillante para que se lea bien
    onSecondary = DeepSpaceBlack,       // Texto negro sobre el cian brillante
    onBackground = Color(0xFFC5C6C7),   // Texto gris claro para que no canse la vista
    onSurface = Color.White             // Texto blanco en las tarjetas
)

@Composable
expect fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Por defecto activado
    content: @Composable () -> Unit
)