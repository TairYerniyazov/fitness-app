package uj.lab.fitnessapp.ui.screen.settings


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    val viewModel: SettingsViewModel = hiltViewModel()
    val isDarkTheme by viewModel.isDarkTheme
    val currentDistanceUnit by viewModel.distanceUnit.collectAsState()
    val themeText by viewModel.themeText
    var expanded by remember { mutableStateOf(false) }
    val bottomNavItems = listOf(Screen.Home, Screen.Settings)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Dark Mode Setting
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(themeText)
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { newValue ->
                            viewModel.toggleTheme(newValue)
                        }
                    )
                }
                // Distance Unit Setting
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Distance Unit")
                    Box {
                        TextButton(onClick = { expanded = true }) {
                            Text(currentDistanceUnit.displayName)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            viewModel.distanceUnits.forEach { unit ->
                                DropdownMenuItem(
                                    text = { Text(unit.displayName) },
                                    onClick = {
                                        viewModel.setDistanceUnit(unit)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}