package com.dracoo.medicinemanagement.menus.new_medicine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracoo.medicinemanagement.model.MedicineMasterModel
import com.dracoo.medicinemanagement.repo.ApiRepository
import com.dracoo.medicinemanagement.repo.DataStoreRepo
import com.dracoo.medicinemanagement.utils.MedicalUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewMedicineViewModel @Inject constructor(
    private val apiRepository : ApiRepository,
    private val dataStoreRepo: DataStoreRepo
):  ViewModel() {

    fun getMasterMedicine(
        callback: DataCallback<List<MedicineMasterModel>>,
        onCallBackString: ((String) -> Unit)? = null
    ){
        viewModelScope.launch {
            apiRepository.getMedicineMaster(object :ApiRepository.ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
                    val list = ArrayList<MedicineMasterModel>()
                    data?.let {
                        onCallBackString?.invoke(it.toString())
                        val jArrayValue = data.getJSONArray("values")
                        (0 until jArrayValue.length()).forEach { i ->
                            val stDate = jArrayValue[i].toString().split(",")[0]
                                .split("[")[1].replace("\"", "").replace("\\", "")
                            val stMedicineCode = jArrayValue[i].toString().split(",")[1].replace("\"", "")
                            val stMedicineType = jArrayValue[i].toString().split(",")[2].replace("\"", "")
                            val stMedicinePrize = jArrayValue[i].toString().split(",")[3].replace("\"", "")
                            val stMedicineName = jArrayValue[i].toString().split(",")[4].split("]")[0].replace("\"", "")
                            val stMedicineCategory = jArrayValue[i].toString().split(",")[5].split("]")[0].replace("\"", "")
//                            Timber.e("split result date "  + stDate + "\ncode " + stMedicineCode + " " +
//                                    "\nstMedicineType " + stMedicineType + "\nstMedicinePrize"
//                                    +stMedicinePrize+ "\nstMedicineName " +stMedicineName)
                            list.add(MedicineMasterModel(stDate, stMedicineCode,stMedicineType,stMedicinePrize, stMedicineName,stMedicineCategory))
                        }
                        callback.onDataLoaded(list)
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

    fun saveDataMedicine(stData : String){
        viewModelScope.launch {
            dataStoreRepo.saveMasterMedicine(stData)
        }
    }

    fun postNewMedicine(postModel: MedicineMasterModel, callback :DataCallback<MedicineMasterModel>){
        viewModelScope.launch {
            apiRepository.postMedicineMaster(postModel, object :ApiRepository.ApiCallback<MedicineMasterModel>{
                override fun onDataLoaded(data: MedicineMasterModel?) {
                    data?.let {
                        Timber.e("data post $data")
                        callback.onDataLoaded(data)
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

    //interface response from server
    interface DataCallback<T> {
        fun onDataLoaded(data: T?)
        fun onDataError(error: String?)
    }
}