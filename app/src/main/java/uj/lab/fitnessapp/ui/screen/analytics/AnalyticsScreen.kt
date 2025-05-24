package uj.lab.fitnessapp.ui.screen.analytics

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.ZoneId
import androidx.compose.runtime.*
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.ui.res.painterResource
import uj.lab.fitnessapp.R
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.navigation.Screen
import uj.lab.fitnessapp.ui.component.DateRangeSelector
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import androidx.compose.ui.unit.sp

private val RangeProvider: CartesianLayerRangeProvider = CartesianLayerRangeProvider.auto()
private val YDecimalFormat = DecimalFormat("#")
private val StartAxisValueFormatter = CartesianValueFormatter.decimal(YDecimalFormat)
private val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter.default(YDecimalFormat)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController, exerciseKind: String, modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableIntStateOf(1) }
    val viewModel: AnalyticsViewModel = hiltViewModel()
    val exerciseID by viewModel.exerciseID.collectAsState()
    val cardioData by viewModel.cardioChartData.collectAsState()
    val strengthData by viewModel.strengthChartData.collectAsState()
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
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            if (metrics.isNotEmpty()) {
                metrics.forEach { (label, value) ->
                    Text(
                        text = "$label: $value",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 4.dp),
                        color = MaterialTheme.colorScheme.onBackground
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
                    Text("Wykres 1", color = MaterialTheme.colorScheme.onPrimary)
                }
                Button(
                    onClick = { selectedTab = 2 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Wykres 2", color = MaterialTheme.colorScheme.onPrimary)
                }
                Button(
                    onClick = { selectedTab = 3 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Wykres 3", color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            exerciseType?.let { type ->
                ChartSelectionAndDisplay(
                    selectedTab = selectedTab,
                    exerciseType = type,
                    strengthData = strengthData,
                    cardioData = cardioData,
                    modifier = modifier
                )
            } ?: run {
                Text("Ładowanie danych...", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}

@Composable
private fun JetpackComposeBasicLineChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
    bottomAxisValueFormatter: CartesianValueFormatter,
    startAxisTitle: String? = null,
    bottomAxisTitle: String? = null,
) {
    val lineColor = Color(0xffa485e0)
    val axisTextColor = MaterialTheme.colorScheme.onBackground
    val axisLineColor = MaterialTheme.colorScheme.onSurfaceVariant
    val axisGuidelineColor = MaterialTheme.colorScheme.outlineVariant

    val AXIS_LABEL_SIZE = 12f
    val AXIS_LINE_WIDTH = 1f
    val AXIS_GUIDELINE_WIDTH = 1f

    val rememberAxisTextComponent = rememberTextComponent(
        color = axisTextColor,
        textSize = AXIS_LABEL_SIZE.sp,
    )

    val rememberAxisTitleComponent = rememberTextComponent(
        color = axisTextColor,
        textSize = AXIS_LABEL_SIZE.sp,
    )

    val rememberAxisLineComponent = rememberLineComponent(
        fill = fill(axisLineColor),
        thickness = AXIS_LINE_WIDTH.dp,
    )

    val rememberGuideline = rememberLineComponent(
        fill = fill(axisGuidelineColor),
        thickness = AXIS_GUIDELINE_WIDTH.dp,
    )

    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider =
                    LineCartesianLayer.LineProvider.series(
                        LineCartesianLayer.rememberLine(
                            fill = LineCartesianLayer.LineFill.single(fill(lineColor)),
                            areaFill =
                                LineCartesianLayer.AreaFill.single(
                                    fill(
                                        ShaderProvider.verticalGradient(
                                            arrayOf(lineColor.copy(alpha = 0.4f), Color.Transparent)
                                        )
                                    )
                                ),
                        )
                    ),
                rangeProvider = RangeProvider,
            ),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = StartAxisValueFormatter,
                title = startAxisTitle,
                label = rememberAxisTextComponent,
                titleComponent = rememberAxisTitleComponent,
                line = rememberAxisLineComponent,
                guideline = rememberGuideline,
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = bottomAxisValueFormatter,
                title = bottomAxisTitle,
                label = rememberAxisTextComponent,
                titleComponent = rememberAxisTitleComponent,
                line = rememberAxisLineComponent,
                guideline = rememberGuideline,
            ),
            marker = rememberMarker(MarkerValueFormatter),
        ),
        modelProducer,
        modifier.height(220.dp),
        rememberVicoScrollState(scrollEnabled = false),
    )
}

