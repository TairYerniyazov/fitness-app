package uj.lab.fitnessapp.ui.screen.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uj.lab.fitnessapp.navigation.Screen
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import uj.lab.fitnessapp.ui.component.ExerciseToAnalyseEntry


/**
 * Screen displaying a list of exercise kinds available to analyse.
 * Screen 7 in figma.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseToAnalyseListScreen(navController: NavController) {
    val viewModel = hiltViewModel<ExerciseToAnalyseViewModel>()
    val state by viewModel.uiState.collectAsState()
    val textFieldState = rememberTextFieldState()

    LaunchedEffect(Unit) {
        viewModel.loadExercises()
    }
    Scaffold { padding ->
        Column(
            Modifier
                .fillMaxSize()
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = textFieldState.text.toString(),
                        onQueryChange = {
                            textFieldState.edit { replace(0, length, it) }
                            viewModel.filterExercises(it)
                        },
                        expanded = false,
                        onExpandedChange = { },
                        onSearch = { },
                        placeholder = { Text("Szukaj ćwiczenia") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Szukaj") },
                    )
                },
                expanded = false,
                onExpandedChange = { },
                content = { },
            )
            LazyColumn(Modifier.weight(1f)) {
                if (state.filteredExercises.isEmpty()) {
                    item {
                        Text(
                            "Brak ćwiczeń do analizowania.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(state.filteredExercises) { exercise ->
                        ExerciseToAnalyseEntry(
                            exercise = exercise,
                            onClick = { navController.navigate(Screen.Analytics.withArgs(exercise.exerciseName)) }
                        )
                    }
                }
            }
        }
    }
}