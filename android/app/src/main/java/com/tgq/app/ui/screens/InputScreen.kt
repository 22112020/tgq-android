package com.tgq.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentPaste
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tgq.app.ui.AppViewModel
import com.tgq.app.ui.components.SectionHeader
import com.tgq.app.ui.components.TqgButton
import com.tgq.app.ui.theme.BrandMagenta
import com.tgq.app.ui.theme.Success
import com.tgq.app.ui.theme.Danger
import com.tgq.app.ui.theme.Stroke
import com.tgq.app.ui.theme.Surface
import com.tgq.app.ui.theme.TextMuted
import com.tgq.app.ui.theme.TextPrimary
import kotlinx.coroutines.launch

@Composable
fun InputScreen(vm: AppViewModel) {
    var text by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf<Pair<Boolean, String>?>(null) }
    var busy by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        SectionHeader("Luna Parse")
        Text(
            "Tempel hasil result pasaran dari situs, lalu parse. Data otomatis dikirim ke server TGQ.",
            fontSize = 12.sp,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 14.dp)
        )

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Tempel teks hasil di sini") },
            placeholder = { Text("Contoh: 4DTOTOMACAU POOL ... PERIODE: 12345 ...") },
            minLines = 10,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandMagenta,
                unfocusedBorderColor = Stroke,
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface,
                cursorColor = BrandMagenta,
                focusedLabelColor = BrandMagenta,
                unfocusedLabelColor = TextMuted,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            )
        )

        Spacer(Modifier.height(14.dp))
        TqgButton(
            text = if (busy) "Memproses..." else "Luna Parse & Simpan",
            onClick = {
                busy = true
                scope.launch {
                    try {
                        val msg = vm.parseAndSubmit(text)
                        result = msg.startsWith("Luna Parse:") to msg
                    } catch (e: Exception) {
                        result = false to (e.message ?: "Gagal memproses.")
                    } finally {
                        busy = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp)
        )

        result?.let { (ok, msg) ->
            Spacer(Modifier.height(14.dp))
            Text(
                msg,
                color = if (ok) Success else Danger,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (ok) Success.copy(alpha = 0.08f) else Danger.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
    }
}
