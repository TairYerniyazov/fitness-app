package uj.lab.fitnessapp.ui.screen.exercises.kindlist

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import uj.lab.fitnessapp.ui.component.ExerciseKindListEntry

/**
 * Screen displaying a list of exercise kinds.
 * Screen 2 in figma.
 */
@Composable
fun ExerciseKindListScreen(navController: NavController) {
    val viewModel = hiltViewModel<ExerciseListViewModel>()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadExercises()
    }

    Log.d("DEBUG", "ExerciseKindListScreen: ${state.exercises.size} exercises loaded")
    Scaffold { padding ->
        Column(Modifier.padding(padding)) {
            LazyColumn(Modifier.padding((padding))) {
                items(state.exercises) { exercise ->
                        ExerciseKindListEntry(
                            exercise = exercise,
                            // should be replaced with proper screen
                            onClick = { navController.navigate("dummy_creator/${exercise.exerciseName}")}
                        )
                }
            }
        }
    }
}