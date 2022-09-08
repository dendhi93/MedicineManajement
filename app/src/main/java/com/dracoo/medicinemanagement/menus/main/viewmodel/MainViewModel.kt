package com.dracoo.medicinemanagement.menus.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.model.MenuModel
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.repo.DataStoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val storeRepository : DataStoreRepo
): ViewModel() {

    fun getUser() = storeRepository.getUser().asLiveData()

    fun getAddress() = storeRepository.getAddress().asLiveData()

    fun clearData(){
        viewModelScope.launch(Dispatchers.IO) {
            storeRepository.saveUser("", "")
            storeRepository.clearDataStore()
        }
    }

    fun initMenu(): ArrayList<MenuModel> {
        return arrayListOf(
            MenuModel(
                ConstantsObject.vInputMedicine,
                "Isi nama obat",
                R.color.bg_menu_1,
                R.mipmap.baseline_medication_white_48,
                R.drawable.intersect_1,
                ConstantsObject.vInputMedicine
            ),
            MenuModel(
                ConstantsObject.vStockOpname,
                "Isi stock obat",
                R.color.bg_menu_2,
                R.drawable.ic_package_48,
                R.drawable.ic_intersect2,
                ConstantsObject.vStockOpname
            ),
            MenuModel(
                ConstantsObject.vDirectSales,
                "Input Penjualan",
                R.color.bg_menu_3,
                R.drawable.ic_direct_sales_24,
                R.drawable.ic_intersect3,
                ConstantsObject.vDirectSales
            ),
            MenuModel(
                ConstantsObject.vReportStockOpname,
                "Laporan Stock",
                R.color.bg_menu_2,
                R.drawable.ic_report_1,
                R.drawable.ic_intersect2,
                ConstantsObject.vReportStockOpname
            ),
            MenuModel(
                ConstantsObject.vReportDirectSales,
                "Laporan Penjualan",
                R.color.bg_menu_3,
                R.drawable.ic_report_1,
                R.drawable.ic_intersect3,
                ConstantsObject.vReportDirectSales
            ),
            MenuModel(
                ConstantsObject.vExitApps,
                "",
                R.color.red_1_transparanst,
                R.drawable.ic_logout_32,
                R.drawable.ic_intersect_logout,
                ConstantsObject.vStockOpname
            )
        )
    }

}