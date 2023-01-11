package com.dracoo.medicinemanagement.menus.direct_sale.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ActivityReportDirectSalesBinding
import com.dracoo.medicinemanagement.menus.direct_sale.adapter.ReportDirectSalesAdapter
import com.dracoo.medicinemanagement.menus.direct_sale.viewmodel.ReportDirectSalesViewModel
import com.dracoo.medicinemanagement.menus.main.view.MainActivity
import com.dracoo.medicinemanagement.model.DirectSaleModel
import com.dracoo.medicinemanagement.utils.CheckConnectionUtil
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.DataCallback
import com.dracoo.medicinemanagement.utils.MedicalUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ReportDirectSalesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportDirectSalesBinding
    private val reportDirectSalesViewModel : ReportDirectSalesViewModel by viewModels()
    private lateinit var reportDirectSaleAdapter : ReportDirectSalesAdapter
    private var aLDirectSalesReport: ArrayList<DirectSaleModel> = ArrayList()
    private val calendar = Calendar.getInstance()
    private var isConnected = false
    private var stSelectedMonth = ""
    private var stSelectedYear = ""
    private val checkConnection by lazy {
        CheckConnectionUtil(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportDirectSalesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back_32)
        }
    }

    private fun initNotConnectedDialog(){
        when(isConnected){
            false -> MedicalUtil.alertDialogDismiss(
                ConstantsObject.vNoConnectionTitle,
                ConstantsObject.vNoConnectionMessage, this, false)
            true -> MedicalUtil.alertDialogDismiss(
                ConstantsObject.vNoConnectionTitle,
                ConstantsObject.vNoConnectionMessage, this, true)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        binding.apply {
            tglRdsIv.setOnClickListener {
                MedicalUtil.monthAndYearPicker(this@ReportDirectSalesActivity, onSelected = {
                        mSelectedMonth, mSelectedYear ->
                    stSelectedMonth = mSelectedMonth
                    stSelectedYear = mSelectedYear
                    calendarRsoTiet.setText("$mSelectedMonth-$mSelectedYear")
                    if(isConnected){getDirectSale(false)}
                })
            }

            refreshRdsSrl.setOnRefreshListener{
                refreshRdsSrl.isRefreshing = true
                when(isConnected){
                    true -> getDirectSale(true)
                    else ->{
                        Handler(Looper.getMainLooper()).postDelayed({
                            refreshRdsSrl.isRefreshing = false
                        },100)
                    }
                }
            }

            reportDirectSaleAdapter = ReportDirectSalesAdapter(onItemClick = {
                    v, model, _ ->
                Timber.e("model " +model.noTagihan)
                when(model.isReverse){
                    "0" -> MedicalUtil.showPopUpMenuDirectSale(this@ReportDirectSalesActivity, v, false,onClickMenu = {
                        Timber.e("itMenu $it")
                    })
                    else -> MedicalUtil.showPopUpMenuDirectSale(this@ReportDirectSalesActivity, v, true,onClickMenu = {
                        Timber.e("itMenu $it")
                    })
                }

            })

            directSalesRdsRv.apply {
                layoutManager = LinearLayoutManager(
                    this@ReportDirectSalesActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                setHasFixedSize(true)
                adapter = reportDirectSaleAdapter
            }

            searchRdsTiet.addTextChangedListener {
                when(aLDirectSalesReport.size){
                    0 -> MedicalUtil.snackBarMessage(getString(R.string.empty_data),
                        this@ReportDirectSalesActivity, ConstantsObject.vSnackBarWithOutTombol)
                    else ->{
                        when(it?.length){
                            0 -> reportDirectSaleAdapter.initDataAdapter(aLDirectSalesReport)
                            else -> {
                                val selectedArrayList = MedicalUtil.filterDirectSalesAdapter(it.toString(), aLDirectSalesReport).distinct().toList()
                                reportDirectSaleAdapter.initDataAdapter(selectedArrayList)
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        checkConnection.observe(this) {
            isConnected = when {
                !it -> false
                else -> true
            }

            initNotConnectedDialog()

            stSelectedYear = calendar.get(Calendar.YEAR).toString()
            val intMonth = calendar.get(Calendar.MONTH) + 1
            stSelectedMonth = when{
                intMonth < 10 -> "0$intMonth"
                else -> intMonth.toString()
            }
            binding.calendarRsoTiet.setText("$stSelectedMonth-$stSelectedYear")
            reportDirectSalesViewModel.getDSStore().observe(this@ReportDirectSalesActivity){ itObserve ->
                itObserve?.let { itLet ->
                    when{
                        itLet.isNotEmpty() -> {
                            val type: Type = object : TypeToken<List<DirectSaleModel?>?>() {}.type
                            val tempSDList: List<DirectSaleModel> = Gson().fromJson(itLet, type)
                            if(tempSDList.isNotEmpty()){
                                binding.animEmptyRdsGiv.visibility = View.GONE
                                binding.titleDataKosongRdsTv.visibility = View.GONE
                                binding.directSalesRdsRv.visibility = View.VISIBLE

                                val tempSortSd = tempSDList.sortedByDescending { obj -> obj.createDate }
                                aLDirectSalesReport.clear()
                                aLDirectSalesReport.addAll(tempSortSd)
                                reportDirectSaleAdapter.initDataAdapter(aLDirectSalesReport)
                            }
                            binding.rdsPg.visibility = View.GONE
                        }
                        else ->if(isConnected){getDirectSale(false)}
                    }
                }
            }
        }
    }

    private fun getDirectSale(isSwipeRefresh : Boolean){
        binding.rdsPg.visibility = View.VISIBLE
        if(isSwipeRefresh){
            aLDirectSalesReport.clear()
            binding.directSalesRdsRv.visibility = View.GONE
            binding.animEmptyRdsGiv.visibility = View.VISIBLE
            binding.titleDataKosongRdsTv.visibility = View.VISIBLE
        }

        reportDirectSalesViewModel.getDataDirectSale("$stSelectedMonth-$stSelectedYear", object :DataCallback<List<DirectSaleModel>>{
            override fun onDataLoaded(data: List<DirectSaleModel>?) {
                binding.apply {
                    if(isSwipeRefresh){refreshRdsSrl.isRefreshing = false}
                    rdsPg.visibility = View.GONE
                    data?.let {
                        when(it.size){
                            0 -> {
                                animEmptyRdsGiv.visibility = View.VISIBLE
                                titleDataKosongRdsTv.visibility = View.VISIBLE
                                directSalesRdsRv.visibility = View.GONE
                            }
                            else ->{
                                animEmptyRdsGiv.visibility = View.GONE
                                titleDataKosongRdsTv.visibility = View.GONE
                                directSalesRdsRv.visibility = View.VISIBLE
                            }
                        }
                        val tempList = it.sortedByDescending { obj -> obj.createDate }
                        aLDirectSalesReport.clear()
                        aLDirectSalesReport.addAll(tempList)
                        reportDirectSaleAdapter.initDataAdapter(aLDirectSalesReport)
                    }
                }
            }

            override fun onDataError(error: String?) {
                error?.let { itError ->
                    MedicalUtil.snackBarMessage(itError, this@ReportDirectSalesActivity, ConstantsObject.vSnackBarWithTombol)
                }
                if(isSwipeRefresh){binding.refreshRdsSrl.isRefreshing = false}
                binding.rdsPg.visibility = View.GONE
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}