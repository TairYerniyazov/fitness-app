package uj.lab.fitnessapp.ui.screen.settings


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import uj.lab.fitnessapp.navigation.Screen
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import uj.lab.fitnessapp.ui.theme.green1
/**
 * Settings screen.
 * Screen 5 in figma.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    var isDarkTheme by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(viewModel.themeText)
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { newValue ->
                            isDarkTheme = newValue
                            viewModel.toggleTheme(isDarkTheme)
                            viewModel.themeText = "Dark Mode"
                        }
                    )
                }
                // You can add more settings options here
            }
        }
    )
}