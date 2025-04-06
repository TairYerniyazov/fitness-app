package uj.lab.fitnessapp.ui.screen.exercises.kindlist

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import uj.lab.fitnessapp.R
import uj.lab.fitnessapp.data.model.Exercise
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.ui.theme.cardioColor
import uj.lab.fitnessapp.ui.theme.favoriteColor
import uj.lab.fitnessapp.ui.theme.goldColor
import uj.lab.fitnessapp.ui.theme.lovelyPink
import uj.lab.fitnessapp.ui.theme.strengthColor

data class Filter(
    val index: Int,
    val icon: @Composable () -> ImageVector,
    val description: String,
    val color: androidx.compose.ui.graphics.Color,
    val predicate: (Exercise) -> Boolean
)

val exerciseFilters = listOf(
    Filter(
        index = 0,
        icon = { ImageVector.vectorResource(R.drawable.exercise_24px) },
        description = "Strength",
        color = strengthColor,
        predicate ={ it.workoutType == WorkoutType.Strength }
    ),
    Filter(
        index = 1,
        icon = { ImageVector.vectorResource(R.drawable.directions_run_24px) },
        description = "Cardio",
        color = cardioColor,
        predicate ={ it.workoutType == WorkoutType.Cardio }
    ),
    Filter(
        index = 2,
        icon = { Icons.Default.Favorite },
        description = "Favorites",
        color = favoriteColor,
        predicate ={ it.isFavourite }
    ),
    Filter(
        index = 3,
        icon = { Icons.Default.MoreVert },
        description = "All",
        color = lovelyPink,
        predicate ={ true }
    ),
)
fun getFilters(): List<Filter> {
    return exerciseFilters
}