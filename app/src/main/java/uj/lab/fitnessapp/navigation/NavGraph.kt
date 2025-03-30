package uj.lab.fitnessapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uj.lab.fitnessapp.ui.screen.exercises.kindlist.ExerciseListScreen
import uj.lab.fitnessapp.ui.screen.home.HomeScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.ExerciseKindList.route) {
            ExerciseListScreen()
        }
    }
}