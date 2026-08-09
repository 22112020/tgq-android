package com.tgq.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tgq.app.ui.AppViewModel
import com.tgq.app.ui.components.ButtonVariant
import com.tgq.app.ui.components.SectionHeader
import com.tgq.app.ui.components.TqgButton
import com.tgq.app.ui.components.TqgCard
import com.tgq.app.ui.theme.BrandGradient
import com.tgq.app.ui.theme.BrandMagenta
import com.tgq.app.ui.theme.Danger
import com.tgq.app.ui.theme.Stroke
import com.tgq.app.ui.theme.Surface
import com.tgq.app.ui.theme.TextMuted
import com.tgq.app.ui.theme.TextPrimary
import com.tgq.app.ui.theme.TextSecondary

@Composable
fun ProfileScreen(vm: AppViewModel) {
    val ui = vm.ui
    var server by rememberSaveable { mutableStateOf(ui.value.serverBase) }
    val session = com.tgq.app.data.Session.get()

    Column {
        SectionHeader("Profil")
        Spacer(Modifier.height(12.dp))

        TqgCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(BrandGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Text("A", fontSize = 26.sp, androidx.compose.ui.text.font.FontWeight.ExtraBold, color = androidx.compose.ui.graphics.Color.White)
                }
                Spacer(Modifier.size(14.dp))
                Column {
                    Text(ui.value.username.ifBlank { "Admin" }, fontSize = 16.sp, androidx.compose.ui.text.font.FontWeight.Bold, color = TextPrimary)
                    Text("Admin Luna Core", fontSize = 11.sp, color = TextMuted)
                }
            }
        }

        Spacer(Modifier.height(18.dp))
        Text("Server TGQ", fontSize = 13.sp, androidx.compose.ui.text.font.FontWeight.Bold, color = TextPrimary)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = server,
            onValueChange = { server = it },
            label = { Text("Server base URL") },
            leadingIcon = { Icon(Icons.Rounded.Public, contentDescription = null, tint = TextSecondary) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandMagenta,
                unfocusedBorderColor = Stroke,
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface,
                cursorColor = BrandMagenta
            )
        )
        Spacer(Modifier.height(10.dp))
        TqgButton(
            text = "Simpan & Uji Koneksi",
            onClick = { vm.setServer(server) },
            variant = ButtonVariant.Ghost,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))
        TqgButton(
            text = "Keluar",
            onClick = { vm.logout() },
            variant = ButtonVariant.Ghost,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))
        Text(
            "TGQ — Luna Core v3.0.0\nTersambung: ${session.serverBase}",
            fontSize = 10.sp,
            color = TextMuted,
            lineHeight = 16.sp
        )
        Spacer(Modifier.height(20.dp))
    }
}
