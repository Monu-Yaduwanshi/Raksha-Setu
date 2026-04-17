package com.example.rakshasetu.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Your specified color palette
val PurpleMain = Color(0xFF007F50)      // Deep Purple
val BlueMain = Color(0xFF5101FF)         // Vibrant Blue
val LavenderLight = Color(0xFFE4DFFC)    // Light Lavender
val MintLight = Color(0xFFDEF5EF)        // Light Mint
val TealMain = Color(0xFF38BEB4)         // Teal
val GreenMain = Color(0xFF0CB381)        // Green
val ForestGreen = Color(0xFF007F50)      // Dark Green
val SageGreen = Color(0xFFB7D6B5)        // Sage
val OrangeAccent = Color(0xFFFF6300)     // Orange

// Semantic colors using your palette
val Primary = PurpleMain
val PrimaryVariant = BlueMain
val Secondary = TealMain
val SecondaryVariant = GreenMain
val Accent = OrangeAccent

// Background colors
val Background = LavenderLight.copy(alpha = 0.3f)
val Surface = Color.White
val SurfaceVariant = MintLight.copy(alpha = 0.5f)

// Text colors
val TextPrimary = ForestGreen
val TextSecondary = TealMain
val TextHint = SageGreen

// Status colors
val Success = GreenMain
val Warning = OrangeAccent
val Error = Color(0xFFCE1A0B)  // Keep red for errors
val Info = BlueMain

// Button colors
val ButtonPrimary = PurpleMain
val ButtonSecondary = TealMain
val ButtonAccent = OrangeAccent

// Light color scheme
val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimaryVariant,
    onPrimaryContainer = Color.White,
    secondary = Secondary,
    onSecondary = Color.White,
    secondaryContainer = SecondaryVariant,
    onSecondaryContainer = Color.White,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    error = Error,
    onError = Color.White,
    surfaceTint = Primary
)

// Dark color scheme
val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimaryVariant,
    onPrimaryContainer = Color.White,
    secondary = Secondary,
    onSecondary = Color.White,
    secondaryContainer = SecondaryVariant,
    onSecondaryContainer = Color.White,
  //  background = ForestGreen.copy(alpha = 0.9f),
    background = Color(0xFFECD7B2),  // Your specified color
    onBackground = LavenderLight,
    surface = ForestGreen,
    onSurface = LavenderLight,
    error = Error,
    onError = Color.White,
    surfaceTint = Primary
)

@Composable
fun RakshaSetuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}