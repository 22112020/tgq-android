package com.tgq.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tgq.app.data.MarketInfo
import com.tgq.app.ui.AppViewModel
import com.tgq.app.ui.components.Tab
import com.tgq.app.ui.screens.AppScaffold
import com.tgq.app.ui.screens.HokiScreen
import com.tgq.app.ui.screens.HomeScreen
import com.tgq.app.ui.screens.InputScreen
import com.tgq.app.ui.screens.LoginScreen
import com.tgq.app.ui.screens.MarketDetailScreen
import com.tgq.app.ui.screens.MarketsScreen
import com.tgq.app.ui.screens.ProfileScreen
import com.tgq.app.ui.screens.SplashScreen
import com.tgq.app.ui.theme.TqgTheme

class MainActivity : ComponentActivity() {

    private val vm: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TqgTheme {
                val nav = rememberNavController()

                fun goTab(tab: Tab) {
                    nav.navigate(tab.route) {
                        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                NavHost(navController = nav, startDestination = "splash") {
                    composable("splash") {
                        SplashScreen(onDone = {
                            vm.boot()
                            if (vm.ui.value.loggedIn) {
                                nav.navigate("home") { popUpTo("splash") { inclusive = true } }
                            } else {
                                nav.navigate("login") { popUpTo("splash") { inclusive = true } }
                            }
                        })
                    }
                    composable("login") {
                        LoginScreen(vm = vm, onLoggedIn = {
                            nav.navigate("home") { popUpTo("login") { inclusive = true } }
                        })
                    }
                    composable("home") {
                        AppScaffold(Tab.HOME, ::goTab) {
                            HomeScreen(
                                vm = vm,
                                onOpenHoki = { nav.navigate("hoki") },
                                onOpenMarkets = { nav.navigate("markets") { launchSingleTop = true } }
                            )
                        }
                    }
                    composable("markets") {
                        AppScaffold(Tab.MARKETS, ::goTab) {
                            MarketsScreen(vm = vm) { market ->
                                nav.navigate("market/${market.name}") { launchSingleTop = true }
                            }
                        }
                    }
                    composable("input") {
                        AppScaffold(Tab.INPUT, ::goTab) {
                            InputScreen(vm = vm)
                        }
                    }
                    composable("profile") {
                        AppScaffold(Tab.PROFILE, ::goTab) {
                            ProfileScreen(vm = vm)
                        }
                    }
                    composable("hoki") {
                        HokiScreen(vm = vm, onBack = { nav.popBackStack() })
                    }
                    composable("market/{name}") { entry ->
                        val name = entry.arguments?.getString("name").orEmpty()
                        val market: MarketInfo? = vm.ui.value.markets.firstOrNull { it.name == name }
                        MarketDetailScreen(
                            vm = vm,
                            market = market
                                ?: MarketInfo(name = name, latestResult = "", latestPeriod = "", lastUpdated = ""),
                            onBack = { nav.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
