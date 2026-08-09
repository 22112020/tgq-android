package com.tgq.app.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tgq.app.ui.AppViewModel
import com.tgq.app.ui.components.ConfidenceRing
import com.tgq.app.ui.components.GradientText
import com.tgq.app.ui.components.SectionHeader
import com.tgq.app.ui.components.TqgCard
import com.tgq.app.ui.theme.BrandGradient2
import com.tgq.app.ui.theme.BrandMagenta
import com.tgq.app.ui.theme.BrandViolet
import com.tgq.app.ui.theme.GoldGradient
import com.tgq.app.ui.theme.Surface
import com.tgq.app.ui.theme.SurfaceHigh
import com.tgq.app.ui.theme.Stroke
import com.tgq.app.ui.theme.TextMuted
import com.tgq.app.ui.theme.TextPrimary
import com.tgq.app.ui.theme.TextSecondary

@Composable
fun HomeScreen(vm: AppViewModel, onOpenHoki: () -> Unit, onOpenMarkets: () -> Unit) {
    val ui = vm.ui

    LaunchedEffect(Unit) {
        if (ui.value.hoki == null) vm.refreshAll()
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Selamat datang kembali,", color = TextMuted, fontSize = 12.sp)
                Text(
                    ui.value.username.ifBlank { "Admin" },
                    color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = { vm.refreshAll() }) {
                Icon(Icons.Rounded.Refresh, contentDescription = "Muat ulang", tint = TextSecondary)
            }
        }

        // ===== Hoki hero card =====
        val hoki = ui.value.hoki
        TqgCard(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Brush.verticalGradient(listOf(BrandViolet.copy(alpha = 0.22f), Surface)))
                )
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = BrandMagenta, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Prediksi Hoki", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        ConfidenceRing(fraction = (hoki?.confidence ?: 0.0).toFloat() / 100f)
                    }

                    if (hoki != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("5D Utama", fontSize = 10.sp, color = TextMuted)
                                Text(
                                    hoki.main.joinToString(" "),
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextPrimary,
                                    letterSpacing = 2.sp
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Backup", fontSize = 10.sp, color = TextMuted)
                                Text(
                                    hoki.backup.joinToString(" "),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandMagenta
                                )
                            }
                        }
                        Text(
                            "Analisis ${hoki.stats?.totalRecords ?: 0} data dari ${hoki.stats?.markets ?: 0} pasaran",
                            fontSize = 11.sp, color = TextMuted
                        )
                    } else {
                        Text("Memuat prediksi hoki...", fontSize = 13.sp, color = TextMuted)
                    }
                }
            }
        }

        // ===== Quick actions =====
        SectionHeader("Akses Cepat")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickCard(
                "Prediksi Hoki",
                Icons.Rounded.AutoAwesome,
                BrandGradient2,
                modifier = Modifier.weight(1f),
                onClick = onOpenHoki
            )
            QuickCard(
                "Input Pasaran",
                Icons.Rounded.EditNote,
                GoldGradient,
                modifier = Modifier.weight(1f),
                onClick = onOpenMarkets
            )
        }

        // ===== Latest markets =====
        SectionHeader("Pasaran Terkini")
        ui.value.markets.take(5).forEach { m ->
            TqgCard(modifier = Modifier.fillMaxWidth().clickable(onClick = onOpenMarkets)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(m.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(
                                if (m.latestPeriod.isNotBlank()) "P${m.latestPeriod}" else "menunggu",
                                fontSize = 10.sp, color = TextMuted
                            )
                        }
                        Text(
                            m.latestResult.ifBlank { "— — — —" },
                            fontSize = 15.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp,
                            color = if (m.latestResult.isNotBlank()) TextPrimary else TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    brush: Brush,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(SurfaceHigh)
            .clickable { onClick() }
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(brush),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
}
