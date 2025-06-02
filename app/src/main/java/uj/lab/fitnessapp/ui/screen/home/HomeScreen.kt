package uj.lab.fitnessapp.ui.screen.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.R
import uj.lab.fitnessapp.data.model.ExerciseInstance
import uj.lab.fitnessapp.ui.component.DatePickerFieldToModal
import uj.lab.fitnessapp.ui.component.ExerciseInstanceEntry
import uj.lab.fitnessapp.ui.screen.exercises.kindlist.ExerciseListViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


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
    val selectedDate by viewModel.date.collectAsState()

    val exerciseInstanceListState = rememberLazyListState()

    var exerciseToDelete by remember { mutableStateOf<ExerciseInstance?>(null) }

    LaunchedEffect(selectedDate) {
        selectedDate.let{
            viewModel.loadExerciseInstances(it)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

//        topBar = {
//            TopAppBar(
//                title = {
//                    Column(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text("Dzisiaj", fontWeight = FontWeight.Bold, fontSize = 30.sp, color = MaterialTheme.colorScheme.onPrimary)
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
//            )
//        },

        topBar = {
            DatePickerFieldToModal(
//                            colors = MaterialTheme.colorScheme.onPrimary //TODO: Nie działa, trzeba ogarnąć kolory w DatePicker.kt
                modifier = Modifier
                    .height(130.dp)
                    .padding(PaddingValues(start = 25.dp, end = 25.dp, top = 40.dp, bottom = 25.dp)),
                onDateChange = {
                    viewModel.setDate(it!!)
                },
                selectedDate = selectedDate
            )
        },
        content = { padding ->
            exerciseToDelete?.let {
                AlertDialog(
                    onDismissRequest = { exerciseToDelete = null },
                    title = { Text("Potwierdzenie usunięcia") },
                    text = { Text("Czy na pewno chcesz usunąć tę instancję ćwiczenia?") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteExerciseInstance(it.id)
                            exerciseToDelete = null
                        }) {
                            Text("Usuń", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { exerciseToDelete = null }) {
                            Text("Usuń", color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                )
            }
            LazyColumn(
                Modifier.padding(padding),
                state = exerciseInstanceListState,
                contentPadding = PaddingValues(bottom = 100.dp),
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
                        },
                        onAnalyticsClick = { clickedExercise ->
                            navController.navigate(Screen.Analytics.withArgs(clickedExercise.exerciseName)) },
                        onDelete = {
                            exerciseToDelete = instance.exerciseInstance
                        },
                        onEditClick = { clickedExercise ->
                            navController.navigate(
                                Screen.EditExerciseInstance.createRoute(
                                    exerciseKind = clickedExercise.exercise!!.exerciseName,
                                    instanceId = clickedExercise.exerciseInstance!!.id
                                )
                            )
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
                    .padding(16.dp)
                    .height(56.dp)
                    .fillMaxWidth(0.7f)
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
                val routes = listOf(
                    Icons.Default.Home to Screen.Home,
                    null to Screen.Analytics,
                    Icons.Default.Settings to Screen.Settings
                )
                routes.forEach { it ->
                    val (icon, screen) = it
                    NavigationBarItem(
                        icon = {
                            if (screen == Screen.Analytics) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_auto_graph_24),
                                    contentDescription = "Analytics",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                icon?.let {
                                    Icon(
                                        it,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        selected = navController.currentDestination?.route == screen.route,
                        onClick = {
                            if (navController.currentDestination?.route != screen.route) {
                                navController.navigate(screen.route)
                            }
                        }
                    )
                }
            }
        }
    )
}

