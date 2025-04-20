package uj.lab.fitnessapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import uj.lab.fitnessapp.ui.screen.exercises.kindlist.ExerciseKindListScreen
import uj.lab.fitnessapp.ui.screen.home.HomeScreen
import uj.lab.fitnessapp.ui.screen.exercises.createview.ExerciseInstanceCreateScreen
import uj.lab.fitnessapp.ui.screen.settings.SettingsScreen
import uj.lab.fitnessapp.ui.screen.analytics.AnalyticsScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(
            Screen.ExerciseKindList.route,
            arguments = listOf(navArgument("workoutDate") { type = NavType.StringType })
        ) { backStackEntry ->
            val workoutDate = backStackEntry.arguments?.getString("workoutDate") ?: ""
            ExerciseKindListScreen(navController, workoutDate)
        }
        composable(
            Screen.ExerciseInstanceCreate.route,
            arguments = listOf(
                navArgument("exerciseKind") { type = NavType.StringType },
                navArgument("workoutDate") { type = NavType.StringType })
        ) { backStackEntry ->
            val exerciseKind = backStackEntry.arguments?.getString("exerciseKind") ?: "Nieznane ćwiczenie"
            val workoutDate = backStackEntry.arguments?.getString("workoutDate") ?: "Nieznana data"
            ExerciseInstanceCreateScreen(navController, exerciseKind, workoutDate)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
        composable(Screen.Analytics.route) {
            AnalyticsScreen(navController)
        }
    }
}