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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tgq.app.data.MarketInfo
import com.tgq.app.ui.AppViewModel
import com.tgq.app.ui.components.ButtonVariant
import com.tgq.app.ui.components.ConfidenceRing
import com.tgq.app.ui.components.GradientOrb
import com.tgq.app.ui.components.StatusTag
import com.tgq.app.ui.components.TqgButton
import com.tgq.app.ui.components.TqgCard
import com.tgq.app.ui.theme.BrandGradient2
import com.tgq.app.ui.theme.BrandMagenta
import com.tgq.app.ui.theme.BrandViolet
import com.tgq.app.ui.theme.Danger
import com.tgq.app.ui.theme.NightInk
import com.tgq.app.ui.theme.Stroke
import com.tgq.app.ui.theme.TextMuted
import com.tgq.app.ui.theme.TextPrimary

@Composable
fun MarketDetailScreen(vm: AppViewModel, market: MarketInfo, onBack: () -> Unit) {
    val ui by vm.ui.collectAsState()

    Box(Modifier.fillMaxSize().background(NightInk)) {
        GradientOrb(Modifier.align(Alignment.TopEnd).offset(x = 80.dp, y = (-50).dp).size(260.dp), BrandViolet)

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
                Text(market.name, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(12.dp))

            TqgCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StatusTag(
                        if (market.latestResult.isNotBlank()) com.tgq.app.ui.components.MarketStatus.DONE
                        else com.tgq.app.ui.components.MarketStatus.WAITING
                    )
                    Spacer(Modifier.height(14.dp))
                    Text(
                        market.latestResult.ifBlank { "— — — —" },
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 3.sp,
                        color = if (market.latestResult.isNotBlank()) TextPrimary else TextMuted
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        if (market.latestPeriod.isNotBlank()) "Periode ${market.latestPeriod}" else "Belum ada hasil",
                        fontSize = 12.sp, color = TextMuted
                    )
                    if (market.lastUpdated.isNotBlank()) {
                        Spacer(Modifier.height(4.dp))
                        Text("Diperbarui ${market.lastUpdated}", fontSize = 10.sp, color = TextMuted)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            TqgButton(
                text = if (ui.loading) "Menghitung..." else "Prediksi Pasaran",
                onClick = { vm.predict(market.name) },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            )

            ui.error?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = Danger, fontSize = 12.sp)
            }

            val pred = ui.lastPrediction
            if (pred != null && pred.market == market.name) {
                Spacer(Modifier.height(20.dp))
                TqgCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = BrandMagenta, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.size(6.dp))
                                Text("Hasil Prediksi", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                            ConfidenceRing(fraction = (pred.confidence.toFloat() / 100f))
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Brush.horizontalGradient(listOf(BrandViolet.copy(alpha = 0.35f), BrandMagenta.copy(alpha = 0.2f))))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                pred.prediction.main.joinToString(" "),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp,
                                color = Color.White
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Engine", fontSize = 11.sp, color = TextMuted)
                            Text(pred.engine, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        }
                        if (pred.targetPeriod.isNotBlank()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Target", fontSize = 11.sp, color = TextMuted)
                                Text("Periode ${pred.targetPeriod}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            }
                        }
                        if (pred.prediction.backup.isNotEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Backup", fontSize = 11.sp, color = TextMuted)
                                Text(pred.prediction.backup.joinToString(" "), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = BrandMagenta)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(30.dp))
        }
    }
}
