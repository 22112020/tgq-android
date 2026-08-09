package com.tgq.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tgq.app.data.MarketInfo
import com.tgq.app.ui.AppViewModel
import com.tgq.app.ui.components.ErrorBlock
import com.tgq.app.ui.components.LoadingBlock
import com.tgq.app.ui.components.MarketRow
import com.tgq.app.ui.components.SectionHeader
import com.tgq.app.ui.theme.BrandMagenta
import com.tgq.app.ui.theme.Stroke
import com.tgq.app.ui.theme.Surface
import com.tgq.app.ui.theme.TextMuted
import com.tgq.app.ui.theme.TextPrimary
import com.tgq.app.ui.theme.TextSecondary

@Composable
fun MarketsScreen(vm: AppViewModel, onOpenDetail: (MarketInfo) -> Unit) {
    val ui = vm.ui
    var query by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (ui.value.markets.isEmpty()) vm.refreshMarkets()
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader("Pasaran Luna Core")
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Cari pasaran...", color = TextMuted, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = TextSecondary) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandMagenta,
                unfocusedBorderColor = Stroke,
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface,
                cursorColor = BrandMagenta
            )
        )

        val filtered = ui.value.markets.filter {
            query.isBlank() || it.name.contains(query.trim().uppercase(), ignoreCase = true)
        }

        when {
            ui.value.loading && ui.value.markets.isEmpty() -> LoadingBlock()
            ui.value.markets.isEmpty() && ui.value.error != null -> ErrorBlock(ui.value.error.orEmpty()) { vm.refreshMarkets() }
            ui.value.markets.isEmpty() -> LoadingBlock()
            else -> {
                Text("${filtered.size} pasaran", fontSize = 11.sp, color = TextMuted)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    filtered.forEach { m ->
                        MarketRow(market = m, onClick = { onOpenDetail(m) })
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}
