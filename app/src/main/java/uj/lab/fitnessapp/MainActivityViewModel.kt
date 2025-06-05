package uj.lab.fitnessapp

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.source.AppDatabase
import uj.lab.fitnessapp.ui.screen.settings.SettingsManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val db: AppDatabase,
    private val application: Application,
    private val settingsManager: SettingsManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState: StateFlow<MainActivityUiState> get() = _uiState

    init {
        viewModelScope.launch {
            populateDatabase()
            val currentDate = LocalDate.now()
            val dateMillis = currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            settingsManager.setDate(dateMillis)
        }
    }

    // Workaround to prevent splash screen animation from being cut off when database initializes too quickly
    suspend fun populateDatabase() {
        if (!uiState.value.isDatabaseInitialized) {
            val start = SystemClock.uptimeMillis()

            db.populateDatabase(application.applicationContext)

            val end = SystemClock.uptimeMillis()
            val elapsed = end - start

            if(elapsed < 1500) delay(1500 - elapsed)
            _uiState.update {
                it.copy(isDatabaseInitialized = true)
            }
        }
    }
}

data class MainActivityUiState(
    val isDatabaseInitialized: Boolean = false,
)