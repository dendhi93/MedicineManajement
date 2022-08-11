package com.dracoo.medicinemanagement.menus.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracoo.medicinemanagement.utils.DataStoreUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val storeRepository : DataStoreUtil
): ViewModel() {

    fun saveUser(name : String){
        viewModelScope.launch(Dispatchers.IO) {
            storeRepository.saveUser(name)
        }
    }
}