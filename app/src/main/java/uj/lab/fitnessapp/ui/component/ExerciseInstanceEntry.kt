package uj.lab.fitnessapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uj.lab.fitnessapp.R
import uj.lab.fitnessapp.data.model.Exercise
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails
import uj.lab.fitnessapp.data.model.WorkoutType
import kotlin.time.Duration.Companion.seconds

@Composable
fun ExerciseInstanceEntry(
    index: Int,
    instance: ExerciseInstanceWithDetails,
    onFavoriteClick: (Exercise) -> Unit,
    onAnalyticsClick: (Exercise) -> Unit,
    onDelete: () -> Unit,
    onEditClick: (ExerciseInstanceWithDetails) -> Unit
) {
    val name = instance.exercise?.exerciseName ?: "Unknown Exercise"
    val height = instance.seriesList!!.size * 122
    val paddings = (instance.seriesList!!.size - 1) * 16
    ElevatedCard(modifier = Modifier.padding(12.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Row (
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end=16.dp, top = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                "${index + 1}. $name",
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
                    .height(((height - paddings)).dp)
                    .padding(8.dp),
                userScrollEnabled = false
            ) {
                itemsIndexed(instance.seriesList!!) { index, set ->
                    when (instance.exercise!!.workoutType) {
                        WorkoutType.Cardio ->
                            CardioWorkoutSetEntry(index, set.distance!!, set.time!!.seconds)

                        WorkoutType.Strength ->
                            StrengthWorkoutSetEntry(index, set.load!!, set.reps!!)
                    }
                }
            }
        }
    }
}
