package com.dracoo.medicinemanagement.menus.direct_sale.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracoo.medicinemanagement.model.DirectSaleModel
import com.dracoo.medicinemanagement.repo.ApiRepository
import com.dracoo.medicinemanagement.utils.DataCallback
import com.dracoo.medicinemanagement.utils.MedicalUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReportDirectSalesViewModel @Inject constructor(
    private val apiRepository: ApiRepository
): ViewModel() {

    fun getDataDirectSale(monthYear : String,callback: DataCallback<List<DirectSaleModel>>){
        viewModelScope.launch {
            apiRepository.postDirectSaleData(monthYear, object :ApiRepository.ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
                    data?.let {
                        Timber.e("json $it")
                        callback.onDataLoaded(MedicalUtil.initReturnDirectSales(it))
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