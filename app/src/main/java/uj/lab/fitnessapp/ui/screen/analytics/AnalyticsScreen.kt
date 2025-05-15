package uj.lab.fitnessapp.ui.screen.analytics

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.navigation.Screen
import uj.lab.fitnessapp.ui.component.DateRangeSelector
import java.time.LocalDate

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController, exerciseKind: String) {
    var selectedTab by remember { mutableIntStateOf(1) }
    val viewModel: AnalyticsViewModel = hiltViewModel()
    val exerciseID by viewModel.exerciseID.collectAsState()
    val chartData by viewModel.chartData.collectAsState()
    val exerciseName by viewModel.exerciseName.collectAsState()
    val metrics by viewModel.metricsData.collectAsState()
    val exerciseType by viewModel.exerciseType.collectAsState()

    var selectedChip by remember { mutableStateOf("tydzień") }

    LaunchedEffect(exerciseKind) {
        viewModel.getExerciseIDFromKind(exerciseKind)
    }

    LaunchedEffect(exerciseID) {
        if (exerciseID > 0) {
            val startDate = LocalDate.now().minusDays(7)
            viewModel.getInstancesInTimeRange(exerciseID, startDate)
//            viewModel.getAllTimeInstances(exerciseID)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Analityka") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
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
                routes.forEach { (icon, screen) ->
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = exerciseName.uppercase(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (metrics.isNotEmpty()) {
                metrics.forEach { (label, value) ->
                    Text(
                        text = "$label: $value",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            DateRangeSelector(
                selected = selectedChip,
                onSelectedChange = {
                    selectedChip = it
                    when(it){
                        "tydzień" -> {
                            val startDate = LocalDate.now().minusDays(7)
                            viewModel.getInstancesInTimeRange(exerciseID, startDate)
                        }
                        "miesiąc" -> {
                            val startDate = LocalDate.now().minusMonths(1)
                            viewModel.getInstancesInTimeRange(exerciseID, startDate)
                        }
                        "kwartał" -> {
                            val startDate = LocalDate.now().minusMonths(3)
                            viewModel.getInstancesInTimeRange(exerciseID, startDate)
                        }
                        "rok" -> {
                            val startDate = LocalDate.now().minusYears(1)
                            viewModel.getInstancesInTimeRange(exerciseID, startDate)
                        }
                        "cały okres" -> {
                            viewModel.getAllTimeInstances(exerciseID)
                        }
                    }
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { selectedTab = 1 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Wykres 1")
                }
                Button(
                    onClick = { selectedTab = 2 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Wykres 2")
                }
                Button(
                    onClick = { selectedTab = 3 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Wykres 3")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (selectedTab) {
                1 -> if (exerciseType == WorkoutType.Cardio) {
                    CardioChart1(chartData, exerciseName)
                } else {
                    StrengthChart1(chartData, exerciseName)
                }
                2 -> if (exerciseType == WorkoutType.Cardio) {
                    CardioChart2(chartData, exerciseName)
                } else {
                    StrengthChart2(chartData, exerciseName)
                }
                3 -> if (exerciseType == WorkoutType.Cardio) {
                    CardioChart3(chartData, exerciseName)
                } else {
                    StrengthChart3(chartData, exerciseName)
                }
            }
        }
    }
}


@Composable
fun CardioChart1(data: List<Pair<String, Any>>, exerciseName: String) {
    key(data) {
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
                line.crosshair().yLabel(true).yStroke(
                    null as String?,
                    null as Number?,
                    null as String?,
                    null as String?,
                    null as String?
                )
                line.tooltip().positionMode(TooltipPositionMode.POINT)
                line.title("Postęp dystansu")
                line.yAxis(0).title("Dystans (m)")
                line.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

                val seriesData = data.filter { it.second is Int }.map { (date, value) ->
                    ValueDataEntry(date, value as Int)
                }

                val series = line.line(seriesData)
                series.name(exerciseName)
                series.hovered().markers().enabled(true)
                series.hovered().markers().type(com.anychart.enums.MarkerType.CIRCLE).size(4.0)
                series.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5.0)
                    .offsetY(5.0)

                line.legend().enabled(true).fontSize(13.0).padding(0.0, 0.0, 10.0, 0.0)

                chartView.setChart(line)
            }
        )
    }
}

@Composable
fun CardioChart2(data: List<Pair<String, Any>>, exerciseName: String) {
    key(data) {
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
                line.crosshair().yLabel(true).yStroke(
                    null as String?,
                    null as Number?,
                    null as String?,
                    null as String?,
                    null as String?
                )
                line.tooltip().positionMode(TooltipPositionMode.POINT)
                line.title("Postęp czasu")
                line.yAxis(0).title("Czas (s)")
                line.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

                val seriesData = data.filter { it.second is Int }.map { (date, value) ->
                    ValueDataEntry(date, value as Int)
                }

                val series = line.line(seriesData)
                series.name(exerciseName)
                series.hovered().markers().enabled(true)
                series.hovered().markers().type(com.anychart.enums.MarkerType.CIRCLE).size(4.0)
                series.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5.0)
                    .offsetY(5.0)

                line.legend().enabled(true).fontSize(13.0).padding(0.0, 0.0, 10.0, 0.0)

                chartView.setChart(line)
            }
        )
    }
}

@Composable
fun CardioChart3(data: List<Pair<String, Any>>, exerciseName: String) {
    key(data) {
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
                line.crosshair().yLabel(true).yStroke(
                    null as String?,
                    null as Number?,
                    null as String?,
                    null as String?,
                    null as String?
                )
                line.tooltip().positionMode(TooltipPositionMode.POINT)
                line.title("Postęp maksymalnego dystansu")
                line.yAxis(0).title("Maksymalny dystans (m)")
                line.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

                val seriesData = data.filter { it.second is Int }.map { (date, value) ->
                    ValueDataEntry(date, value as Int)
                }

                val series = line.line(seriesData)
                series.name(exerciseName)
                series.hovered().markers().enabled(true)
                series.hovered().markers().type(com.anychart.enums.MarkerType.CIRCLE).size(4.0)
                series.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5.0)
                    .offsetY(5.0)

                line.legend().enabled(true).fontSize(13.0).padding(0.0, 0.0, 10.0, 0.0)

                chartView.setChart(line)
            }
        )
    }
}

