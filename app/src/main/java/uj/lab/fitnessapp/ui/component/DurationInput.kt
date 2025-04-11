package uj.lab.fitnessapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

data class DurationInputState(
    var hour: String = "0",
    var minute: String = "0",
    var second: String = "0",
) {
    fun toDuration(): Duration {
        return hour.toInt().hours + minute.toInt().minutes + second.toInt().seconds
    }

    fun fromDuration(duration: Duration) {
        hour = duration.inWholeHours.toString()
        minute = (duration.inWholeMinutes % 60).toString()
        second = (duration.inWholeSeconds % 60).toString()
    }
}

@Composable
fun DurationInput(
    state: MutableState<DurationInputState>,
) {
    var state by state
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = state.hour,
            onValueChange = { newHour ->
                state = state.copy(hour = newHour)
            },
            label = { Text("Hour") },
            suffix = { Text("h") },
            isError = state.hour.toIntOrNull() == null || state.hour.toInt() < 0,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Text(":")
        OutlinedTextField(
            value = state.minute,
            onValueChange = { newMinute ->
                state = state.copy(minute = newMinute)
            },
            label = { Text("Minute") },
            suffix = { Text("m") },
            isError = state.minute.toIntOrNull() == null || state.minute.toInt() < 0,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Text(":")
        OutlinedTextField(
            value = state.second,
            onValueChange = { newSecond ->
                state = state.copy(second = newSecond)
            },
            label = { Text("Second") },
            suffix = { Text("s") },
            isError = state.second.toIntOrNull() == null || state.second.toInt() < 0,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun DurationInputPreview() {
    var state = remember { mutableStateOf(DurationInputState()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DurationInput(
            state,
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
