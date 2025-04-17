package uj.lab.fitnessapp.ui.screen.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import uj.lab.fitnessapp.navigation.Screen
import uj.lab.fitnessapp.ui.theme.backgroundColor
import uj.lab.fitnessapp.ui.theme.green1
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.text.font.FontWeight


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
    val currentWeightUnit by viewModel.weightUnit.collectAsState()
    val themeText by viewModel.themeText
    var expandedDistanceUnits by remember { mutableStateOf(false) }
    var expandedWeightUnits by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Ustawienia") },
                colors = TopAppBarColors(
                    containerColor = green1,
                    scrolledContainerColor = green1,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar {
                val routes = listOf(
                    Icons.Default.Home to Screen.Home,
                    Icons.Default.Info to null,
                    Icons.Default.Settings to Screen.Settings
                )
                routes.forEach {
                    NavigationBarItem(
                        icon = { Icon(it.first, contentDescription = null) },
                        selected = navController.currentDestination?.route == it.second?.route,
                        onClick = {
                            it.second?.let { screen ->
                                if (navController.currentDestination?.route != screen.route) {
                                    navController.navigate(screen.route)
                                }
                            }
                        }
                    )
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
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
                    Text(text = "Jednostki dystansu")
                    Box {
                        TextButton(
                            onClick = { expandedDistanceUnits = true },
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(currentDistanceUnit.displayName)
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = "Otwórz menu jednostek dystansu",
                                    tint = green1
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                            }
                        }
                        DropdownMenu(
                            expanded = expandedDistanceUnits,
                            onDismissRequest = { expandedDistanceUnits = false }
                        ) {
                            viewModel.distanceUnits.forEach { unit ->
                                DropdownMenuItem(
                                    text = { Text(unit.displayName) },
                                    onClick = {
                                        viewModel.setDistanceUnit(unit)
                                        expandedDistanceUnits = false
                                    }
                                )
                            }
                        }
                    }
                }
                // Weight Unit Setting
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Jednostki wagi")
                    Box {
                        TextButton(
                            onClick = { expandedWeightUnits = true },
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(currentWeightUnit.displayName)
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = "Otwórz menu jednostek wagi",
                                    tint = green1
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                            }
                        }
                        DropdownMenu(
                            expanded = expandedWeightUnits,
                            onDismissRequest = { expandedWeightUnits = false }
                        ) {
                            viewModel.weightUnits.forEach { unit ->
                                DropdownMenuItem(
                                    text = { Text(unit.displayName) },
                                    onClick = {
                                        viewModel.setWeightUnit(unit)
                                        expandedWeightUnits = false
                                    }
                                )
                            }
                        }
                    }
                }
                // Synchronization Data Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Synchronizacja danych", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                }
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Color.Black)
                // Export/Import Buttons Column
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            // TODO: data export logic
                            println("Export Data Clicked")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = green1),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "Eksportuj dane",
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            // TODO: data import logic
                            println("Import Data Clicked")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = green1),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "Importuj dane",
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    )
}