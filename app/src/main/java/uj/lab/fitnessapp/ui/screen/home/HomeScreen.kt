package uj.lab.fitnessapp.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import uj.lab.fitnessapp.navigation.Screen

/**
 * Home screen.
 * Screen 1 in figma.
 */
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()

    Scaffold(
        content = { padding ->
            Column(Modifier.padding(padding)) {
                Text("Home screen")
                Button(onClick = {
                    navController.navigate(Screen.ExerciseKindList.route)
                }) {
                    Text("Go to exercise kind list")
                }
            }
        },
        bottomBar = {
            NavigationBar {
                // TODO(noituri): This is just a placeholder
                val routes = listOf(
                    Icons.Default.Home to Screen.Home,
                    Icons.Default.Info to null,
                    Icons.Default.Settings to null
                )
                routes.forEach {
                    NavigationBarItem(
                        icon = { Icon(it.first, contentDescription = null) },
                        selected = navController.currentDestination?.route == it.second?.route,
                        onClick = {
                            it.second?.let { screen ->
                                if (navController.currentDestination?.route != screen.route) {
                                    navController.navigate(screen.route)
                                }
                            }
                        }
                    )
                }
            }
        }
    )
}