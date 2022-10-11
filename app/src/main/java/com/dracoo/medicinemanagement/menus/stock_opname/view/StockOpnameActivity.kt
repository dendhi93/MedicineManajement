package com.dracoo.medicinemanagement.menus.stock_opname.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ActivityStockOpnameBinding
import com.dracoo.medicinemanagement.menus.main.view.MainActivity
import com.dracoo.medicinemanagement.menus.stock_opname.view_model.StockOpnameViewModel
import com.dracoo.medicinemanagement.model.MedicineMasterModel
import com.dracoo.medicinemanagement.model.StockOpnameModel
import com.dracoo.medicinemanagement.model.ThreeColumnModel
import com.dracoo.medicinemanagement.utils.CheckConnectionUtil
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.MedicalUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.reflect.Type


@AndroidEntryPoint
class StockOpnameActivity : AppCompatActivity(), MedicalUtil.TwoColumnInterface {
    private val stockOpnameViewModel : StockOpnameViewModel by viewModels()
    private lateinit var binding: ActivityStockOpnameBinding
    private var isFakturNoEmpty = true
    private var isQtyEmpty = true
    private var isConnected = false
    private lateinit var popUpSearchMedicine: Dialog
    private var stPiecesPrize = ""
    private var stUser = ""
    private val checkConnection by lazy {
        CheckConnectionUtil(application)
    }
    private var alMstMedicine =  ArrayList<MedicineMasterModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockOpnameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back_32)
        }

        checkConnection.observe(this){
            isConnected = when {
                !it -> {
                    MedicalUtil.alertDialogDismiss(
                        ConstantsObject.vNoConnectionTitle,
                        ConstantsObject.vNoConnectionMessage, this, false)
                    false
                }
                else -> {
                    MedicalUtil.alertDialogDismiss(
                        ConstantsObject.vNoConnectionTitle,
                        ConstantsObject.vNoConnectionMessage, this, true)
                    true
                }
            }
        }

        stockOpnameViewModel.getUserData().observe(this) { stUser = it.toString() }
        stockOpnameViewModel.getDataMedicine().observe(this) {
            when {
                it?.isNotEmpty() == true -> {
                    val type: Type = object : TypeToken<List<MedicineMasterModel?>?>() {}.type
                    val tempMedicineList: List<MedicineMasterModel> = Gson().fromJson(it, type)
                    if(tempMedicineList.isNotEmpty()){
                        alMstMedicine.addAll(tempMedicineList)
                    }
                }
                else -> if(isConnected){ getMedicineData() }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.apply {
            fakturNoSoTiet.addTextChangedListener {
                isFakturNoEmpty = it.isNullOrBlank()
                when{
                    medicineCodeSoTiet.text!!.isNotEmpty() &&
                            !isFakturNoEmpty && !isQtyEmpty -> activeInActiveButton(true)
                    else -> activeInActiveButton(false)
                }
            }
            qtySoTiet.addTextChangedListener {
                isQtyEmpty = it.isNullOrBlank()
                when{
                    medicineCodeSoTiet.text!!.isNotEmpty() &&
                            !isFakturNoEmpty && !isQtyEmpty -> activeInActiveButton(true)
                    else -> activeInActiveButton(true)
                }
            }
            lblSearchSoTv.setOnClickListener {
                when(alMstMedicine.size){
                    0 -> MedicalUtil.snackBarMessage("Tidak ada data medicine",
                        this@StockOpnameActivity, ConstantsObject.vSnackBarWithOutTombol)
                    else ->{
                        val listVendor = ArrayList<ThreeColumnModel>()
                        alMstMedicine.forEach {
                            listVendor.add(
                                ThreeColumnModel(
                                it.namaobat, it.kodeobat,it.hargasatuan)
                            )
                        }

                        popUpSearchMedicine = MedicalUtil.initPopUpSearch2Column(
                            this@StockOpnameActivity,getString(R.string.find_medicine),
                            listVendor,"Nama Obat",
                            "Kode Obat", this@StockOpnameActivity)

                        if(::popUpSearchMedicine.isInitialized && !popUpSearchMedicine.isShowing){
                            popUpSearchMedicine.show()
                        }
                    }
                }
            }

            saveSoBtn.setOnClickListener {
                binding.apply {
                    nmSo.visibility = View.VISIBLE
                    saveSoBtn.isEnabled = false
                    stockOpnameViewModel.transactionStockOpname(StockOpnameModel(
                        medicineCodeSoTiet.text.toString(),
                        medicineNameSoTiet.text.toString(),
                        fakturNoSoTiet.text.toString(),
                        stPiecesPrize,
                        qtySoTiet.text.toString(),
                        MedicalUtil.getCurrentDateTime(ConstantsObject.vDateGaringJam),
                        stUser
                    ), object : StockOpnameViewModel.DataCallback<String>{
                        override fun onDataLoaded(data: String?) {
                            nmSo.visibility = View.GONE
                            saveSoBtn.isEnabled = true
                            medicineCodeSoTiet.setText("")
                            medicineNameSoTiet.setText("")
                            fakturNoSoTiet.setText("")
                            prizeSoTiet.setText("")
                            qtySoTiet.setText("")

                            data?.let {
                                MedicalUtil.snackBarMessage("Transaksi $it", this@StockOpnameActivity, ConstantsObject.vSnackBarWithOutTombol)
                            }
                        }

                        override fun onDataError(error: String?) {
                            nmSo.visibility = View.GONE
                            saveSoBtn.isEnabled = true

                            MedicalUtil.snackBarMessage("failed $error", this@StockOpnameActivity, ConstantsObject.vSnackBarWithOutTombol)
                        }
                    })
                }
            }
        }
    }

    private fun getMedicineData(){
        binding.nmSo.visibility = View.VISIBLE
        stockOpnameViewModel.getMasterMedicine(object : StockOpnameViewModel.DataCallback<List<MedicineMasterModel>>{
            override fun onDataLoaded(data: List<MedicineMasterModel>?) {
                data?.let {
                    if(it.isNotEmpty()){
                        alMstMedicine.addAll(it)
                    }
                }
                binding.nmSo.visibility = View.GONE
            }

            override fun onDataError(error: String?) {
                error?.let {
                    Timber.e("$error")
                    MedicalUtil.snackBarMessage(it, this@StockOpnameActivity, ConstantsObject.vSnackBarWithOutTombol)
                }
                binding.nmSo.visibility = View.GONE
            }
        })
    }

    private fun activeInActiveButton(isActive : Boolean){
        binding.saveSoBtn.apply {
            when(isActive){
                true -> {
                    backgroundTintList =
                        ContextCompat.getColorStateList(this@StockOpnameActivity, R.color.text_blue1)
                    isEnabled = true
                }
                else -> {
                    backgroundTintList =
                        ContextCompat.getColorStateList(this@StockOpnameActivity, R.color.gray_button)
                    isEnabled = false
                }
            }
        }
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

    override fun selectedTwoSearch(selectedData: ThreeColumnModel) {
        binding.medicineCodeSoTiet.setText(selectedData.column2)
        binding.medicineNameSoTiet.setText(selectedData.column1)
        binding.prizeSoTiet.setText(MedicalUtil.moneyFormat(selectedData.column3.toDouble()))
        stPiecesPrize = selectedData.column3

        if(popUpSearchMedicine.isShowing){
            popUpSearchMedicine.dismiss()
            popUpSearchMedicine.cancel()
        }
    }
}