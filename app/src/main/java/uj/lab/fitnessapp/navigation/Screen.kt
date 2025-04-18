package uj.lab.fitnessapp.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Settings : Screen("settings")
    data object Analytics : Screen("analytics")
    data object ExerciseKindList : Screen("exercise_kind_list")
    data object ExerciseInstanceCreate : Screen("exercise_instance_create/{exerciseKind}") {
        fun withArgs(exerciseKind: String): String {
            return "exercise_instance_create/$exerciseKind"
        }
    }
}