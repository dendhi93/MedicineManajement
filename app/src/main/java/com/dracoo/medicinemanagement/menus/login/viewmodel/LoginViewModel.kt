package com.dracoo.medicinemanagement.menus.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracoo.medicinemanagement.model.UserModel
import com.dracoo.medicinemanagement.repo.ApiRepository
import com.dracoo.medicinemanagement.repo.DataStoreRepo
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.DataCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val storeRepository : DataStoreRepo,
    private val apiRepository: ApiRepository
): ViewModel() {

    fun saveUser(name : String, address : String){
        viewModelScope.launch(Dispatchers.IO) {
            storeRepository.saveUser(name, address)
        }
    }

    fun postLogin(model : UserModel, callback: DataCallback<UserModel>){
        viewModelScope.launch {
            apiRepository.postLogin(model, ConstantsObject.vLoginJson,object : ApiRepository.ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
                    data?.let {
                        Timber.e("dataLogin $it")
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
//                                ConstantsObject.vSpecialDateJson, ConstantsObject.vTahunJamSetrip)
//
//                            list.add(StockOpnameModel(medicineCode, medicineName, invoiceNo, piecesPrize, qtyMedicine, dateSO.toString(), userInput))
//                        }
//                        saveJsonSO(list)
//                        callback.onDataLoaded(list)
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