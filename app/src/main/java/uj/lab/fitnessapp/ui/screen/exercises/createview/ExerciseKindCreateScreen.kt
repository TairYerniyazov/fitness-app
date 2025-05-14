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
import androidx.navigation.NavController
import uj.lab.fitnessapp.data.model.WorkoutType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseKindCreateScreen(navController: NavController, selectedFilters: List<String>) {
    val viewModel = hiltViewModel<ExerciseKindCreateViewModel>()
    val state = viewModel.uiState.collectAsState()

    var selectedExerciseName by remember { mutableStateOf("") }
    var selectedWorkoutType by remember { mutableStateOf(WorkoutType.Strength) }
    var selectedIsFavourite by remember { mutableStateOf(false) }

    var isWorkoutTypeDropdownMenuExpanded by remember { mutableStateOf(false) }
    var isFavouriteDropdownMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(selectedFilters) {
        // set selectedWorkoutType to Cardio if in selectedFilters, Strength is default
        if (selectedFilters.contains("Cardio")) {
            selectedWorkoutType = WorkoutType.Cardio
        }
        // set selectedIsFavourite to Tak if in selectedFilters, Nie is default
        if (selectedFilters.contains("Favorites")) {
            selectedIsFavourite = true
        }
    }

    Log.d("DEBUG", "ExerciseKindCreateScreen: $selectedFilters")

    Scaffold (
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(title = {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Dodaj nowe ćwiczenie",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }, colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = MaterialTheme.colorScheme.background))
        },
        content = { padding ->
            Column (
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                // text box for exercise name
                Text(
                    text = "Nazwa ćwiczenia:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 8.dp)
                )
                OutlinedTextField(
                    value = selectedExerciseName,
                    onValueChange = {
                        selectedExerciseName = it
                        //viewModel.setNewExerciseName(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    singleLine = true,
                    shape = MaterialTheme.shapes.small
                )
                // dropdown menu for workout type
                Text(
                    text = "Rodzaj ćwiczenia:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 8.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = isWorkoutTypeDropdownMenuExpanded,
                    onExpandedChange = { isWorkoutTypeDropdownMenuExpanded = !isWorkoutTypeDropdownMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedWorkoutType.toString(),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isWorkoutTypeDropdownMenuExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isWorkoutTypeDropdownMenuExpanded,
                        onDismissRequest = { isWorkoutTypeDropdownMenuExpanded = false }
                    ) {
                        WorkoutType.entries.forEach { workoutType ->
                            DropdownMenuItem(
                                text = { Text(workoutType.toString()) },
                                onClick = {
                                    selectedWorkoutType = workoutType
                                    isWorkoutTypeDropdownMenuExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
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
                    expanded = isFavouriteDropdownMenuExpanded,
                    onExpandedChange = { isFavouriteDropdownMenuExpanded = !isFavouriteDropdownMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = if (selectedIsFavourite) "Tak" else "Nie",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isFavouriteDropdownMenuExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isFavouriteDropdownMenuExpanded,
                        onDismissRequest = { isFavouriteDropdownMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Nie") },
                            onClick = {
                                selectedIsFavourite = false
                                isFavouriteDropdownMenuExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                        DropdownMenuItem(
                            text = { Text("Tak") },
                            onClick = {
                                selectedIsFavourite = true
                                isFavouriteDropdownMenuExpanded = false
                            },
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
                Row (
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            //viewModel.saveExercise()
                            //navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
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
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
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