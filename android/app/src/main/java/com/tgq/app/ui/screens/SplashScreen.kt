package com.tgq.app.ui.screens

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tgq.app.ui.components.GradientOrb
import com.tgq.app.ui.components.GradientText
import com.tgq.app.ui.theme.BrandGradient
import com.tgq.app.ui.theme.BrandMagenta
import com.tgq.app.ui.theme.BrandViolet
import com.tgq.app.ui.theme.NightInk
import com.tgq.app.ui.theme.TextMuted
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onDone: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "orb")
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4000))
    )

    Box(
        modifier = Modifier.fillMaxSize().background(BrandViolet.copy(alpha = 0.04f))
    ) {
        Box(Modifier.fillMaxSize().background(NightInk))
        GradientOrb(Modifier.align(Alignment.TopStart).offset(x = (-40).dp, y = (-30).dp).size(240.dp), BrandMagenta)
        GradientOrb(Modifier.align(Alignment.BottomEnd).offset(x = 40.dp, y = 60.dp).size(300.dp), BrandViolet)

        Column(
            modifier = Modifier.align(Alignment.Center).padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(104.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(BrandGradient)
                    .scale(0.92f + 0.08f * drift),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "A",
                    color = Color.White,
                    fontSize = 54.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            GradientText("TGQ", fontSize = 48, letterSpacing = 6)
            Text(
                "Luna Core",
                color = TextMuted,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 4.sp
            )
            Spacer(Modifier.height(8.dp))
            CircularProgressIndicator(
                color = BrandMagenta,
                strokeWidth = 3.dp,
                modifier = Modifier.size(26.dp)
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(1500)
        onDone()
    }
}
