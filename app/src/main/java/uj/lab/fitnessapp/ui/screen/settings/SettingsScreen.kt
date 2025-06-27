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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import uj.lab.fitnessapp.navigation.Screen
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import uj.lab.fitnessapp.R


/**
 * Settings screen.
 * Screen 5 in figma.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val isDarkTheme = viewModel.isDarkTheme
    val currentDistanceUnit by viewModel.distanceUnit.collectAsState()
    val currentWeightUnit by viewModel.weightUnit.collectAsState()
    var expandedDistanceUnits by remember { mutableStateOf(false) }
    var expandedWeightUnits by remember { mutableStateOf(false) }

    // KOLOR = MaterialTheme.colorScheme.cośtam
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            TopAppBar(
                title = { Text("Ustawienia") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                val routes = listOf(
                    Icons.Default.Home to Screen.Home,
                    null to Screen.Analytics,
                    Icons.Default.Settings to Screen.Settings
                )
                routes.forEach { it ->
                    val (icon, screen) = it
                    NavigationBarItem(
                        icon = {
                            if (screen == Screen.Analytics) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_auto_graph_24),
                                    contentDescription = "Analytics",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                icon?.let {
                                    Icon(
                                        it,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        selected = navController.currentDestination?.route == screen.route,
                        onClick = {
                            if (navController.currentDestination?.route != screen.route) {
                                navController.navigate(screen.route)
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
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tryb ciemny",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { viewModel.toggleTheme() }
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
                                    tint = MaterialTheme.colorScheme.primary
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
                                    tint = MaterialTheme.colorScheme.primary
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
                // App info
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "June 2025, v1.4",
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}