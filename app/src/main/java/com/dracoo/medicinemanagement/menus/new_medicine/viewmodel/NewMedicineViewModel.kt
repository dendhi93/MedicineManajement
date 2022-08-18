package com.dracoo.medicinemanagement.menus.new_medicine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracoo.medicinemanagement.model.MedicineMasterModel
import com.dracoo.medicinemanagement.repo.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class NewMedicineViewModel @Inject constructor(
    private val apiRepository : ApiRepository
):  ViewModel() {

    fun getMasterMedicine(callback: DataCallback<List<MedicineMasterModel>>){
        viewModelScope.launch {
            val list = ArrayList<MedicineMasterModel>()
            apiRepository.getMedicineMaster(object : ApiRepository.ApiCallback<List<MedicineMasterModel>>{
                override fun onDataLoaded(data: List<MedicineMasterModel>?) {
                    data?.let {
                        callback.onDataLoaded(it)
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

    //interface response from server
    interface DataCallback<T> {
        fun onDataLoaded(data: T?)
        fun onDataError(error: String?)
    }
}