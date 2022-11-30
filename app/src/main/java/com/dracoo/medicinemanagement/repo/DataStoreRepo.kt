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

    suspend fun saveUser(name: String,
                         pass : String,
                         role : String,
                         address : String
    ) {
        context.dataStore.edit { preferences ->
            preferences[vUserKey] = name
            preferences[vAddressKey] = address
            preferences[vPasswordKey] = pass
            preferences[vUserRoleKey] = role
        }
    }

    suspend fun saveMasterMedicine(stResponse : String){
        context.dataStore.edit { preferences ->
            preferences[vMasterMedicineKey] = stResponse
        }
    }

    suspend fun saveSOData(stResponse : String){
        context.dataStore.edit { preferences ->
            preferences[vSODataKey] = stResponse
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

    fun getMasterMedicine(): Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[vMasterMedicineKey] ?: ""
        }

    fun getSOData(): Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[vSODataKey] ?: ""
        }

    suspend fun clearDataStore(){
        context.dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userData")
        val vUserKey = stringPreferencesKey("user_key")
        val vPasswordKey = stringPreferencesKey("password_key")
        val vAddressKey = stringPreferencesKey("address_key")
        val vUserRoleKey = stringPreferencesKey("user_role")
        val vMasterMedicineKey = stringPreferencesKey("master_medicine")
        val vSODataKey = stringPreferencesKey("stock_opname_data")
    }

}