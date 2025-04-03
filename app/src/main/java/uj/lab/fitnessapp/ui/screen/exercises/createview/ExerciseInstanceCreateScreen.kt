package uj.lab.fitnessapp.ui.screen.exercises.createview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uj.lab.fitnessapp.ui.theme.backgroundColor
import uj.lab.fitnessapp.ui.theme.green1
import uj.lab.fitnessapp.ui.theme.lovelyPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseInstanceCreateScreen(navController: NavController, exerciseKind: String) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = backgroundColor,
                    shape = RoundedCornerShape(4.dp),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = exerciseKind,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }, colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = backgroundColor))
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .background(backgroundColor)
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showBottomSheet.value) {
                    ModalBottomSheet(onDismissRequest = {
                        showBottomSheet.value = false
                    }, sheetState = sheetState) { }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    FloatingActionButton(
                        onClick = {
                            //TODO: Implement add series logic
                        },
                        containerColor = lovelyPink,
                        shape = CircleShape,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.Black
                        )
                    }
                }

            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = backgroundColor
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            //TODO: Implement save instance logic
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = green1),
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = "Zapisz",
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Button(
                        onClick = {
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = green1),
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            text = "Anuluj",
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSetCreator(showBottomSheet: MutableState<Boolean>) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    //TODO: Implement save instance logic
                },
                colors = ButtonDefaults.buttonColors(containerColor = green1),
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = "Dodaj",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Button(
                onClick = {
                },
                colors = ButtonDefaults.buttonColors(containerColor = green1),
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = "Anuluj",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
//@Preview(showBackground = true)
fun WorkoutSetCreatorPreview() {
    val showBottomSheet = remember { mutableStateOf(true) }
    WorkoutSetCreator(showBottomSheet)
}

@Preview
@Composable
fun ExerciseInstanceCreateScreenPreview() {
    ExerciseInstanceCreateScreen(
        navController = NavController(LocalContext.current),
        exerciseKind = "Exercise Name"
    )
}