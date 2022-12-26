package com.dracoo.medicinemanagement.menus.stock_opname.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dracoo.medicinemanagement.model.MedicineMasterModel
import com.dracoo.medicinemanagement.model.StockOpnameModel
import com.dracoo.medicinemanagement.repo.ApiRepository
import com.dracoo.medicinemanagement.repo.DataStoreRepo
import com.dracoo.medicinemanagement.utils.DataCallback
import com.dracoo.medicinemanagement.utils.MedicalUtil
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StockOpnameViewModel @Inject constructor(
    private val apiRepository : ApiRepository,
    private val dataStoreRepo: DataStoreRepo
):  ViewModel() {

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

    fun transactionStockOpname(postModel : StockOpnameModel, actionRequest : String,callback: DataCallback<String>){
        viewModelScope.launch {
            apiRepository.postTransStockOpname(postModel, actionRequest,object :ApiRepository.ApiCallback<String>{
                override fun onDataLoaded(data: String?) {
                    data?.let {
                        callback.onDataLoaded(it)
                    }
                }

                override fun onDataError(error: String?) {
                    error.let {
                        Timber.e("$error")
                        callback.onDataError(error.toString())
                    }
                }
            })
        }
    }
}