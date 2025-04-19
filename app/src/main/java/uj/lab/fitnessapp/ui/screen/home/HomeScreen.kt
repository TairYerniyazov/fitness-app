package uj.lab.fitnessapp.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import uj.lab.fitnessapp.navigation.Screen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.ui.component.DatePickerFieldToModal
import uj.lab.fitnessapp.ui.component.ExerciseInstanceEntry
import uj.lab.fitnessapp.ui.screen.exercises.kindlist.ExerciseListViewModel
import java.util.Date


/**
 * Home screen.
 * Screen 1 in figma.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val exerciseListViewModel = hiltViewModel<ExerciseListViewModel>()
    val state by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val exerciseInstanceListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.loadExerciseInstances()
    }

    val selectedDate = Date().time

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier
//                            .fillMaxWidth()
                            .height(200.dp),
//                            .padding(10.dp,0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DatePickerFieldToModal(
//                            colors = MaterialTheme.colorScheme.onPrimary
//                            modifier = Modifier
//                                .height(240.dp)
//                                .padding(20.dp,10.dp)
                        )
                        Text("Dzisiaj", fontWeight = FontWeight.Bold, fontSize = 30.sp, color = MaterialTheme.colorScheme.onPrimary)
                    }
                },
//                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
            )
        },
        content = { padding ->
            LazyColumn(
                Modifier.padding(padding),
                state = exerciseInstanceListState,
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                itemsIndexed(state.exerciseInstances) { index, instance ->
                    ExerciseInstanceEntry(
                        index = index,
                        instance = instance,
                        onFavoriteClick = { clickedExercise ->
                            val newFavoriteState = !clickedExercise.isFavourite
                            scope.launch {
                                exerciseListViewModel.toggleFavorite(clickedExercise)
                            }
                            viewModel.updateExerciseFavoriteStatus(clickedExercise.exerciseName,
                                newFavoriteState)
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            Button(
                onClick = {
                    navController.navigate(Screen.ExerciseKindList.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .padding(18.dp)
                    .height(56.dp)
                    .fillMaxWidth(0.6f)
            ) {
                Text(
                    "Dodaj instancję ćwiczenia",
                    color = MaterialTheme.colorScheme.onPrimary,
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
        }
    )
}

