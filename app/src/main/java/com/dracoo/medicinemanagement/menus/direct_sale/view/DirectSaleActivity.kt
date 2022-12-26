package com.dracoo.medicinemanagement.menus.direct_sale.view

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ActivityDirectSaleBinding
import com.dracoo.medicinemanagement.menus.direct_sale.viewmodel.DirectSalesViewModel
import com.dracoo.medicinemanagement.menus.main.view.MainActivity
import com.dracoo.medicinemanagement.model.DirectSaleModel
import com.dracoo.medicinemanagement.model.MedicineMasterModel
import com.dracoo.medicinemanagement.model.ThreeColumnModel
import com.dracoo.medicinemanagement.utils.CheckConnectionUtil
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.DataCallback
import com.dracoo.medicinemanagement.utils.MedicalUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.reflect.Type

@AndroidEntryPoint
class DirectSaleActivity : AppCompatActivity(), MedicalUtil.TwoColumnInterface {
    private var isConnected : Boolean = false
    private lateinit var binding: ActivityDirectSaleBinding
    private val directSalesViewModel : DirectSalesViewModel by viewModels()
    private lateinit var popUpSearchMedicine: Dialog
    private var alMstMedicine =  ArrayList<MedicineMasterModel>()
    private var directSaleMl = mutableListOf<DirectSaleModel>()
    private var stUser = ""
    private var dbPiecesPrize = 0.0
    private var stMedicineCode = ""
    private val checkConnection by lazy {
        CheckConnectionUtil(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDirectSaleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back_32)
        }
    }

    override fun onStart() {
        super.onStart()
        checkConnection.observe(this) {
            isConnected = when {
                !it -> false
                else -> true
            }
            initNotConnectedDialog()

            directSalesViewModel.getUserData().observe(this) { itUSer -> stUser = itUSer.toString() }

            directSalesViewModel.getDataMedicine().observe(this) { itList ->
                when {
                    itList?.isNotEmpty() == true -> {
                        binding.dsPg.visibility = View.GONE
                        val type: Type = object : TypeToken<List<MedicineMasterModel?>?>() {}.type
                        val tempMedicineList: List<MedicineMasterModel> = Gson().fromJson(itList, type)
                        if(tempMedicineList.isNotEmpty()){
                            alMstMedicine.addAll(tempMedicineList)
                        }
                    }
                    else -> if(isConnected){ getMedicineData() }
                }
            }

            binding.lblSearchAdmTv.setOnClickListener {
                when(alMstMedicine.size){
                    0 -> MedicalUtil.snackBarMessage("Tidak ada data medicine",
                        this, ConstantsObject.vSnackBarWithOutTombol)
                    else ->{
                        val listThree = ArrayList<ThreeColumnModel>()
                        alMstMedicine.forEach { itLoop ->
                            listThree.add(
                                ThreeColumnModel(
                                    itLoop.namaobat, itLoop.kodeobat,itLoop.hargasatuan)
                            )
                        }

                        popUpSearchMedicine = MedicalUtil.initPopUpSearch2Column(
                            this,getString(R.string.find_medicine),
                            listThree,"Nama Obat",
                            "Kode Obat", this)

                        if(::popUpSearchMedicine.isInitialized && !popUpSearchMedicine.isShowing){
                            popUpSearchMedicine.show()
                        }
                    }
                }
            }

            binding.qtyAdmTiet.addTextChangedListener {
                when{
                    !it.isNullOrBlank() && stMedicineCode.isNotEmpty() ->{
                        activeInActiveAddButton(true)
                    }
                    else -> activeInActiveAddButton(false)
                }
            }
        }
    }

    private fun getMedicineData(){
        binding.dsPg.visibility = View.VISIBLE
        directSalesViewModel.getMasterMedicine(object : DataCallback<List<MedicineMasterModel>> {
            override fun onDataLoaded(data: List<MedicineMasterModel>?) {
                data?.let {
                    if(it.isNotEmpty()){ alMstMedicine.addAll(it) }
                }
                binding.dsPg.visibility = View.GONE
            }

            override fun onDataError(error: String?) {
                error?.let {
                    Timber.e("$error")
                    MedicalUtil.snackBarMessage(it, this@DirectSaleActivity, ConstantsObject.vSnackBarWithOutTombol)
                }
                binding.dsPg.visibility = View.GONE
            }
        })
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

    private fun activeInActiveAddButton(isActive : Boolean){
        binding.addDsBtn.apply {
            when(isActive){
                true -> {
                    backgroundTintList =
                        ContextCompat.getColorStateList(this@DirectSaleActivity, R.color.text_blue1)
                    isEnabled = true
                    binding.bottomDsCl.visibility = View.VISIBLE
                }
                else -> {
                    backgroundTintList =
                        ContextCompat.getColorStateList(this@DirectSaleActivity, R.color.gray_button)
                    isEnabled = false
                    binding.bottomDsCl.visibility = View.GONE
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
        binding.apply {
            stMedicineCode = selectedData.column2
            medicineNameAdsTiet.setText(selectedData.column1)
            dbPiecesPrize = selectedData.column3.toDouble()

            if(popUpSearchMedicine.isShowing){
                popUpSearchMedicine.dismiss()
                popUpSearchMedicine.cancel()
            }
        }
    }
}