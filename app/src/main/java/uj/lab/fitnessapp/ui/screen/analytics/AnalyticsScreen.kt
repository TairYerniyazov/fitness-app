package uj.lab.fitnessapp.ui.screen.analytics

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import uj.lab.fitnessapp.R
import uj.lab.fitnessapp.navigation.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController, exerciseKind: String) {
    val viewModel: AnalyticsViewModel = hiltViewModel()
    val exerciseID by viewModel.exerciseID.collectAsState()
    val chartData by viewModel.chartData.collectAsState()

    LaunchedEffect(exerciseKind) {
        viewModel.getExerciseIDFromKind(exerciseKind)
    }

    LaunchedEffect(exerciseID) {
        if (exerciseID > 0) { // Ensure exerciseID is valid
            viewModel.getAllInstancesByExerciseID(exerciseID)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Analityka") },
                colors = TopAppBarColors(
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
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Analiza dla: $exerciseKind",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
                if (chartData.isNotEmpty()) {
                    ChartView(chartData)
                } else {
                    Text(
                        "Brak danych do wyświetlenia dla tego ćwiczenia.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun ChartView(data: List<Pair<String, Int>>) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            AnyChartView(context)
        },
        update = { chartView ->
            val line = AnyChart.line()

            line.animation(true)

            line.padding(10.0, 20.0, 5.0, 20.0)

            line.crosshair().enabled(true)
            line.crosshair()
                .yLabel(true)
                .yStroke(null as String?, null as Number?, null as String?, null as String?, null as String?)

            line.tooltip().positionMode(TooltipPositionMode.POINT)

            line.title("Postęp ćwiczenia")

            line.yAxis(0).title("Wartość (np. dystans, suma obciążeń)")
            line.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

            val seriesData = data.map { (date, value) ->
                ValueDataEntry(date, value)
            }

            val series = line.line(seriesData)
            series.name(data.firstOrNull()?.first?.substringBefore("-") ?: "Dane") // Example name
            series.hovered().markers().enabled(true)
            series.hovered().markers()
                .type(com.anychart.enums.MarkerType.CIRCLE)
                .size(4.0)
            series.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5.0)
                .offsetY(5.0)

            line.legend().enabled(true)
            line.legend().fontSize(13.0)
            line.legend().padding(0.0, 0.0, 10.0, 0.0)

            chartView.setChart(line)
        }
    )
}