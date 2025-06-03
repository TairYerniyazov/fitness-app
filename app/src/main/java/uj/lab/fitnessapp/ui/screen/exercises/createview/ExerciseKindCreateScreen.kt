package uj.lab.fitnessapp.ui.screen.exercises.createview

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uj.lab.fitnessapp.data.model.WorkoutType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseKindCreateScreen(
    navController: NavController,
    selectedFilters: List<String>,
    kindId: Int? = null
) {
    val viewModel = hiltViewModel<ExerciseKindCreateViewModel>()
    val state by viewModel.uiState.collectAsState()

    var title by remember { mutableStateOf("Dodaj nowe ćwiczenie") }
    var isWorkoutTypeMenuExpanded by remember { mutableStateOf(false) }
    var isFavouriteMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(selectedFilters) {
        if (kindId != null) {
            return@LaunchedEffect
        }
        // set selectedWorkoutType to Cardio if in selectedFilters, Strength is default
        if (selectedFilters.contains("Cardio")) {
            viewModel.setExerciseType(WorkoutType.Cardio)
        }
        // set selectedIsFavourite to Tak if in selectedFilters, Nie is default
        if (selectedFilters.contains("Favorites")) {
            viewModel.setExerciseFavorite(true)
        }
    }
    LaunchedEffect(kindId) {
        if (kindId != null) {
            viewModel.loadExerciseKind(kindId)
            title = "Edytuj ćwiczenie"
        }
        else {
            viewModel.setExerciseName("")
        }
    }

    Log.d("DEBUG", "ExerciseKindCreateScreen: $selectedFilters")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // text box for exercise name
                Text(
                    text = "Nazwa ćwiczenia:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 8.dp)
                )
                OutlinedTextField(
                    value = state.exerciseName,
                    onValueChange = {
                        viewModel.setExerciseName(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp),
                    singleLine = true,
                    shape = MaterialTheme.shapes.small,
                    placeholder = { Text("Wpisz nazwę ćwiczenia") },
                    isError = state.errorMsg != null,
                    supportingText = {
                        if (state.errorMsg != null) {
                            Text(
                                state.errorMsg!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                // dropdown menu for workout type
                Text(
                    text = "Rodzaj ćwiczenia:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 8.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = isWorkoutTypeMenuExpanded,
                    onExpandedChange = { isWorkoutTypeMenuExpanded = !isWorkoutTypeMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = when (state.workoutType) {
                            WorkoutType.Strength -> "Siłowe"
                            WorkoutType.Cardio -> "Cardio"
                        },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isWorkoutTypeMenuExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isWorkoutTypeMenuExpanded,
                        onDismissRequest = { isWorkoutTypeMenuExpanded = false }
                    ) {
                        fun selectWorkoutTypeAndCloseMenu(workoutType: WorkoutType) {
                            viewModel.setExerciseType(workoutType)
                            isWorkoutTypeMenuExpanded = false
                        }
                        DropdownMenuItem(
                            text = { Text("Siłowe") },
                            onClick = { selectWorkoutTypeAndCloseMenu(WorkoutType.Strength) },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                        DropdownMenuItem(
                            text = { Text("Cardio") },
                            onClick = { selectWorkoutTypeAndCloseMenu(WorkoutType.Cardio) },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
                // dropdown menu for favourite
                Text(
                    text = "Ulubione:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 8.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = isFavouriteMenuExpanded,
                    onExpandedChange = { isFavouriteMenuExpanded = !isFavouriteMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = when (state.isFavorite) {
                            true -> "Tak"
                            false -> "Nie"
                        },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isFavouriteMenuExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isFavouriteMenuExpanded,
                        onDismissRequest = { isFavouriteMenuExpanded = false }
                    ) {
                        fun selectFavouriteOptionAndCloseMenu(isFavourite: Boolean) {
                            viewModel.setExerciseFavorite(isFavourite)
                            isFavouriteMenuExpanded = false
                        }
                        DropdownMenuItem(
                            text = { Text("Nie") },
                            onClick = { selectFavouriteOptionAndCloseMenu(false) },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                        DropdownMenuItem(
                            text = { Text("Tak") },
                            onClick = { selectFavouriteOptionAndCloseMenu(true) },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            if (kindId != null) {
                                viewModel.updateExercise(kindId, onSuccess = {
                                    navController.popBackStack()
                                }, onError = { msg ->
                                    viewModel.setErrorMsg(msg)
                                })
                            } else {
                                viewModel.saveNewExercise(
                                    onSuccess = {
                                        navController.popBackStack()
                                    },
                                    onError = { msg ->
                                        viewModel.setErrorMsg(msg)
                                    }
                                )
                            }
                        },
                        enabled = state.exerciseName.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 1.0f),
                            disabledContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 1.0f)
                        ),
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = "Zapisz",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Button(
                        onClick = {
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 1.0f),
                            disabledContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 1.0f)
                        ),
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            text = "Anuluj",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        })
}