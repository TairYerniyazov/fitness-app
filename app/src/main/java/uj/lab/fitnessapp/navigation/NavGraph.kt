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
import uj.lab.fitnessapp.ui.screen.analytics.ExerciseToAnalyseListScreen
import uj.lab.fitnessapp.ui.screen.exercises.createview.ExerciseKindCreateScreen
import androidx.compose.ui.Modifier

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.ExerciseKindList.route) {
            ExerciseKindListScreen(navController)
        }
        composable(
            Screen.ExerciseInstanceCreate.route,
            arguments = listOf(
                navArgument("exerciseKind") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val exerciseKind = backStackEntry.arguments?.getString("exerciseKind") ?: "Nieznane ćwiczenie"
            ExerciseInstanceCreateScreen(navController, exerciseKind)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
        composable(
            Screen.Analytics.route,
            arguments = listOf(
                navArgument("exerciseKind") { type = NavType.StringType })
        ) { backStackEntry ->
            val exerciseKind = backStackEntry.arguments?.getString("exerciseKind") ?: "Nieznane ćwiczenie"
            if (exerciseKind != "{exerciseKind}") {
                AnalyticsScreen(navController, exerciseKind, Modifier)
            }
            else {
                ExerciseToAnalyseListScreen(navController)
            }
        }
        composable(
            route = Screen.EditExerciseInstance.route,
            arguments = listOf(
                navArgument("exerciseKind") { type = NavType.StringType },
                navArgument("instanceId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val exerciseKind = backStackEntry.arguments?.getString("exerciseKind") ?: ""
            val instanceId = backStackEntry.arguments?.getInt("instanceId")

            ExerciseInstanceCreateScreen(
                navController = navController,
                exerciseKind = exerciseKind,
                instanceId = instanceId
            )
        }
        composable(
            Screen.ExerciseKindCreate.route,
            arguments = listOf(navArgument("selectedFilters") { type = NavType.StringType})
        ) { backStackEntry ->
            val selectedFiltersString = backStackEntry.arguments?.getString("selectedFilters") ?: "Nieznany filtr ćwiczeń"
            val selectedFilters = selectedFiltersString.split(",")
            ExerciseKindCreateScreen(navController, selectedFilters)
        }
        composable(
            route = Screen.EditExerciseKind.route,
            arguments = listOf(
                navArgument("kindId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val kindId = backStackEntry.arguments?.getInt("kindId")
            ExerciseKindCreateScreen(
                navController = navController,
                selectedFilters = emptyList(),
                kindId = kindId
            )
        }
    }
}