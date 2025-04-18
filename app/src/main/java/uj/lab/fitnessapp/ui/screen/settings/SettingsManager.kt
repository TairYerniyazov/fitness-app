package uj.lab.fitnessapp.ui.screen.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    private val weightUnitKey = stringPreferencesKey("weight_unit")
    private val isDarkThemeKey = booleanPreferencesKey("is_dark_theme")
    private val distanceUnitKey = stringPreferencesKey("distance_unit")

    val distanceUnit: Flow<String> = dataStore.data.map { prefs ->
        prefs[distanceUnitKey] ?: "metric"
    }

    val weightUnit: Flow<String> = dataStore.data.map { prefs ->
        prefs[weightUnitKey] ?: "metric"
    }

    val isDarkTheme: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[isDarkThemeKey] ?: false
    }

    suspend fun setDistanceUnit(unit: String) {
        dataStore.edit { prefs ->
            prefs[distanceUnitKey] = unit
        }
    }

    suspend fun setWeightUnit(unit: String) {
        dataStore.edit { prefs ->
            prefs[weightUnitKey] = unit
        }
    }

    suspend fun toggleTheme(newValue: Boolean) {
        dataStore.edit { prefs ->
            prefs[isDarkThemeKey] = newValue
        }
    }
}