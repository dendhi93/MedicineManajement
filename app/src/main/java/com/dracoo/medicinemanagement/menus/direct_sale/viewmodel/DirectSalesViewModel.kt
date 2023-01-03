package com.dracoo.medicinemanagement.menus.direct_sale.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dracoo.medicinemanagement.model.DirectSaleModel
import com.dracoo.medicinemanagement.model.StockOpnameModel
import com.dracoo.medicinemanagement.repo.ApiRepository
import com.dracoo.medicinemanagement.repo.DataStoreRepo
import com.dracoo.medicinemanagement.utils.DataCallback
import com.dracoo.medicinemanagement.utils.MedicalUtil
import com.dracoo.medicinemanagement.utils.StraightApiCallBack
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

    fun getDataSO(callback: DataCallback<List<StockOpnameModel>>){
        viewModelScope.launch {
            apiRepository.postStockOpnameData(object :ApiRepository.ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
                    data?.let {
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

    fun postDirectSale(mModel : DirectSaleModel, mYearMonth : String, actionTrans : String, callback : StraightApiCallBack){
        viewModelScope.launch {
            apiRepository.postTransDirectSale(mModel, mYearMonth, actionTrans, callback)
        }
    }
}