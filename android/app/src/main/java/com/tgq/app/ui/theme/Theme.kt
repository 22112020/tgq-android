package com.tgq.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Brand signature gradient (violet -> magenta -> gold)
val BrandGradient = Brush.linearGradient(
    listOf(BrandViolet, BrandMagenta, BrandGold)
)
val BrandGradient2 = Brush.linearGradient(listOf(BrandViolet, BrandMagenta))
val GoldGradient = Brush.linearGradient(listOf(Color(0xFFF59E0B), BrandMagenta))

@Composable
fun TqgTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val scheme = darkColorScheme(
        primary = BrandMagenta,
        onPrimary = Color.White,
        primaryContainer = Surface3,
        onPrimaryContainer = TextPrimary,
        secondary = BrandViolet,
        onSecondary = Color.White,
        background = NightInk,
        onBackground = TextPrimary,
        surface = Surface,
        onSurface = TextPrimary,
        surfaceVariant = SurfaceHigh,
        onSurfaceVariant = TextSecondary,
        error = Danger,
        onError = Color.White
    )
    MaterialTheme(
        colorScheme = scheme,
        typography = TgqTypography,
        content = content
    )
}
