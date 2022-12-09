package com.dracoo.medicinemanagement.menus.stock_opname.view

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
import com.dracoo.medicinemanagement.databinding.ActivityReportStockOpnameBinding
import com.dracoo.medicinemanagement.menus.main.view.MainActivity
import com.dracoo.medicinemanagement.menus.stock_opname.adapter.ReportStockOpnameAdapter
import com.dracoo.medicinemanagement.menus.stock_opname.view_model.ReportStockOpnameViewModel
import com.dracoo.medicinemanagement.model.StockOpnameModel
import com.dracoo.medicinemanagement.utils.CheckConnectionUtil
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.DataCallback
import com.dracoo.medicinemanagement.utils.MedicalUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ReportStockOpnameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportStockOpnameBinding
    private lateinit var reportStockOpnameAdapter: ReportStockOpnameAdapter
    private val reportSOViewModel : ReportStockOpnameViewModel by viewModels()
    private var aLSOReport: ArrayList<StockOpnameModel> = ArrayList()
    private val calendar = Calendar.getInstance()
    private var isConnected = false
    private var stSelectedMonth = ""
    private var stSelectedYear = ""
    private val checkConnection by lazy {
        CheckConnectionUtil(application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportStockOpnameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back_32)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()

        reportStockOpnameAdapter = ReportStockOpnameAdapter(this,onItemClick = {
                v, model ->
            MedicalUtil.showPopUpMenu(this, v, onClickMenu = { itMenu ->
                val intentToStockOpname = Intent(this, StockOpnameActivity::class.java)
                when(itMenu){
                    getString(R.string.detail_mnu) ->{
                        val bundle = Bundle()
                        bundle.putString(ConstantsObject.vExtrasActionForm, ConstantsObject.vShowData)
                        bundle.putParcelable(ConstantsObject.vExtrasStockOpname,model)
                        intentToStockOpname.putExtras(bundle)
                        startActivity(intentToStockOpname)
                        finish()
                    }
                    getString(R.string.edit_mnu) ->{
                        val bundle = Bundle()
                        bundle.putString(ConstantsObject.vExtrasActionForm, ConstantsObject.vEditData)
                        bundle.putParcelable(ConstantsObject.vExtrasStockOpname,model)
                        intentToStockOpname.putExtras(bundle)
                        startActivity(intentToStockOpname)
                        finish()
                    }
                    else ->{
                        MedicalUtil.showDialogConfirmation(this,"Konfirmasi",
                            "Apakah anda yakin ingin hapus stock opname obat ${model.NamaObat} ?"
                        ) {
                            binding.rsoPg.visibility = View.VISIBLE
                            reportSOViewModel.transactionStockOpname(
                                model, ConstantsObject.vDeleteJson, object :DataCallback<String>{
                                    override fun onDataLoaded(data: String?) {
                                        binding.rsoPg.visibility = View.GONE
                                        data?.let {
                                            aLSOReport.remove(model)
                                            reportStockOpnameAdapter.initAdapter(aLSOReport)
                                            MedicalUtil.snackBarMessage(it, this@ReportStockOpnameActivity, ConstantsObject.vSnackBarWithOutTombol)
                                        }
                                    }

                                    override fun onDataError(error: String?) {
                                        binding.rsoPg.visibility = View.GONE
                                        error?.let {
                                            MedicalUtil.snackBarMessage("failed $it", this@ReportStockOpnameActivity, ConstantsObject.vSnackBarWithOutTombol)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            })
        })

        reportStockOpnameAdapter.initAdapter(aLSOReport)
        binding.medicineBmRv.apply {
            layoutManager = LinearLayoutManager(
                this@ReportStockOpnameActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
            adapter = reportStockOpnameAdapter
        }

        binding.apply {
            searchRsoTiet.addTextChangedListener { itInput ->
                when(aLSOReport.size){
                    0 -> MedicalUtil.snackBarMessage(getString(R.string.empty_data),
                        this@ReportStockOpnameActivity, ConstantsObject.vSnackBarWithOutTombol)
                    else ->{
                        when(itInput?.length){
                            0 -> reportStockOpnameAdapter.initAdapter(aLSOReport)
                            else -> {
                                val selectedArrayList = MedicalUtil.filterSOAdapter(itInput.toString(), aLSOReport).distinct().toList()
                                reportStockOpnameAdapter.initAdapter(ArrayList(selectedArrayList))
                            }
                        }
                    }
                }
            }

            tglRsoIv.setOnClickListener {
                MedicalUtil.monthAndYearPicker(this@ReportStockOpnameActivity, onSelected = {
                        mSelectedMonth, mSelectedYear ->
                    stSelectedMonth = mSelectedMonth
                    stSelectedYear = mSelectedYear
                    calendarRsoTiet.setText("$mSelectedMonth-$mSelectedYear")
                    if(isConnected){ getDataSO(true) }
                })
            }

            refreshRsoSrl.setOnRefreshListener{
                refreshRsoSrl.isRefreshing = true
                when(isConnected){
                    true -> getDataSO(true)
                    else ->{
                        Handler(Looper.getMainLooper()).postDelayed({
                            refreshRsoSrl.isRefreshing = false
                        },100)
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
                !it -> {
                    MedicalUtil.alertDialogDismiss(
                        ConstantsObject.vNoConnectionTitle,
                        ConstantsObject.vNoConnectionMessage, this, false
                    )
                    false
                }
                else -> {
                    MedicalUtil.alertDialogDismiss(
                        ConstantsObject.vNoConnectionTitle,
                        ConstantsObject.vNoConnectionMessage, this, true
                    )
                    true
                }
            }

            reportSOViewModel.getSOStore().observe(this@ReportStockOpnameActivity){ itSOObserve ->
                stSelectedYear = calendar.get(Calendar.YEAR).toString()
                val intMonth = calendar.get(Calendar.MONTH) + 1
                stSelectedMonth = when{
                    intMonth < 10 -> "0$intMonth"
                    else -> intMonth.toString()
                }

                itSOObserve?.let { itLet ->
                    when{
                        itLet.isNotEmpty() ->{
                            val type: Type = object : TypeToken<List<StockOpnameModel?>?>() {}.type
                            val tempSOList: List<StockOpnameModel> = Gson().fromJson(itLet, type)
                            if(tempSOList.isNotEmpty()){
                                val tempList = tempSOList.sortedByDescending { obj -> obj.CreateDate }
                                aLSOReport.clear()
                                aLSOReport.addAll(tempList)
                                reportStockOpnameAdapter.initAdapter(aLSOReport)

                                binding.apply {
                                    rsoPg.visibility = View.GONE
                                    medicineBmRv.visibility = View.VISIBLE
                                    animEmptyRsoGiv.visibility = View.GONE
                                    titleDataKosongAiscTv.visibility = View.GONE
                                    calendarRsoTiet.setText(tempList[0].monthYear)
                                }
                            }
                        }
                        else -> {
                            if(isConnected){ getDataSO(false) }
                            binding.calendarRsoTiet.setText("$stSelectedMonth-$stSelectedYear")
                        }
                    }
                }
            }
        }
    }

    private fun getDataSO(isSwipeRefresh : Boolean){
        binding.rsoPg.visibility = View.VISIBLE
        if(isSwipeRefresh){
            aLSOReport.clear()
            binding.medicineBmRv.visibility = View.GONE
            binding.animEmptyRsoGiv.visibility = View.VISIBLE
            binding.titleDataKosongAiscTv.visibility = View.VISIBLE
        }
        reportSOViewModel.getDataSO("$stSelectedMonth-$stSelectedYear",object :DataCallback<List<StockOpnameModel>>{
            override fun onDataLoaded(data: List<StockOpnameModel>?) {
                data?.let {
                    binding.apply {
                        when(it.size){
                            0 -> {
                                medicineBmRv.visibility = View.GONE
                                animEmptyRsoGiv.visibility = View.VISIBLE
                                titleDataKosongAiscTv.visibility = View.VISIBLE
                            }
                            else -> {
//                                val tempList = it.sortedByDescending { obj -> obj.CreateDate }
//                                aLSOReport.clear()
//                                aLSOReport.addAll(tempList)
//                                reportStockOpnameAdapter.initAdapter(aLSOReport)

                                medicineBmRv.visibility = View.VISIBLE
                                animEmptyRsoGiv.visibility = View.GONE
                                titleDataKosongAiscTv.visibility = View.GONE
                            }
                        }
                    }
                }
                if(isSwipeRefresh){binding.refreshRsoSrl.isRefreshing = false}
                binding.rsoPg.visibility = View.GONE
            }

            override fun onDataError(error: String?) {
                error?.let {
                    MedicalUtil.snackBarMessage(it, this@ReportStockOpnameActivity, ConstantsObject.vSnackBarWithTombol)
                }
                binding.rsoPg.visibility = View.GONE
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