package uj.lab.fitnessapp.ui.component

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerFieldToModal(
    modifier: Modifier = Modifier,
    colors: TextFieldColors =  OutlinedTextFieldDefaults.colors(),
    selectedDate: Long?,
    onDateChange: (Long?) -> Unit
) {
    var showModal by remember { mutableStateOf(false) }

    val currentDate = Date()
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val defaultDate = formatter.format(currentDate)

    val dateOnly = formatter.parse(defaultDate)
    val dateOnlyMillis = dateOnly!!.time

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: defaultDate,
        onValueChange = { },
        label = {
            selectedDate?.let {
                if (it == dateOnlyMillis) {
                    Text("Dzisiejszy Trening")
                }
                else if (it < dateOnlyMillis){
                    Text("Poprzedni Trening")
                }
            }
        },
        readOnly = true,
        placeholder = { Text("DD-MM-YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Wybierz datę")
        },
        colors = colors,
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
            onDateSelected = { onDateChange(it) },
            onDismiss = { showModal = false },
            initialSelectedDateMillis = selectedDate
        )
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    initialSelectedDateMillis: Long?
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis,

        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val selectedDate = Instant.ofEpochMilli(utcTimeMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                val today = LocalDate.now(ZoneId.systemDefault())
                return !selectedDate.isAfter(today)
            }
//            TODO: Można to ustawić na jakiś 2022 albo 2023, bo po co więcej lat
//            override fun isSelectableYear(year: Int): Boolean {
//                val currentYear = LocalDate.now(ZoneId.systemDefault()).year
//                return year <= currentYear && year >= 2023
//            }
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("Wybierz")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            dateFormatter = object : DatePickerFormatter {
                private val locale = Locale("pl", "PL")
                private val zone = ZoneId.systemDefault()

                override fun formatDate(
                    dateMillis: Long?,
                    locale: CalendarLocale,
                    forContentDescription: Boolean
                ): String? {
                    dateMillis ?: return null
                    val date = Instant.ofEpochMilli(dateMillis)
                        .atZone(zone)
                        .toLocalDate()
                    // "d MMMM yyyy" → 19 kwietnia 2025
                    return DateTimeFormatter.ofPattern("d MMMM yyyy", this.locale).format(date)
                }

                override fun formatMonthYear(
                    monthMillis: Long?,
                    locale: CalendarLocale
                ): String? {
                    monthMillis ?: return null
                    val date = Instant.ofEpochMilli(monthMillis)
                        .atZone(zone)
                        .toLocalDate()
                    // "LLLL yyyy" → kwiecień 2025
                    return DateTimeFormatter.ofPattern("LLLL yyyy", this.locale).format(date)
                }
            },

            //TODO: wywaliłem opcję ręcznego wpisania daty, bo nie da się zmodyfikować okna do jej wpisywania
            showModeToggle = false,

            title = {
                Text(
                    text = "Wybierz datę",
                    Modifier.padding(PaddingValues(start = 24.dp, end = 12.dp, top = 16.dp))
                )
            },
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//internal fun DatePickerFieldToModalPreview(){
//    DatePickerFieldToModal(initialSelectedDate = Date().time)
//}