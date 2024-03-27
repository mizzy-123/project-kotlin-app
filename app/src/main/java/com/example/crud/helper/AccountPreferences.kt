package com.example.crud.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStoreAccount: DataStore<Preferences> by preferencesDataStore(name = "account")

class AccountPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val UID = stringPreferencesKey("uid")
    private val EMAIL = stringPreferencesKey("email")

    fun getUid(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[UID] ?: ""
        }
    }

    fun getEmail(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[EMAIL] ?: ""
        }
    }

    suspend fun saveAccount(email: String, uid: String){
        dataStore.edit { preferences ->
            preferences[UID] = uid
            preferences[EMAIL] = email
        }
    }

    suspend fun deleteAccount(){
        dataStore.edit { preferences ->
            preferences[UID] = ""
            preferences[EMAIL] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AccountPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): AccountPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = AccountPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}