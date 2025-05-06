package uj.lab.fitnessapp.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uj.lab.fitnessapp.R
import uj.lab.fitnessapp.data.model.Exercise
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.ui.theme.*

@Composable
fun ExerciseKindListEntry(
    exercise: Exercise,
    onClick: () -> Unit,
    onFavoriteClick: (Exercise) -> Unit
) {

    val bgColor = if (exercise.workoutType == WorkoutType.Strength)
        MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.secondaryContainer
    val textColor = if (exercise.workoutType == WorkoutType.Strength)
        MaterialTheme.colorScheme.onPrimaryContainer
    else MaterialTheme.colorScheme.onSecondaryContainer
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = bgColor)
    ) {
        Row(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        )
        {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exercise.exerciseName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

            }
            // attach function to handle favorites
            IconButton(onClick = { onFavoriteClick(exercise) }) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                    ,
                    painter = painterResource(id = if (exercise.isFavourite)
                        R.drawable.baseline_star_32 else R.drawable.baseline_star_outline_32),
                    contentDescription = "Add to favorites",
                    tint = textColor
                )
            }
            }
        }
    }

//@Preview(showBackground = true)
//@Composable
//fun ExerciseKindListEntryPreview() {
//    ExerciseKindListEntry(
//        exercise = Exercise(0, "Running", WorkoutType.Cardio, false, false),
//        onClick = {}
//    )
//}
