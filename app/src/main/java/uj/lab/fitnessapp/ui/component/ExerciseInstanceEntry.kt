package uj.lab.fitnessapp.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import uj.lab.fitnessapp.R
import uj.lab.fitnessapp.data.model.Exercise
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.data.model.WorkoutSet
import uj.lab.fitnessapp.ui.screen.home.HomeViewModel
import androidx.compose.ui.unit.sp
import kotlin.time.Duration.Companion.seconds
import uj.lab.fitnessapp.data.utils.UnitConverter
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun ExerciseInstanceEntry(
    index: Int,
    instance: ExerciseInstanceWithDetails,
    onFavoriteClick: (Exercise) -> Unit,
    onAnalyticsClick: (Exercise) -> Unit,
    onDelete: () -> Unit,
    onEditClick: (ExerciseInstanceWithDetails) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val isImperialWeight by homeViewModel.isImperialWeight.collectAsState()
    val isImperialDistance by homeViewModel.isImperialDistance.collectAsState()

    ElevatedCard(modifier = Modifier.padding(12.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end=16.dp, top = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                "${index + 1}. ${instance.exercise?.exerciseName ?: "Unknown Exercise"}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ) }
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 2.dp, end=2.dp, top = 0.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top
            ) {
                Row {
                    IconButton(onClick = { onEditClick(instance) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit_24px),
                            contentDescription = "Edit exercise",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = { onAnalyticsClick(instance.exercise!!) }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.baseline_auto_graph_24),
                            contentDescription = "Go to Analytics",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete exercise",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = { onFavoriteClick(instance.exercise!!) }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(
                                id = if (instance.exercise!!.isFavourite)
                                    R.drawable.baseline_star_32 else R.drawable.baseline_star_outline_32
                            ),
                            contentDescription = "Add to favorites",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .height(((instance.seriesList!!.size * 122) - ((instance.seriesList!!.size - 1) * 16)).dp)
                    .padding(8.dp),
                userScrollEnabled = false
            ) {
                itemsIndexed(instance.seriesList!!) { setIndex, workoutSet ->
                    when (instance.exercise!!.workoutType) {
                        WorkoutType.Cardio ->
                            CardioWorkoutSetEntry(
                                setIndex = setIndex,
                                distance = workoutSet.distance!!,
                                time = (workoutSet.time ?: 0).seconds,
                                onDelete = null,
                                onEdit = null,
                                viewModel = null,
                                isImperial = isImperialDistance
                            )

                        WorkoutType.Strength ->
                            StrengthWorkoutSetEntry(
                                setIndex = setIndex,
                                load = workoutSet.load!!,
                                reps = workoutSet.reps!!,
                                onDelete = null,
                                onEdit = null,
                                viewModel = null,
                                isImperial = isImperialWeight
                            )
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutSetEditDialog(
    workoutSet: WorkoutSet,
    isImperial: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (WorkoutSet) -> Unit
) {
    var reps by remember { mutableStateOf(workoutSet.reps?.toString() ?: "") }
    var load by remember { mutableStateOf(workoutSet.load?.let { 
        UnitConverter.displayWeight(it, isImperial).first.toString() 
    } ?: "") }
    var time by remember { mutableStateOf(workoutSet.time?.toString() ?: "") }
    var distance by remember { mutableStateOf(workoutSet.distance?.let { 
        UnitConverter.displayDistance(it, isImperial).first.toString() 
    } ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edytuj serię") },
        text = {
            Column {
                OutlinedTextField(
                    value = reps,
                    onValueChange = { reps = it },
                    label = { Text("Powtórzenia") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = load,
                    onValueChange = { load = it },
                    label = { Text("Ciężar") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Czas (sekundy)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = distance,
                    onValueChange = { distance = it },
                    label = { Text("Dystans") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val updatedSet = workoutSet.copy(
                        reps = reps.toIntOrNull(),
                        load = load.toDoubleOrNull()?.let { UnitConverter.storeWeight(it, isImperial) },
                        time = time.toIntOrNull(),
                        distance = distance.toDoubleOrNull()?.let { UnitConverter.storeDistance(it, isImperial) }
                    )
                    onConfirm(updatedSet)
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
