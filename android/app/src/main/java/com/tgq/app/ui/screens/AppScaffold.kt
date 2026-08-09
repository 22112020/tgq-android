package com.tgq.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tgq.app.ui.components.GradientOrb
import com.tgq.app.ui.components.TqgBottomNav
import com.tgq.app.ui.components.Tab
import com.tgq.app.ui.theme.BrandMagenta
import com.tgq.app.ui.theme.BrandViolet
import com.tgq.app.ui.theme.NightInk

/**
 * Shared scaffold for bottom-tab screens: gradient background + bottom navigation.
 */
@Composable
fun AppScaffold(
    currentTab: Tab,
    onSelectTab: (Tab) -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        containerColor = NightInk,
        bottomBar = {
            TqgBottomNav(current = currentTab, onSelect = onSelectTab)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NightInk)
                .padding(padding)
        ) {
            GradientOrb(Modifier.align(Alignment.TopEnd).offset(x = 90.dp, y = (-70).dp).size(240.dp), BrandViolet)
            GradientOrb(Modifier.align(Alignment.BottomStart).offset(x = (-60).dp, y = 100.dp).size(300.dp), BrandMagenta)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp, bottom = 16.dp)
            ) {
                content()
            }
        }
    }
}