@Composable
fun StrengthChart1(data: List<Pair<String, Any>>, exerciseName: String) {
    key(data){
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
                line.crosshair().yLabel(true).yStroke(
                    null as String?,
                    null as Number?,
                    null as String?,
                    null as String?,
                    null as String?
                )
                line.tooltip().positionMode(TooltipPositionMode.POINT)
                line.title("Postęp powtórzeń")
                line.yAxis(0).title("Powtórzenia")
                line.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

                val seriesData = data.filter { it.second is Int }.map { (date, value) ->
                    ValueDataEntry(date, value as Int)
                }

                val series = line.line(seriesData)
                series.name(exerciseName)
                series.hovered().markers().enabled(true)
                series.hovered().markers().type(com.anychart.enums.MarkerType.CIRCLE).size(4.0)
                series.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5.0).offsetY(5.0)

                line.legend().enabled(true).fontSize(13.0).padding(0.0, 0.0, 10.0, 0.0)

                chartView.setChart(line)
            }
        )
    }
}

@Composable
fun StrengthChart2(data: List<Pair<String, Any>>, exerciseName: String) {
    key(data) {
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
                line.crosshair().yLabel(true).yStroke(
                    null as String?,
                    null as Number?,
                    null as String?,
                    null as String?,
                    null as String?
                )
                line.tooltip().positionMode(TooltipPositionMode.POINT)
                line.title("Postęp objętości treningu")
                line.yAxis(0).title("Objętość treningu")
                line.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

                val seriesData = data.filter { it.second is Double }.map { (date, value) ->
                    ValueDataEntry(date, value as Double)
                }

                val series = line.line(seriesData)
                series.name(exerciseName)
                series.hovered().markers().enabled(true)
                series.hovered().markers().type(com.anychart.enums.MarkerType.CIRCLE).size(4.0)
                series.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5.0)
                    .offsetY(5.0)

                line.legend().enabled(true).fontSize(13.0).padding(0.0, 0.0, 10.0, 0.0)

                chartView.setChart(line)
            }
        )
    }
}
@Composable
fun StrengthChart3(data: List<Pair<String, Any>>, exerciseName: String) {
    key(data) {
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
                line.crosshair().yLabel(true).yStroke(
                    null as String?,
                    null as Number?,
                    null as String?,
                    null as String?,
                    null as String?
                )
                line.tooltip().positionMode(TooltipPositionMode.POINT)
                line.title("Postęp szacowanego 1RM")
                line.yAxis(0).title("Szacowany 1RM")
                line.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

                val seriesData = data.filter { it.second is Double }.map { (date, value) ->
                    ValueDataEntry(date, value as Double)
                }

                val series = line.line(seriesData)
                series.name(exerciseName)
                series.hovered().markers().enabled(true)
                series.hovered().markers().type(com.anychart.enums.MarkerType.CIRCLE).size(4.0)
                series.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5.0)
                    .offsetY(5.0)

                line.legend().enabled(true).fontSize(13.0).padding(0.0, 0.0, 10.0, 0.0)

                chartView.setChart(line)
            }
        )
    }
}