@Composable
private fun ChartContent(
    yValues: List<Number>,
    dates: List<String>,
    modifier: Modifier = Modifier,
    chartTitle: String,
    yAxisLabel: String,
    xAxisLabel: String = "Data",
) {
    val inputDateFormatter = remember { DateTimeFormatter.ofPattern("dd-MM-yyyy") }
    val outputDateFormatter = remember { DateTimeFormatter.ofPattern("dd-MM", Locale.getDefault()) }

    val xValues = remember(dates) {
        dates.map { dateString ->
            LocalDate.parse(dateString, inputDateFormatter)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }
    }

    val bottomAxisValueFormatter = remember(outputDateFormatter) {
        CartesianValueFormatter { _, valueToFormat: Double, _ ->
            val millisecondsPerDay = (1000.0 * 60 * 60 * 24)
            val epochDay = (valueToFormat / millisecondsPerDay).toLong()
            val date = LocalDate.ofEpochDay(epochDay)
            outputDateFormatter.format(date)
        }
    }

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(yValues, xValues) {
        if (yValues.isNotEmpty() && xValues.isNotEmpty()) {
            modelProducer.runTransaction {
                lineSeries { series(x = xValues, y = yValues.map { it.toDouble() }) }
            }
        }
    }

    Column {
        Text(
            text = chartTitle,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        JetpackComposeBasicLineChart(
            modelProducer,
            modifier,
            bottomAxisValueFormatter,
            startAxisTitle = yAxisLabel,
            bottomAxisTitle = xAxisLabel
        )
    }
}

@Composable
private fun ChartSelectionAndDisplay(
    selectedTab: Int,
    exerciseType: WorkoutType,
    strengthData: List<StrengthChartData>,
    cardioData: List<CardioChartData>,
    modifier: Modifier = Modifier
) {
    when (selectedTab) {
        1 -> {
            if (exerciseType == WorkoutType.Strength) {
                val yValues = strengthData.map { it.reps }
                val dates = strengthData.map { it.date }
                ChartContent(
                    yValues = yValues,
                    dates = dates,
                    modifier = modifier,
                    chartTitle = "Postęp powtórzeń",
                    yAxisLabel = "Powtórzenia"
                )
            } else if (exerciseType == WorkoutType.Cardio) {
                val yValues = cardioData.map { it.distance }
                val dates = cardioData.map { it.date }
                ChartContent(
                    yValues = yValues,
                    dates = dates,
                    modifier = modifier,
                    chartTitle = "Postęp dystansu",
                    yAxisLabel = "Dystans (km)"
                )
            }
        }
        2 -> {
            if (exerciseType == WorkoutType.Strength) {
                val yValues = strengthData.map { it.load }
                val dates = strengthData.map { it.date }
                ChartContent(
                    yValues = yValues,
                    dates = dates,
                    modifier = modifier,
                    chartTitle = "Postęp obciążenia",
                    yAxisLabel = "Obciążenie (kg)"
                )
            } else if (exerciseType == WorkoutType.Cardio) {
                val yValues = cardioData.map { it.time }
                val dates = cardioData.map { it.date }
                ChartContent(
                    yValues = yValues,
                    dates = dates,
                    modifier = modifier,
                    chartTitle = "Postęp czasu",
                    yAxisLabel = "Czas (h)"
                )
            }
        }
        3 -> {
            if (exerciseType == WorkoutType.Strength) {
                val yValues = strengthData.map { it.volume }
                val dates = strengthData.map { it.date }
                ChartContent(
                    yValues = yValues,
                    dates = dates,
                    modifier = modifier,
                    chartTitle = "Postęp objętości",
                    yAxisLabel = "Objętość"
                )
            } else if (exerciseType == WorkoutType.Cardio) {
                val yValues = cardioData.map { it.velocity }
                val dates = cardioData.map { it.date }
                ChartContent(
                    yValues = yValues,
                    dates = dates,
                    modifier = modifier,
                    chartTitle = "Postęp prędkości",
                    yAxisLabel = "Prędkość (km/h)"
                )
            }
        }
    }
}