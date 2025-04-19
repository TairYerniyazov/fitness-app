package uj.lab.fitnessapp.ui.component

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DatePickerDocked() {
//    var showDatePicker by remember { mutableStateOf(false) }
//    val datePickerState = rememberDatePickerState()
//
//    val currentDate = Date()
//    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
//
//    val selectedDate = datePickerState.selectedDateMillis?.let {
//        convertMillisToDate(it)
//    } ?: formatter.format(currentDate)
//
//    Box(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        OutlinedTextField(
//            value = selectedDate,
//            onValueChange = { },
//            label = { Text("DOB") },
//            readOnly = true,
//            trailingIcon = {
//                IconButton(onClick = { showDatePicker = !showDatePicker }) {
//                    Icon(
//                        imageVector = Icons.Default.DateRange,
//                        contentDescription = "Select date"
//                    )
//                }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(64.dp)
//        )
//
//        if (showDatePicker) {
//            Popup(
//                onDismissRequest = { showDatePicker = false },
//                alignment = Alignment.TopStart
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .offset(y = 64.dp)
//                        .shadow(elevation = 4.dp)
//                        .background(MaterialTheme.colorScheme.surface)
//                        .padding(16.dp)
//                ) {
//                    DatePicker(
//                        state = datePickerState,
//                        showModeToggle = false
//                    )
//                }
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerFieldToModal(



    modifier: Modifier = Modifier


) {

    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    val currentDate = Date()
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val defaultDate = formatter.format(currentDate)

    val date = currentDate.time

    if (selectedDate == null) {
        selectedDate = date
    }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: defaultDate,
        onValueChange = { },
        label = {
            if (selectedDate == date) Text("Today")
            else Text("")
                },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false },
            initialSelectedDateMillis = selectedDate
        )
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun convertDateToMillis(dateString: String): Long? {
    return try {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val date = formatter.parse(dateString)
        date?.time
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    initialSelectedDateMillis: Long?
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialSelectedDateMillis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

//@Preview(showBackground = true)
//@Composable
//internal fun DatePickerModalPreview(){
//    DatePickerModal(
//        onDateSelected = {},
//        onDismiss = {},
//        initialSelectedDateMillis = null
//    )
//}


//@Preview(showBackground = true)
//@Composable
//internal fun DatePickerDockedPreview(){
//    DatePickerDocked()
//}


@Preview(showBackground = true)
@Composable
internal fun DatePickerFieldToModalPreview(){
    DatePickerFieldToModal()
}