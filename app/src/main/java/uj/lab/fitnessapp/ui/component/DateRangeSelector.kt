package uj.lab.fitnessapp.ui.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun DateRangeSelector(
    selected: String,
    onSelectedChange: (String) -> Unit,
    onDateRangeChange: (Pair<Long?, Long?>) -> Unit
) {
    val options = listOf("tydzień", "miesiąc", "kwartał", "rok", "cały okres", "własny zakres")
    var canSelectRange by remember { mutableStateOf(false) }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items(options) { label ->
            FilterChip(
                selected = label == selected,
                onClick = {
                    if(label == "własny zakres"){
                        canSelectRange = true
                    }
                    else{
                        onSelectedChange(label)
                    }
                },
                label = { Text(label) }
            )
        }
    }

    if(canSelectRange){
        DateRangePickerModal(
            onDateRangeSelected = { range ->
                onDateRangeChange(range)
                onSelectedChange("własny zakres")
            },
            onDismiss = { canSelectRange = false }
        )
    }



}

//@Preview(showBackground = true)
//@Composable
//internal fun DateRangeSelectorPreview(){
//    var selectedChip by remember { mutableStateOf("cały okres") }
//
//    DateRangeSelector(
//        selected = selectedChip,
//        onSelectedChange = { selectedChip = it }
//    )
//}