package com.dracoo.medicinemanagement.menus.stock_opname.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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
class ReportStockOpnameViewModel @Inject constructor(
    private val apiRepository : ApiRepository,
    private val dataStoreRepo: DataStoreRepo
):  ViewModel()  {

    fun getDataSO(callback: DataCallback<List<StockOpnameModel>>){
        viewModelScope.launch {
            apiRepository.postStockOpnameData(object :ApiRepository.ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
//                    val list = ArrayList<StockOpnameModel>()
                    data?.let {
//                        val jArray: JSONArray = it.getJSONArray("items")
//                        (0 until jArray.length()).forEach { i ->
//                            val jo: JSONObject = jArray.getJSONObject(i)
//                            val medicineName = jo.getString("NamaObat")
//                            val medicineCode = jo.getString("KodeObat")
//                            val invoiceNo = jo.getString("NoFaktur")
//                            val piecesPrize = jo.getString("HargaSatuan")
//                            val qtyMedicine = jo.getString("Jumlah")
//                            val userInput = jo.getString("UserCreate")
//                            val dateSO = MedicalUtil.getChangeDateFormat(jo.getString("CreateDate"),
//                            ConstantsObject.vSpecialDateJson, ConstantsObject.vTahunJamSetrip)
//
//                            list.add(StockOpnameModel(medicineCode, medicineName, invoiceNo, piecesPrize, qtyMedicine, dateSO.toString(), userInput))
//                        }
                        saveJsonSO(MedicalUtil.initReturnMedicalStock(it))
                        callback.onDataLoaded(MedicalUtil.initReturnMedicalStock(it))
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

    private fun saveJsonSO(alData : List<StockOpnameModel>){
        viewModelScope.launch {
            val stData = Gson().toJson(alData)
            dataStoreRepo.saveSOData(stData)
        }
    }

    fun getSOStore() = dataStoreRepo.getSOData().asLiveData()

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