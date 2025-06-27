package uj.lab.fitnessapp.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uj.lab.fitnessapp.data.model.WorkoutSet
import uj.lab.fitnessapp.data.utils.UnitConverter
import uj.lab.fitnessapp.ui.screen.exercises.createview.ExerciseInstanceCreateViewModel

@Composable
fun CardioWorkoutSetEditDialog(
    workoutSet: WorkoutSet,
    onDismiss: () -> Unit,
    onSave: (WorkoutSet) -> Unit,
    viewModel: ExerciseInstanceCreateViewModel = hiltViewModel()
) {
    val isImperial by viewModel.isImperialDistance.collectAsState()
    
    // Convert stored distance (meters) to display units
    val (initialDisplayDistance, distanceUnit) = UnitConverter.displayDistance(workoutSet.distance ?: 0.0, isImperial)
    
    var distance by remember { mutableStateOf(initialDisplayDistance.toString()) }
    var timeSeconds by remember { mutableStateOf((workoutSet.time ?: 0).toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edytuj serię") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = distance,
                    onValueChange = { distance = it },
                    label = { Text("Dystans ($distanceUnit)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = timeSeconds,
                    onValueChange = { timeSeconds = it },
                    label = { Text("Czas (sekundy)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newDisplayDistance = distance.toDoubleOrNull() ?: 0.0
                    // Convert back to meters for storage
                    val newDistanceMeters = UnitConverter.storeDistance(newDisplayDistance, isImperial)
                    val newTime = timeSeconds.toIntOrNull() ?: 0
                    onSave(
                        workoutSet.copy(
                            distance = newDistanceMeters,
                            time = newTime
                        )
                    )
                }
            ) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}

@Composable
fun StrengthWorkoutSetEditDialog(
    workoutSet: WorkoutSet,
    onDismiss: () -> Unit,
    onSave: (WorkoutSet) -> Unit,
    viewModel: ExerciseInstanceCreateViewModel = hiltViewModel()
) {
    val isImperial by viewModel.isImperialWeight.collectAsState()
    
    // Convert stored weight (kg) to display units
    val (initialDisplayLoad, weightUnit) = UnitConverter.displayWeight(workoutSet.load ?: 0.0, isImperial)
    
    var load by remember { mutableStateOf(initialDisplayLoad.toString()) }
    var reps by remember { mutableStateOf((workoutSet.reps ?: 0).toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edytuj serię") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = load,
                    onValueChange = { load = it },
                    label = { Text("Obciążenie ($weightUnit)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = reps,
                    onValueChange = { reps = it },
                    label = { Text("Powtórzenia") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newDisplayLoad = load.toDoubleOrNull() ?: 0.0
                    // Convert back to kg for storage
                    val newLoadKg = UnitConverter.storeWeight(newDisplayLoad, isImperial)
                    val newReps = reps.toIntOrNull() ?: 0
                    onSave(
                        workoutSet.copy(
                            load = newLoadKg,
                            reps = newReps
                        )
                    )
                }
            ) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}

