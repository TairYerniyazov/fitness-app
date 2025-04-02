package uj.lab.fitnessapp.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import uj.lab.fitnessapp.navigation.Screen
import androidx.compose.ui.graphics.Color
import uj.lab.fitnessapp.ui.theme.backgroundColor
import uj.lab.fitnessapp.ui.theme.green1


/**
 * Home screen.
 * Screen 1 in figma.
 */
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()

    Scaffold(
        containerColor = backgroundColor,
        content = { padding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding)) {
                Button(
                    onClick = {
                        navController.navigate(Screen.ExerciseKindList.route)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = green1),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(18.dp)
                        .height(56.dp)
                        .fillMaxWidth(0.6f)
                ) {
                    Text(
                        "Dodaj instancję ćwiczenia",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
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
