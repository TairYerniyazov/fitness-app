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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class DurationInputState(
    var hour: UInt = 0u,
    var minute: UInt = 0u,
    var second: UInt = 0u
)

@Composable
fun DurationInput(
    state: MutableState<DurationInputState>,
) {
    val state by state

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = state.hour.toString(),
            onValueChange = { newHour ->
                state.hour = try {
                    newHour.toUInt()
                } catch (e: NumberFormatException) {
                    0u
                }
            },
            label = { Text("Hour") },
            suffix = { Text("h") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().weight(1f)
        )
        Text(":")
        OutlinedTextField(
            value = state.minute.toString(),
            onValueChange = { newMinute ->
                state.minute = try {
                    newMinute.toUInt()
                } catch (e: NumberFormatException) {
                    0u
                }
            },
            label = { Text("Minute") },
            suffix = { Text("m") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().weight(1f)
        )
        Text(":")
        OutlinedTextField(
            value = state.second.toString(),
            onValueChange = { newSecond ->
                state.second = try {
                    newSecond.toUInt()
                } catch (e: NumberFormatException) {
                    0u
                }
            },
            label = { Text("Second") },
            suffix = { Text("s") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().weight(1f)
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
