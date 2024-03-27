package com.example.crud.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.crud.helper.AccountPreferences
import kotlinx.coroutines.launch

class AccountViewModel(private val pref: AccountPreferences) : ViewModel() {
    fun getUidData(): LiveData<String> {
        return pref.getUid().asLiveData()
    }

    fun getEmailData(): LiveData<String> {
        return pref.getEmail().asLiveData()
    }

    fun saveAccount(email: String, uid: String){
        viewModelScope.launch {
            pref.saveAccount(email, uid)
        }
    }

    fun deleteAccount(){
        viewModelScope.launch {
            pref.deleteAccount()
        }
    }
}