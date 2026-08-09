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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tgq.app.data.Session
import com.tgq.app.ui.AppViewModel
import com.tgq.app.ui.components.ButtonVariant
import com.tgq.app.ui.components.GradientOrb
import com.tgq.app.ui.components.GradientText
import com.tgq.app.ui.components.TqgButton
import com.tgq.app.ui.theme.BrandGradient
import com.tgq.app.ui.theme.BrandMagenta
import com.tgq.app.ui.theme.BrandViolet
import com.tgq.app.ui.theme.Danger
import com.tgq.app.ui.theme.NightInk
import com.tgq.app.ui.theme.Stroke
import com.tgq.app.ui.theme.Surface
import com.tgq.app.ui.theme.TextMuted
import com.tgq.app.ui.theme.TextPrimary
import com.tgq.app.ui.theme.TextSecondary

@Composable
fun LoginScreen(vm: AppViewModel, onLoggedIn: () -> Unit) {
    val ui = vm.ui
    var username by rememberSaveable { mutableStateOf(Session.get().username) }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var server by rememberSaveable { mutableStateOf(Session.get().serverBase) }

    LaunchedEffect(ui.value.loggedIn) {
        if (ui.value.loggedIn) onLoggedIn()
    }

    Box(Modifier.fillMaxSize().background(NightInk)) {
        GradientOrb(Modifier.align(Alignment.TopEnd).offset(x = 50.dp, y = (-40).dp).size(260.dp), BrandViolet)
        GradientOrb(Modifier.align(Alignment.BottomStart).offset(x = (-40).dp, y = 40.dp).size(280.dp), BrandMagenta)

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(BrandGradient),
                contentAlignment = Alignment.Center
            ) {
                Text("A", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.height(16.dp))
            GradientText("TGQ", fontSize = 42, letterSpacing = 5)
            Text("Masuk sebagai admin Luna Core", color = TextMuted, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
            Spacer(Modifier.height(28.dp))

            TqgTextField("Server", server, Icons.Rounded.Public, onValue = { server = it })
            Spacer(Modifier.height(12.dp))
            TqgTextField("Username", username, Icons.Rounded.Person, onValue = { username = it })
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(if (passwordVisible) "Sembunyikan" else "Lihat", fontSize = 10.sp, color = BrandMagenta)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors()
            )

            ui.value.error?.let {
                Spacer(Modifier.height(10.dp))
                Text(it, color = Danger, fontSize = 12.sp)
            }

            Spacer(Modifier.height(20.dp))
            TqgButton(
                text = if (ui.value.loading) "Memeriksa..." else "Masuk",
                onClick = {
                    Session.get().serverBase = server
                    vm.login(username, password) { ok, err ->
                        if (!ok) err?.let { } // error surfaced via state
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            )
            Spacer(Modifier.height(10.dp))
            TextButton(onClick = { vm.setServer(server) }) {
                Text("Uji koneksi ke server ini", fontSize = 11.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun TqgTextField(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onValue: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValue,
        label = { Text(label) },
        singleLine = true,
        leadingIcon = { Icon(icon, contentDescription = label, tint = TextSecondary) },
        modifier = Modifier.fillMaxWidth(),
        colors = fieldColors()
    )
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = BrandMagenta,
    unfocusedBorderColor = Stroke,
    focusedLabelColor = BrandMagenta,
    unfocusedLabelColor = TextMuted,
    cursorColor = BrandMagenta,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedContainerColor = Surface,
    unfocusedContainerColor = Surface
)

@Suppress("unused")
private val _keepVariant = ButtonVariant.Primary
