package com.dracoo.medicinemanagement.menus.direct_sale.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dracoo.medicinemanagement.model.MedicineMasterModel
import com.dracoo.medicinemanagement.repo.ApiRepository
import com.dracoo.medicinemanagement.repo.DataStoreRepo
import com.dracoo.medicinemanagement.utils.DataCallback
import com.dracoo.medicinemanagement.utils.MedicalUtil
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class DirectSalesViewModel @Inject constructor(
    private val dataStoreRepo : DataStoreRepo,
    private val apiRepository: ApiRepository
): ViewModel() {

    fun getUserData() = dataStoreRepo.getUser().asLiveData()

    fun getDataMedicine() = dataStoreRepo.getMasterMedicine().asLiveData()

    fun getMasterMedicine(
        callback: DataCallback<List<MedicineMasterModel>>
    ){
        viewModelScope.launch {
            apiRepository.getMedicineMaster(object :ApiRepository.ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
                    data?.let {
                        saveDataMedicine(MedicalUtil.initReturnMedical(it))
                        callback.onDataLoaded(MedicalUtil.initReturnMedical(it))
                    }
                }

                override fun onDataError(error: String?) {
                    error?.let {
                        callback.onDataError(it)
                    }
                }
            })
        }
    }

    private fun saveDataMedicine(alData : List<MedicineMasterModel>){
        viewModelScope.launch {
            val stData = Gson().toJson(alData)
            dataStoreRepo.saveMasterMedicine(stData)
        }
    }

}