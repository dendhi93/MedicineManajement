package com.dracoo.medicinemanagement.menus.stock_opname.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
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
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ReportStockOpnameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportStockOpnameBinding
    private lateinit var reportStockOpnameAdapter: ReportStockOpnameAdapter
    private val reportSOViewModel : ReportStockOpnameViewModel by viewModels()
    private var aLSOReport: ArrayList<StockOpnameModel> = ArrayList()
    private var isConnected = false
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

    override fun onStart() {
        super.onStart()

        reportStockOpnameAdapter = ReportStockOpnameAdapter(this,onItemClick = {
            Timber.e("data " +it.NamaObat)
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

            if(isConnected){ getDataSO() }
        }
    }

    private fun getDataSO(){
        reportSOViewModel.getDataSO(object :DataCallback<List<StockOpnameModel>>{
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
                                aLSOReport.addAll(it)
                                reportStockOpnameAdapter.initAdapter(aLSOReport)

                                medicineBmRv.visibility = View.VISIBLE
                                animEmptyRsoGiv.visibility = View.GONE
                                titleDataKosongAiscTv.visibility = View.GONE
                            }
                        }
                    }
                }
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