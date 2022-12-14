package com.dracoo.medicinemanagement.repo

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
class DataStoreRepo
@Inject
constructor(
    @ApplicationContext private val context: Context
)  {

    suspend fun saveUser(name: String, address : String) {
        context.dataStore.edit { preferences ->
            preferences[vUserKey] = name
            preferences[vAddressKey] = address
        }
    }

    suspend fun saveMasterMedicine(stResponse : String){
        context.dataStore.edit { preferences ->
            preferences[vMasterMedicineKey] = stResponse
        }
    }

    fun getUser(): Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[vUserKey] ?: ""
        }

    fun getAddress(): Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[vAddressKey] ?: ""
        }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userData")
        val vUserKey = stringPreferencesKey("user_key")
        val vAddressKey = stringPreferencesKey("address_key")
        val vMasterMedicineKey = stringPreferencesKey("master_medicine")
    }

}