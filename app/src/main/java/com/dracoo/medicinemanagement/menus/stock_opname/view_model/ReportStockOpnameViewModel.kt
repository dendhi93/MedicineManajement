package com.dracoo.medicinemanagement.menus.stock_opname.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracoo.medicinemanagement.model.StockOpnameModel
import com.dracoo.medicinemanagement.repo.ApiRepository
import com.dracoo.medicinemanagement.repo.DataStoreRepo
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.DataCallback
import com.dracoo.medicinemanagement.utils.MedicalUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
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
                    val list = ArrayList<StockOpnameModel>()
                    data?.let {
                        val jArray: JSONArray = it.getJSONArray("items")
                        (0 until jArray.length()).forEach { i ->
                            val jo: JSONObject = jArray.getJSONObject(i)
                            val medicineName = jo.getString("NamaObat")
                            val medicineCode = jo.getString("KodeObat")
                            val invoiceNo = jo.getString("NoFaktur")
                            val piecesPrize = jo.getString("HargaSatuan")
                            val qtyMedicine = jo.getString("Jumlah")
                            val userInput = jo.getString("UserCreate")
                            val dateSO = MedicalUtil.getChangeDateFormat(jo.getString("CreateDate"),
                            ConstantsObject.vSpecialDateJson, ConstantsObject.vTahunJamSetrip)

                            list.add(StockOpnameModel(medicineCode, medicineName, invoiceNo, piecesPrize, qtyMedicine, dateSO.toString(), userInput))
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
}