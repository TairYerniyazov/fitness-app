package uj.lab.fitnessapp.ui.screen.analytics

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import uj.lab.fitnessapp.navigation.Screen
import uj.lab.fitnessapp.ui.theme.backgroundColor
import uj.lab.fitnessapp.ui.theme.darkGreen
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors


/**
 * Analytics screen.
 * Screen 6 in figma.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController) {
    val viewModel: AnalyticsViewModel = hiltViewModel()
    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Analityka") },
                colors = TopAppBarColors(
                    containerColor = darkGreen,
                    scrolledContainerColor = darkGreen,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar {
                val routes = listOf(
                    Icons.Default.Home to Screen.Home,
                    Icons.Default.Info to Screen.Analytics,
                    Icons.Default.Settings to Screen.Settings
                )
                routes.forEach {
                    NavigationBarItem(
                        icon = { Icon(it.first, contentDescription = null) },
                        selected = navController.currentDestination?.route == it.second.route,
                        onClick = {
                            it.second.let { screen ->
                                if (navController.currentDestination?.route != screen.route) {
                                    navController.navigate(screen.route)
                                }
                            }
                        }
                    )
                }
            }
        },
        content = {
            // TODO: Add analytics screen content
        }
    )
}