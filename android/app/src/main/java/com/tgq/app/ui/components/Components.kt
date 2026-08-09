package com.tgq.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tgq.app.data.MarketInfo
import com.tgq.app.ui.theme.BrandGradient2
import com.tgq.app.ui.theme.BrandGold
import com.tgq.app.ui.theme.BrandMagenta
import com.tgq.app.ui.theme.GoldGradient
import com.tgq.app.ui.theme.NightInk
import com.tgq.app.ui.theme.Success
import com.tgq.app.ui.theme.Surface
import com.tgq.app.ui.theme.Surface3
import com.tgq.app.ui.theme.TextMuted
import com.tgq.app.ui.theme.TextPrimary
import com.tgq.app.ui.theme.TextSecondary
import com.tgq.app.ui.theme.Danger
import com.tgq.app.ui.theme.Stroke

// ===== Gradient text =====
@Composable
fun GradientText(text: String, modifier: Modifier = Modifier, brush: Brush = BrandGradient2, fontSize: Int = 40, fontWeight: FontWeight = FontWeight.ExtraBold, letterSpacing: Int = 3) {
    val styled = buildAnnotatedString {
        withStyle(SpanStyle(brush = brush)) {
            append(text)
        }
    }
    Text(
        text = styled,
        modifier = modifier,
        fontSize = fontSize.sp,
        fontWeight = fontWeight,
        letterSpacing = letterSpacing.sp,
        textAlign = TextAlign.Center
    )
}

// ===== Buttons =====
@Composable
fun TqgButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Primary,
    enabled: Boolean = true
) {
    val brush = when (variant) {
        ButtonVariant.Primary -> BrandGradient2
        ButtonVariant.Gold -> GoldGradient
        ButtonVariant.Ghost -> null
    }
    val bg: androidx.compose.ui.graphics.Shape = RoundedCornerShape(14.dp)
    Box(
        modifier = modifier
            .clip(bg)
            .then(
                if (variant == ButtonVariant.Ghost) Modifier.border(1.dp, Stroke, bg)
                else Modifier
            )
            .then(
                if (brush != null) Modifier.background(brush) else Modifier.background(Surface3)
            )
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 18.dp, vertical = 13.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = if (variant == ButtonVariant.Ghost) TextPrimary else Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

enum class ButtonVariant { Primary, Gold, Ghost }

// ===== Chips =====
@Composable
fun TqgChip(text: String, active: Boolean = false, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    val shape = RoundedCornerShape(99.dp)
    val mod = if (onClick != null) modifier.clip(shape).clickable { onClick() } else modifier
    Box(
        modifier = mod
            .clip(shape)
            .then(if (active) Modifier.background(BrandGradient2) else Modifier.background(Surface3).border(1.dp, Stroke, shape))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = if (active) Color.White else TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ===== Status tag =====
enum class MarketStatus { LIVE, WAITING, DONE }

@Composable
fun StatusTag(status: MarketStatus) {
    val (color, label) = when (status) {
        MarketStatus.LIVE -> Success to "● Live"
        MarketStatus.WAITING -> BrandGold to "Menunggu"
        MarketStatus.DONE -> BrandMagenta to "Selesai"
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(99.dp))
            .background(color.copy(alpha = 0.14f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(label, color = color, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
    }
}

// ===== Confidence ring =====
@Composable
fun ConfidenceRing(fraction: Float, size: Dp = 58.dp, modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = 6.dp.toPx()
            val arcSize = Size(size.toPx() - stroke, size.toPx() - stroke)
            drawArc(
                color = Color.White.copy(alpha = 0.08f),
                startAngle = -90f, sweepAngle = 360f, useCenter = false,
                topLeft = Offset(stroke / 2, stroke / 2), size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(
                brush = BrandGradient2,
                startAngle = -90f, sweepAngle = 360f * fraction.coerceIn(0f, 1f), useCenter = false,
                topLeft = Offset(stroke / 2, stroke / 2), size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${(fraction * 100).toInt()}%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("konfiden", fontSize = 8.sp, color = TextMuted)
        }
    }
}

// ===== Card container =====
@Composable
fun TqgCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Surface)
            .border(1.dp, Stroke, RoundedCornerShape(20.dp))
    ) {
        content()
    }
}

// ===== Section header =====
@Composable
fun SectionHeader(title: String, action: String? = null, onAction: (() -> Unit)? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        if (action != null) {
            Text(
                action,
                fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                color = BrandGold,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(enabled = onAction != null) { onAction?.invoke() }
                    .padding(4.dp)
            )
        }
    }
}

// ===== Market row =====
@Composable
fun MarketRow(market: MarketInfo, onClick: () -> Unit) {
    val hasResult = market.latestResult.isNotBlank()
    val status = when {
        hasResult -> MarketStatus.DONE
        market.latestPeriod.isNotBlank() -> MarketStatus.WAITING
        else -> MarketStatus.LIVE
    }
    TqgCard(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(market.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(
                    if (market.latestPeriod.isNotBlank()) "P${market.latestPeriod}" else "menunggu hasil",
                    fontSize = 10.sp, color = TextMuted
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                StatusTag(status)
                Spacer(Modifier.height(4.dp))
                Text(
                    if (hasResult) market.latestResult else "— — — —",
                    fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = if (hasResult) TextPrimary else TextMuted
                )
            }
        }
    }
}

// ===== Bottom navigation =====
enum class Tab(val route: String, val label: String, val icon: ImageVector) {
    HOME("home", "Beranda", Icons.Rounded.Home),
    MARKETS("markets", "Pasaran", Icons.Rounded.GridView),
    INPUT("input", "Input", Icons.Rounded.AddCircleOutline),
    PROFILE("profile", "Profil", Icons.Rounded.Person)
}

@Composable
fun TqgBottomNav(current: Tab, onSelect: (Tab) -> Unit) {
    NavigationBar(
        containerColor = NightInk,
        tonalElevation = 0.dp,
        modifier = Modifier.border(1.dp, Stroke.copy(alpha = 0.6f), RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
    ) {
        Tab.entries.forEach { tab ->
            val selected = tab == current
            NavigationBarItem(
                selected = selected,
                onClick = { onSelect(tab) },
                icon = {
                    Icon(
                        tab.icon,
                        contentDescription = tab.label,
                        tint = if (selected) BrandMagenta else TextMuted,
                        modifier = Modifier
                            .size(22.dp)
                            .graphicsLayer(alpha = if (selected) 1f else 0.9f)
                    )
                },
                label = {
                    Text(tab.label, fontSize = 9.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = BrandMagenta,
                    unselectedTextColor = TextMuted,
                    indicatorColor = BrandMagenta.copy(alpha = 0.16f)
                )
            )
        }
    }
}

// ===== Loading / Error =====
@Composable
fun LoadingBlock(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
        Text("Memuat...", color = TextMuted, fontSize = 13.sp)
    }
}

@Composable
fun ErrorBlock(message: String, modifier: Modifier = Modifier, onRetry: (() -> Unit)? = null) {
    Column(
        modifier = modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(message, color = Danger, fontSize = 13.sp, textAlign = TextAlign.Center)
        if (onRetry != null) {
            TqgButton("Coba lagi", onClick = onRetry, variant = ButtonVariant.Ghost)
        }
    }
}

// ===== Soft gradient orb decoration =====
@Composable
fun GradientOrb(modifier: Modifier = Modifier, color: Color = BrandMagenta) {
    Box(
        modifier = modifier
            .graphicsLayer { alpha = 0.45f }
            .background(color, CircleShape)
    )
}
