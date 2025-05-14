package uj.lab.fitnessapp.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Settings : Screen("settings")
    data object Analytics : Screen("analytics/{exerciseKind}"){
        fun withArgs(exerciseKind: String): String {
            return "analytics/$exerciseKind"
        }
    }
    data object ExerciseKindList : Screen("exercise_kind_list/{workoutDate}"){
        fun withArgs(workoutDate: String): String {
            return "exercise_kind_list/$workoutDate"
        }
    }
    data object ExerciseInstanceCreate : Screen("exercise_instance_create/{exerciseKind}/{workoutDate}") {
        fun withArgs(exerciseKind: String, workoutDate: String): String {
            return "exercise_instance_create/$exerciseKind/$workoutDate"
        }
    }
    data object EditExerciseInstance : Screen("edit_exercise_instance/{exerciseKind}/{workoutDate}/{instanceId}") {
        fun createRoute(exerciseKind: String, workoutDate: String, instanceId: Int): String {
            return "edit_exercise_instance/$exerciseKind/$workoutDate/$instanceId"
        }
    }
    data object ExerciseKindCreate : Screen("exercise_kind_create/{selectedFilters}") {
        fun withArgs(selectedFilters: List<String>): String {
            val selectedFiltersString = selectedFilters.joinToString(",")
            return "exercise_kind_create/$selectedFiltersString"
        }
    }
}