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
import uj.lab.fitnessapp.ui.component.DatePickerFieldToModal
import uj.lab.fitnessapp.ui.component.ExerciseInstanceEntry
import uj.lab.fitnessapp.ui.screen.exercises.kindlist.ExerciseListViewModel
import java.text.SimpleDateFormat
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

    val exerciseInstanceListState = rememberLazyListState()
    var selectedDate by remember { mutableStateOf<Long?>(Date().time) }

    val currentDate = Date()
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val defaultDate = formatter.format(currentDate)

    val dateOnly = formatter.parse(defaultDate)
    val dateOnlyMillis = dateOnly!!.time

    if (selectedDate == null) {
        selectedDate = dateOnlyMillis
    }

    LaunchedEffect(selectedDate) {
        val date = selectedDate
        val stringDate = formatter.format(Date(date!!))
        Log.d("Test","Changing date to $stringDate")
        viewModel.loadExerciseInstances(stringDate)
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
                onDateChange = { selectedDate = it },
                selectedDate = selectedDate
            )
        },
        content = { padding ->
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
                        onDelete = {
                            viewModel.deleteExerciseInstance(instance.exerciseInstance!!.id)
                        }
                    )
                }
            }
        },

        floatingActionButton = {
            Button(
                onClick = {
                    val date = selectedDate
                    val stringDate = formatter.format(Date(date!!))
                    navController.navigate(Screen.ExerciseKindList.withArgs(stringDate))
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

