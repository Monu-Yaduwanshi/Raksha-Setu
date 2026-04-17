//package com.example.rakshasetu.ui.theme
package com.example.surakshasetu.ui.theme

import androidx.compose.ui.graphics.Color

// Your specified color palette
val PurpleMain = Color(0xFF7E3EE3)      // Deep Purple
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
val Error = Color(0xFFD32F2F)  //
val Info = BlueMain

// Button colors
val ButtonPrimary = PurpleMain
val ButtonSecondary = TealMain
val ButtonAccent = OrangeAccent

// Additional UI colors
val CardBackground = Color.White
val DividerColor = SageGreen.copy(alpha = 0.3f)
val IconTint = PurpleMain
val SelectedTabColor = PurpleMain
val UnselectedTabColor = SageGreen