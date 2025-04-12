package uj.lab.fitnessapp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails
import uj.lab.fitnessapp.data.model.WorkoutType
import kotlin.time.Duration.Companion.seconds

@Composable
fun ExerciseInstanceEntry(index: Int, instance: ExerciseInstanceWithDetails) {
    val name = instance.exercise?.exerciseName ?: "Unknown Exercise"
    val height = instance.seriesList!!.size * 122
    val paddings = (instance.seriesList!!.size - 1) * 16
    ElevatedCard(modifier = Modifier.padding(16.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "${index + 1}. $name",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
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


