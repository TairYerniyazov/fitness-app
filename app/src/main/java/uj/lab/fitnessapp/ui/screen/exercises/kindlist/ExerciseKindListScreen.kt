package uj.lab.fitnessapp.ui.screen.exercises.kindlist

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uj.lab.fitnessapp.navigation.Screen
import uj.lab.fitnessapp.ui.component.ExerciseKindListEntry
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import uj.lab.fitnessapp.ui.theme.favoriteColor

/**
 * Screen displaying a list of exercise kinds.
 * Screen 2 in figma.
 */
@Composable
fun ExerciseKindListScreen(navController: NavController, workoutDate: String) {
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
                if (state.filteredExercises.isEmpty()) {
                    item {
                        Text(
                            "Brak ćwiczeń spełniających podane kryteria.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(state.filteredExercises) { exercise ->
                        ExerciseKindListEntry(
                            exercise = exercise,
                            onClick = { navController.navigate(Screen.ExerciseInstanceCreate.withArgs(exercise.exerciseName, workoutDate))},
                            onFavoriteClick = { viewModel.toggleFavorite(exercise) }
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                // TODO: create exercise kind
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer),
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier
                                .height(56.dp)
                                .fillMaxWidth(0.6f)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add",
                                modifier = Modifier.padding(end = 8.dp),
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                text = "Dodaj rodzaj ćwiczenia",
                                color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
               MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                   getFilters().forEach { filter ->
                       val resolvedBgColor = when (filter.color) {
                           FilterColors.Strength -> MaterialTheme.colorScheme.primaryContainer
                           FilterColors.Cardio -> MaterialTheme.colorScheme.secondaryContainer
                           FilterColors.Favorite -> MaterialTheme.colorScheme.tertiaryContainer
                           FilterColors.User -> MaterialTheme.colorScheme.errorContainer
                       }
                       val resolvedTextColor = when (filter.color) {
                           FilterColors.Strength -> MaterialTheme.colorScheme.onPrimaryContainer
                           FilterColors.Cardio -> MaterialTheme.colorScheme.onSecondaryContainer
                           FilterColors.Favorite -> MaterialTheme.colorScheme.onTertiaryContainer
                           FilterColors.User -> MaterialTheme.colorScheme.onErrorContainer
                       }
                   SegmentedButton(
                       shape = SegmentedButtonDefaults.itemShape(
                           index = filter.index,
                           count = getFilters().size
                       ),
                       onCheckedChange = {
                           viewModel.toggleFilter(filter)
                           viewModel.filterExercises()
                                 },
                       checked = viewModel.isFilterSelected(filter),
                       label = { Icon(
                           imageVector = filter.icon(),
                           contentDescription = filter.description,
                           modifier = Modifier.size(filterIconSize),
                           tint = resolvedTextColor
                       )},
                       colors = SegmentedButtonDefaults.colors(
                           activeContainerColor = resolvedBgColor,
                           activeContentColor =  resolvedTextColor,
                           disabledInactiveContainerColor = MaterialTheme.colorScheme.surface,
                           disabledInactiveContentColor = MaterialTheme.colorScheme.onSurface
                       )
                   )
                   }
               }
            }
        }
    }
}