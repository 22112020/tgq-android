package com.tgq.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
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
import com.tgq.app.ui.components.GradientOrb
import com.tgq.app.ui.components.TqgCard
import com.tgq.app.ui.theme.BrandMagenta
import com.tgq.app.ui.theme.BrandViolet
import com.tgq.app.ui.theme.NightInk
import com.tgq.app.ui.theme.TextMuted
import com.tgq.app.ui.theme.TextPrimary
import com.tgq.app.ui.theme.TextSecondary

@Composable
fun HokiScreen(vm: AppViewModel, onBack: () -> Unit) {
    val hoki = vm.ui.value.hoki

    LaunchedEffect(Unit) {
        vm.refreshHoki()
    }

    Box(Modifier.fillMaxSize().background(NightInk)) {
        GradientOrb(Modifier.align(Alignment.BottomStart).offset(x = (-50).dp, y = 60.dp).size(300.dp), BrandMagenta)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali", tint = TextPrimary)
                }
                Text("Prediksi Hoki", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.weight(1f))
                IconButton(onClick = { vm.refreshHoki() }) {
                    Icon(Icons.Rounded.Refresh, contentDescription = "Muat ulang", tint = TextSecondary)
                }
            }

            Spacer(Modifier.height(12.dp))

            if (hoki != null) {
                TqgCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("5D Utama Hari Ini", fontSize = 12.sp, color = TextMuted)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            hoki.main.forEach { d ->
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(BrandViolet.copy(alpha = 0.9f), BrandMagenta.copy(alpha = 0.9f))
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(d, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Backup:", fontSize = 11.sp, color = TextMuted)
                            hoki.backup.forEach { d ->
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(11.dp))
                                        .background(BrandMagenta.copy(alpha = 0.18f))
                                        .padding(0.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(d, color = BrandMagenta, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        ConfidenceRing(fraction = (hoki.confidence.toFloat() / 100f))
                        Text(
                            "Konfidensi ${hoki.confidence}%",
                            fontSize = 11.sp, color = TextSecondary
                        )
                        if (hoki.date.isNotBlank()) {
                            Text(
                                "Untuk tanggal ${hoki.date}" + if (hoki.cached) " · cache" else "",
                                fontSize = 10.sp, color = TextMuted
                            )
                        }
                    }
                }

                hoki.stats?.let { stats ->
                    Spacer(Modifier.height(16.dp))
                    TqgCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Statistik Analisis", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                StatBox("${stats.totalRecords}", "Data")
                                StatBox("${stats.markets}", "Pasaran")
                            }
                            if (stats.rankedTop10.isNotEmpty()) {
                                Spacer(Modifier.height(4.dp))
                                Text("Top 10 Digit", fontSize = 11.sp, color = TextMuted)
                                Text(
                                    stats.rankedTop10.joinToString("  ·  "),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            } else {
                TqgCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = BrandViolet, modifier = Modifier.size(28.dp))
                        Spacer(Modifier.height(10.dp))
                        Text("Menghitung prediksi hoki...", fontSize = 13.sp, color = TextMuted)
                    }
                }
            }
            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
private fun StatBox(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
        Text(label, fontSize = 10.sp, color = TextMuted)
    }
}
