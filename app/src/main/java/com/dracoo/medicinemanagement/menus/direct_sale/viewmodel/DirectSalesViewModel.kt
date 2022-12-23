package com.dracoo.medicinemanagement.menus.direct_sale.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dracoo.medicinemanagement.repo.ApiRepository
import com.dracoo.medicinemanagement.repo.DataStoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DirectSalesViewModel @Inject constructor(
    private val dataStoreRepo : DataStoreRepo,
    private val apiRepository: ApiRepository
): ViewModel() {

    fun getUserData() = dataStoreRepo.getUser().asLiveData()

    fun getDataMedicine() = dataStoreRepo.getMasterMedicine().asLiveData()



}