package com.dracoo.medicinemanagement.menus.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dracoo.medicinemanagement.utils.DataStoreUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val storeRepository : DataStoreUtil
): ViewModel() {

    fun getUser() = storeRepository.getUser().asLiveData()

}