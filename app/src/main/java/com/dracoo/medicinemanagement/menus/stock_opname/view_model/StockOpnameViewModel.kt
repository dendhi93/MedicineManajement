package com.dracoo.medicinemanagement.menus.stock_opname.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracoo.medicinemanagement.model.MedicineMasterModel
import com.dracoo.medicinemanagement.repo.ApiRepository
import com.dracoo.medicinemanagement.repo.DataStoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class StockOpnameViewModel @Inject constructor(
    private val apiRepository : ApiRepository,
    private val dataStoreRepo: DataStoreRepo
):  ViewModel() {

    fun getUserData() = dataStoreRepo.getUser()

    fun getDataMedicine() = dataStoreRepo.getMasterMedicine()

    fun getMasterMedicine(
        callback: DataCallback<List<MedicineMasterModel>>
    ){
        viewModelScope.launch {
            apiRepository.getMedicineMaster(object :ApiRepository.ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
                    val list = ArrayList<MedicineMasterModel>()
                    data?.let {
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

    //interface response from server
    interface DataCallback<T> {
        fun onDataLoaded(data: T?)
        fun onDataError(error: String?)
    }
}