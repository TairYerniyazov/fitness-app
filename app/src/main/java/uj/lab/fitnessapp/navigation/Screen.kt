package uj.lab.fitnessapp.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object ExerciseKindList : Screen("exercise_kind_list")
}