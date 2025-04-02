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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uj.lab.fitnessapp.data.model.Exercise
import androidx.compose.runtime.*
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.ui.theme.*

@Composable
fun ExerciseKindListEntry(
    exercise: Exercise,
    onClick: () -> Unit
) {

    // depends on mock data model, will change soon.
    var isFavorite by remember { mutableStateOf(false) }
    val baseColor = if (exercise.workoutType == WorkoutType.Strength) strengthColor else cardioColor

    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = if (isFavorite) ButtonDefaults.buttonColors(containerColor = favoriteColor)
                else ButtonDefaults.buttonColors(containerColor = baseColor)
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
                    color = Color.White
                )

            }
            // attach function to handle tri-dot menu (if needed)
            IconButton(onClick = {  }) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        ,
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Options",
                    tint = Color.White
                )
            }
            // attach function to handle favorites
            IconButton(onClick = { isFavorite = !isFavorite }) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                    ,
                    imageVector = Icons.Default.Star,
                    contentDescription = "Add to favorites",
                    tint = Color.White
                )
            }
            }
        }
    }

@Preview(showBackground = true)
@Composable
fun ExerciseListEntryPreview() {
    ExerciseKindListEntry(
        exercise = Exercise(0, "Running", WorkoutType.Cardio, false, false),
        onClick = {}
    )
}
