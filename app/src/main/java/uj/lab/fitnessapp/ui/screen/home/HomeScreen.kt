package uj.lab.fitnessapp.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import uj.lab.fitnessapp.navigation.Screen
import androidx.compose.ui.graphics.Color
import uj.lab.fitnessapp.ui.theme.backgroundColor
import uj.lab.fitnessapp.ui.theme.green1
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import uj.lab.fitnessapp.data.model.WorkoutType
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


/**
 * Home screen.
 * Screen 1 in figma.
 */
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadExerciseInstances()
    }

    Scaffold(
        containerColor = backgroundColor,
        content = { padding ->
            LazyColumn(Modifier.padding(padding)) {
                items(state.exerciseInstances) {
                    Column{
                        Text("${it.exercise?.exerciseName} (${it.exerciseInstance?.date})")
                        it.seriesList?.forEachIndexed { index, set ->
                            when (it.exercise!!.workoutType) {
                                WorkoutType.Cardio ->
                                    Text("Seria ${index + 1} - ${set.distance} m, ${set.time?.seconds}")
                                WorkoutType.Strength ->
                                    Text("Seria ${index + 1} - ${set.reps} powtórzeń, ${set.load} kg")
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            Button(
                onClick = {
                    navController.navigate(Screen.ExerciseKindList.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = green1),
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
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
        },
        floatingActionButtonPosition = FabPosition.Center,
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

