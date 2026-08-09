package com.tgq.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

val TgqTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 40.sp, fontWeight = FontWeight.ExtraBold,
        letterSpacing = (-1).sp, lineHeight = 48.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 28.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp
    ),
    titleLarge = TextStyle(
        fontSize = 20.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
    bodyMedium = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal),
    labelSmall = TextStyle(
        fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.2.sp
    )
)

// Large tabular number style for predictions & results
val NumberStyle = TextStyle(
    fontSize = 44.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = 3.sp,
    textAlign = TextAlign.Center,
    lineHeight = 52.sp
)

val NumberStyleLarge = TextStyle(
    fontSize = 72.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = 6.sp,
    textAlign = TextAlign.Center,
    lineHeight = 80.sp
)
