package uj.lab.fitnessapp.ui.screen.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
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

    companion object {
        private val THEME_KEY = booleanPreferencesKey("dark_theme_enabled")
    }

    private val dataStore = context.dataStore

    val isDarkThemeEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
            prefs[THEME_KEY] ?: false
        }

    suspend fun setDarkThemeEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[THEME_KEY] = enabled
        }
    }

    private val weightUnitKey = stringPreferencesKey("weight_unit")
    private val isDarkThemeKey = booleanPreferencesKey("is_dark_theme")
    private val distanceUnitKey = stringPreferencesKey("distance_unit")
    private val dateKey = longPreferencesKey("date")

    val distanceUnit: Flow<String> = dataStore.data.map { prefs ->
        prefs[distanceUnitKey] ?: "metric"
    }

    val weightUnit: Flow<String> = dataStore.data.map { prefs ->
        prefs[weightUnitKey] ?: "metric"
    }

    val isDarkTheme: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[isDarkThemeKey] ?: false
    }

    // tutaj może ustawić na datę
    val date: Flow<Long> = dataStore.data.map{ prefs ->
        prefs[dateKey] ?: 0L
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

    suspend fun setDate(date: Long){
        dataStore.edit { prefs ->
            prefs[dateKey] = date
        }
    }
}