package uj.lab.fitnessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import uj.lab.fitnessapp.navigation.NavigationWrapper
import uj.lab.fitnessapp.ui.theme.FitnessAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FitnessAppTheme {
                NavigationWrapper()
            }
        }
    }
}