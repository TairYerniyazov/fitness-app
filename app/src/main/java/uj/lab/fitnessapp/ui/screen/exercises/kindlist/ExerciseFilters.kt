package uj.lab.fitnessapp.ui.screen.exercises.kindlist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import uj.lab.fitnessapp.R
import uj.lab.fitnessapp.data.model.Exercise
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.ui.theme.cardioColor
import uj.lab.fitnessapp.ui.theme.favoriteColor
import uj.lab.fitnessapp.ui.theme.goldColor
import uj.lab.fitnessapp.ui.theme.strengthColor

data class Filter(
    val index: Int,
    val icon: @Composable () -> ImageVector,
    val description: String,
    val color: FilterColors,
    val predicate: (Exercise) -> Boolean
)
enum class FilterColors {
    Strength,Cardio,Favorite,User
}

val exerciseFilters = listOf(
    Filter(
        index = 0,
        icon = { ImageVector.vectorResource(R.drawable.exercise_24px) },
        description = "Strength",
        color = FilterColors.Strength,
        predicate ={ it.workoutType == WorkoutType.Strength }
    ),
    Filter(
        index = 1,
        icon = { ImageVector.vectorResource(R.drawable.directions_run_24px) },
        description = "Cardio",
        color = FilterColors.Cardio,
        predicate ={ it.workoutType == WorkoutType.Cardio }
    ),
    Filter(
        index = 2,
        icon = { Icons.Default.Star },
        description = "Favorites",
        color = FilterColors.Favorite,
        predicate ={ it.isFavourite }
    ),
    Filter(
        index = 3,
        icon = { Icons.Default.AccountCircle },
        description = "User Created",
        color = FilterColors.User,
        predicate ={ it.canModify }
    ),
)
fun getFilters(): List<Filter> {
    return exerciseFilters
}