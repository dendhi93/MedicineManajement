package com.dracoo.medicinemanagement.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class DataStoreUtil
@Inject
constructor(
    @ApplicationContext private val context: Context
)  {

    suspend fun saveUser(name: String) {
        context.dataStore.edit { preferences ->
            preferences[vUserKey] = name
        }
    }

    fun getUser(): Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[vUserKey] ?: ""
        }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userEmail")
        val vUserKey = stringPreferencesKey("user_key")
    }

}