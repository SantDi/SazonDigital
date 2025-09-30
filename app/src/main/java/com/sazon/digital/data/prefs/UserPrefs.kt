
package com.sazon.digital.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

object Keys {
    val GRID_SIZE = intPreferencesKey("grid_size")
}

class UserPrefs(private val context: Context) {
    val gridSize: Flow<Int> = context.dataStore.data.map { it[Keys.GRID_SIZE] ?: 160 }

    suspend fun setGridSize(size: Int) {
        context.dataStore.edit { it[Keys.GRID_SIZE] = size }
    }
}
