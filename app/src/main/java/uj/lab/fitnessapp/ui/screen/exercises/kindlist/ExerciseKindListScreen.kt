package uj.lab.fitnessapp.ui.screen.exercises.kindlist

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.navigation.Screen
import uj.lab.fitnessapp.ui.component.ExerciseKindListEntry
import uj.lab.fitnessapp.ui.theme.*
import androidx.compose.material3.Icon
import uj.lab.fitnessapp.R

/**
 * Screen displaying a list of exercise kinds.
 * Screen 2 in figma.
 */
@Composable
fun ExerciseKindListScreen(navController: NavController) {
    val viewModel = hiltViewModel<ExerciseListViewModel>()
    val state by viewModel.uiState.collectAsState()
    val filterIconSize = 32.dp

    LaunchedEffect(Unit) {
        viewModel.loadExercises()
    }

    Log.d("DEBUG", "ExerciseKindListScreen: ${state.allExercises.size} exercises loaded")
    Scaffold { padding ->
        Column(Modifier
            .fillMaxSize()
            .padding(padding)) {
            LazyColumn(Modifier.weight(1f)) {
                items(state.filteredExercises) { exercise ->
                        ExerciseKindListEntry(
                            exercise = exercise,
                            // should be replaced with proper screen
                            onClick = { navController.navigate(Screen.ExerciseInstanceCreate.withArgs(exercise.exerciseName))},
                            onFavoriteClick = { viewModel.toggleFavorite(exercise) }
                        )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { viewModel.filterExercises { it.workoutType == WorkoutType.Cardio } },
                    colors = ButtonDefaults.buttonColors(containerColor = cardioColor)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.directions_run_24px),
                        contentDescription = "Cardio",
                        modifier = Modifier.size(filterIconSize)
                    )
//                    Text(
//                        text = "Cardio"
//                    )
                }
                Button(
                    onClick = { viewModel.filterExercises { it.workoutType == WorkoutType.Strength } },
                    colors = ButtonDefaults.buttonColors(containerColor = strengthColor)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.exercise_24px),
                        contentDescription = "Strength",
                        modifier = Modifier.size(filterIconSize)
                    )
//                    Text(
//                        text = "Strength"
//                    )
                }
                Button(
                    onClick = { viewModel.filterExercises { it.isFavourite } },
                    colors = ButtonDefaults.buttonColors(containerColor = favoriteColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorites",
                        modifier = Modifier.size(filterIconSize)
                    )
//                    Text(
//                        text = "Favorites"
//                    )
                }
                Button(
                    onClick = { viewModel.filterExercises { true } },
                    colors = ButtonDefaults.buttonColors(containerColor = lovelyPink)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "All",
                        modifier = Modifier.size(filterIconSize)
                    )
//                    Text(
//                        text = "All"
//                    )
                }
            }
        }
    }
}