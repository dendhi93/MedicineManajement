package com.dracoo.medicinemanagement.menus.new_medicine.viewmodel

import androidx.lifecycle.ViewModel
import com.dracoo.medicinemanagement.repo.DataStoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewMedicineViewModel @Inject constructor(
    private val storeRepository : DataStoreRepo
):  ViewModel() {

}