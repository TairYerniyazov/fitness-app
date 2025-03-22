package uj.lab.fitnessapp.ui.screen.exercises.kindlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue

/**
 * Screen displaying a list of exercise kinds.
 * Screen 2 in figma.
 */
@Composable
fun ExerciseKindListScreen() {
    val viewModel = hiltViewModel<ExerciseKindListViewModel>()
    val state by viewModel.uiState.collectAsState()

    Scaffold { padding ->
        Column(Modifier.padding(padding)) {
            Text("Exercise kind list screen")
            LazyColumn {
                items(state.exerciseKinds) { exerciseKind ->
                    Text(exerciseKind.name)
                }
            }
        }
    }
}