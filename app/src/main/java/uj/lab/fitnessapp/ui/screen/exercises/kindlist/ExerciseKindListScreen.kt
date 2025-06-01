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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import uj.lab.fitnessapp.data.model.Exercise

/**
 * Screen displaying a list of exercise kinds.
 * Screen 2 in figma.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseKindListScreen(navController: NavController) {
    val viewModel = hiltViewModel<ExerciseListViewModel>()
    val state by viewModel.uiState.collectAsState()
    val selectedFilters by viewModel.selectedFilters.collectAsState()
    val filterIconSize = 32.dp

    var exerciseToDelete by remember { mutableStateOf<Exercise?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadExercises()
    }

    Log.d("DEBUG", "ExerciseKindListScreen: ${state.allExercises.size} exercises loaded")
    Scaffold { padding ->
        exerciseToDelete?.let {
            AlertDialog(
                icon = { Icon(Icons.Default.Warning, contentDescription = "Warning") },
                title = { Text("Potwierdź usunięcie ćwiczenia") },
                text = { Text("Czy na pewno chcesz usunąć ćwiczenie ${exerciseToDelete?.exerciseName}?") },
                onDismissRequest = {
                    exerciseToDelete = null
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteExercise(it.id)
                        exerciseToDelete = null
                    }) {
                        Text("Usuń", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { exerciseToDelete = null }) {
                        Text("Anuluj", color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
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
                            onClick = {
                                navController.navigate(
                                    Screen.ExerciseInstanceCreate.withArgs(
                                        exercise.exerciseName
                                    )
                                )
                            },
                            onFavoriteClick = { viewModel.toggleFavorite(exercise) },
                            onEditClick = {
                                navController.navigate(Screen.EditExerciseKind.withArgs(exercise.id))
                            },
                            onDeleteClick = { exerciseToDelete = it }
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
                                val selectedFiltersDescriptions =
                                    viewModel.getSelectedFilters().map { it.description }
                                navController.navigate(
                                    Screen.ExerciseKindCreate.withArgs(
                                        selectedFiltersDescriptions
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
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
                                text = "Dodaj nowe ćwiczenie",
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
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
                    viewModel.filters.forEachIndexed() { index, filter ->
                        val isSelected = selectedFilters.contains(filter)
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
                        key(index) {
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = viewModel.filters.size
                                ),
                                onCheckedChange = {
                                    viewModel.toggleFilter(filter)
                                    viewModel.filterExercises()
                                },
                                checked = isSelected,
                                label = {
                                    Icon(
                                        imageVector = filter.icon(),
                                        contentDescription = filter.description,
                                        modifier = Modifier.size(filterIconSize),
                                        tint = resolvedTextColor
                                    )
                                },
                                colors = SegmentedButtonDefaults.colors(
                                    activeContainerColor = resolvedBgColor,
                                    activeContentColor = resolvedTextColor,
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
}