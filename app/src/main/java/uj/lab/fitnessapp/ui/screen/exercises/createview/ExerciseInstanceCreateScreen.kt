package uj.lab.fitnessapp.ui.screen.exercises.createview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import uj.lab.fitnessapp.data.model.WorkoutSet
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.navigation.Screen
import uj.lab.fitnessapp.ui.component.CardioWorkoutSetEntry
import uj.lab.fitnessapp.ui.component.DurationInput
import uj.lab.fitnessapp.ui.component.DurationInputState
import uj.lab.fitnessapp.ui.component.StrengthWorkoutSetEntry
import uj.lab.fitnessapp.ui.theme.lovelyPink
import kotlin.time.DurationUnit
import kotlin.time.toDuration


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseInstanceCreateScreen(navController: NavController, exerciseKind: String, instanceId: Int? = null) {
    val viewModel = hiltViewModel<ExerciseInstanceCreateViewModel>()
    val state by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet = remember { mutableStateOf(false) }
    var workoutSetToDelete by remember { mutableStateOf<WorkoutSet?>(null) }

    LaunchedEffect(Unit) {
        if (instanceId != null) {
            viewModel.loadExistingInstance(instanceId)
        } else {
            viewModel.loadExercise(exerciseKind)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(title = {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = exerciseKind,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }, colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = MaterialTheme.colorScheme.background))
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                workoutSetToDelete?.let {
                    AlertDialog(
                        onDismissRequest = { workoutSetToDelete = null },
                        title = { Text("Potwierdzenie usunięcia") },
                        text = { Text("Czy na pewno chcesz usunąć tę serię?") },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.removeWorkoutSet(it)
                                workoutSetToDelete = null
                            }) {
                                Text("Usuń", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { workoutSetToDelete = null }) {
                                Text("Anuluj")
                            }
                        }
                    )
                }

                if (showBottomSheet.value) {
                    ModalBottomSheet(onDismissRequest = {
                        showBottomSheet.value = false
                    }, sheetState = sheetState) {
                        when (state.workoutType) {
                            WorkoutType.Cardio ->
                                CardioWorkoutSetCreator(
                                    onSave = { workoutSet ->
                                        viewModel.addWorkoutSet(workoutSet)
                                        showBottomSheet.value = false
                                    },
                                    onCancel = { showBottomSheet.value = false },
                                    viewModel = viewModel
                                )

                            WorkoutType.Strength ->
                                StrengthWorkoutSetCreator(
                                    onSave = { workoutSet ->
                                        viewModel.addWorkoutSet(workoutSet)
                                        showBottomSheet.value = false
                                    },
                                    onCancel = { showBottomSheet.value = false },
                                    viewModel = viewModel
                                )
                        }
                    }
                }

                LazyColumn {
                    if (state.workoutSets.isEmpty()) {
                        item {
                            Text(
                                "Brak serii dodanych do tego ćwiczenia.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    else {
                        itemsIndexed(state.workoutSets) { index, workoutSet ->
                            when (state.workoutType) {
                                WorkoutType.Cardio ->
                                    CardioWorkoutSetEntry(
                                        index,
                                        workoutSet.distance!!,
                                        workoutSet.time!!.toDuration(DurationUnit.SECONDS),
                                        onDelete = { workoutSetToDelete = workoutSet },
                                        viewModel = viewModel
                                    )

                                WorkoutType.Strength ->
                                    StrengthWorkoutSetEntry(
                                        index,
                                        workoutSet.load!!,
                                        workoutSet.reps!!,
                                        onDelete = { workoutSetToDelete = workoutSet },
                                        viewModel = viewModel
                                    )
                            }
                        }
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
                            viewModel.saveExerciseInstance() {
                                navController.popBackStack(Screen.Home.route, false)
                            }
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
                            .padding(end = 8.dp),
                        enabled = !state.isSaving && state.workoutSets.isNotEmpty()
                    ) {
                        Text(
                            text = "Zapisz",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelLarge,
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet.value = true
                },
                containerColor = lovelyPink,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.Black
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardioWorkoutSetCreator(
    onSave: (WorkoutSet) -> Unit,
    onCancel: () -> Unit,
    viewModel: ExerciseInstanceCreateViewModel = hiltViewModel()
) {
    var state = remember {
        mutableStateOf(DurationInputState())
    }
    var distance by remember { mutableStateOf("0") }
    val distanceUnit by viewModel.distanceUnit.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        DurationInput(state)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = distance,
            onValueChange = {
                distance = it
            },
            isError = distance.toDoubleOrNull() == null || distance.toDouble() < 0,
            label = { Text("Dystans") },
            suffix = { Text(distanceUnit) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    onSave(
                        WorkoutSet(
                            0,
                            0,
                            time = state.value.toDuration().toInt(DurationUnit.SECONDS),
                            distance = distance.toDouble()
                        )
                    )
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
                    .padding(end = 8.dp),
                enabled = state.value.hour.toIntOrNull() != null &&
                        state.value.minute.toIntOrNull() != null &&
                        state.value.second.toIntOrNull() != null &&
                        distance.toDoubleOrNull() != null &&
                        state.value.toDuration() > 0.toDuration(DurationUnit.SECONDS) &&
                        distance.toDouble() > 0
            ) {
                Text(
                    text = "Dodaj",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Button(
                onClick = onCancel,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StrengthWorkoutSetCreator(
    onSave: (WorkoutSet) -> Unit,
    onCancel: () -> Unit,
    viewModel: ExerciseInstanceCreateViewModel = hiltViewModel()
) {
    var reps by remember { mutableStateOf("0") }
    var load by remember { mutableStateOf("0") }
    val weightUnit by viewModel.weightUnit.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = load,
            onValueChange = {
                load = it
            },
            label = { Text("Obciążenie") },
            suffix = { Text(weightUnit) },
            isError = load.toDoubleOrNull() == null || load.toDouble() < 0,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = reps,
            onValueChange = {
                reps = it
            },
            label = { Text("Powtórzenia") },
            isError = reps.toIntOrNull() == null || reps.toInt() < 0,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    onSave(
                        WorkoutSet(
                            0,
                            0,
                            reps = reps.toInt(),
                            load = load.toDouble(),
                        )
                    )
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
                    .padding(end = 8.dp),
                enabled = load.toDoubleOrNull() != null && load.toDouble() > 0.0 &&
                        reps.toIntOrNull() != null && reps.toInt() > 0
            ) {
                Text(
                    text = "Dodaj",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Button(
                onClick = onCancel,
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
}

@Composable
//@Preview(showBackground = true)
fun WorkoutSetCreatorPreview() {
    val showBottomSheet = remember { mutableStateOf(true) }
    StrengthWorkoutSetCreator(onSave = {}, onCancel = {})
}

//@Preview
//@Composable
//fun ExerciseInstanceCreateScreenPreview() {
//    ExerciseInstanceCreateScreen(
//        navController = NavController(LocalContext.current),
//        exerciseKind = "Exercise Name",
//        workoutDate =
//    )
//}