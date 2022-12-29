package com.dracoo.medicinemanagement.menus.direct_sale.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ActivityDirectSaleBinding
import com.dracoo.medicinemanagement.menus.direct_sale.adapter.DirectSalesAdapter
import com.dracoo.medicinemanagement.menus.direct_sale.viewmodel.DirectSalesViewModel
import com.dracoo.medicinemanagement.menus.main.view.MainActivity
import com.dracoo.medicinemanagement.model.DirectSaleModel
import com.dracoo.medicinemanagement.model.StockOpnameModel
import com.dracoo.medicinemanagement.model.ThreeColumnModel
import com.dracoo.medicinemanagement.utils.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class DirectSaleActivity : AppCompatActivity(), MedicalUtil.TwoColumnInterface {
    private var isConnected : Boolean = false
    private lateinit var binding: ActivityDirectSaleBinding
    private val directSalesViewModel : DirectSalesViewModel by viewModels()
    private lateinit var popUpSearchMedicine: Dialog
    private var alMstMedicine =  mutableListOf<StockOpnameModel>()
    private lateinit var directSalesAdapter: DirectSalesAdapter
    private var directSaleMl = mutableListOf<DirectSaleModel>()
    private var stUser = ""
    private var dbPiecesPrize = "0"
    private var stMedicineCode = ""
    private var intBillTotal = 0
    private var selectedStock = 0
    private lateinit var stBillNo : String
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

        directSalesAdapter = DirectSalesAdapter(this@DirectSaleActivity)
        directSalesAdapter.initAdapter(directSaleMl)
        initRecyleDirectSale()
        binding.saleDsRv.apply {
            layoutManager = LinearLayoutManager(
                this@DirectSaleActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
            adapter = directSalesAdapter
        }

        val swipeToDeleteCallback: SwipeToDeleteCallback = object : SwipeToDeleteCallback(this) {
            @SuppressLint("SetTextI18n")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition
                val item :DirectSaleModel = directSalesAdapter.getData()[position]
                intBillTotal -= item.total.toInt()
                binding.valueDsTotalTv.text = "Rp. "+MedicalUtil.moneyFormat(intBillTotal.toDouble()).toString()
                directSalesAdapter.removeItem(position)
                directSaleMl.remove(item)
                initRecyleDirectSale()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.saleDsRv)
        stBillNo = MedicalUtil.getRandomString(6)
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        checkConnection.observe(this) {
            isConnected = when {
                !it -> false
                else -> true
            }
            initNotConnectedDialog()

            directSalesViewModel.getUserData().observe(this) { itUSer -> stUser = itUSer.toString() }

            if(isConnected){ getMedicineStock() }

            binding.apply {
                lblSearchAdmTv.setOnClickListener {
                    when(alMstMedicine.size){
                        0 -> MedicalUtil.snackBarMessage("Tidak ada data medicine",
                            this@DirectSaleActivity, ConstantsObject.vSnackBarWithOutTombol)
                        else ->{
                            val listThree = ArrayList<ThreeColumnModel>()
                            alMstMedicine.forEach { itLoop ->
                                listThree.add(
                                    ThreeColumnModel(
                                        itLoop.NamaObat, itLoop.HargaSatuan,itLoop.KodeObat, itLoop.Jumlah)
                                )
                            }

                            popUpSearchMedicine = MedicalUtil.initPopUpSearch2Column(
                                this@DirectSaleActivity,getString(R.string.find_medicine),
                                listThree,"Nama Obat",
                                "Harga Obat", this@DirectSaleActivity)

                            if(::popUpSearchMedicine.isInitialized && !popUpSearchMedicine.isShowing){
                                popUpSearchMedicine.show()
                            }
                        }
                    }
                }

                qtyAdmTiet.addTextChangedListener { itEditable ->
                    when{
                        !itEditable.isNullOrBlank() && stMedicineCode.isNotEmpty() ->{
                            activeInActiveAddButton(true)
                        }
                        else -> activeInActiveAddButton(false)
                    }
                }

                addDsBtn.setOnClickListener {
                    MedicalUtil.showDialogConfirmation(
                        this@DirectSaleActivity, ConstantsObject.vConfirmTitle,
                        "Apakah anda yakin ingin menambahkan "+medicineNameAdsTiet.text.toString() + " ?"
                    ){
                        val isSameData = directSaleMl.find { itFind ->
                            stMedicineCode == itFind.kodeObat
                        }
                        when(isSameData != null){
                            false ->{
                                when {
                                    qtyAdmTiet.text.toString().toInt() > selectedStock -> MedicalUtil.alertDialogDismiss(
                                        "Stock tidak mencukupi",
                                        ConstantsObject.vConfirmTitle, this@DirectSaleActivity, false)
                                    else -> {
                                       directSaleMl.add(DirectSaleModel(
                                            noTagihan = stBillNo,
                                            kodeObat = stMedicineCode,
                                            namaObat = medicineNameAdsTiet.text.toString(),
                                            hargaSatuan = dbPiecesPrize,
                                            jumlah = qtyAdmTiet.text.toString() ,
                                            total = (qtyAdmTiet.text.toString().toInt() * dbPiecesPrize.toInt()).toString(),
                                            createDate = MedicalUtil.getCurrentDateTime(ConstantsObject.vDateGaringJam),
                                            userCreate = stUser
                                        ))
                                        intBillTotal += (qtyAdmTiet.text.toString().toInt() * dbPiecesPrize.toInt())
                                        dbPiecesPrize = "0"
                                        valueDsTotalTv.text = "Rp. "+MedicalUtil.moneyFormat(intBillTotal.toDouble()).toString()
                                        medicineNameAdsTiet.setText("")
                                        qtyAdmTiet.setText("")
                                        directSalesAdapter.initAdapter(directSaleMl)
                                        initRecyleDirectSale()
                                        selectedStock = 0
                                    }
                                }

                            }
                            else -> {
                                MedicalUtil.toastMessage(this@DirectSaleActivity, "obat sudah ada dalam daftar obat", ConstantsObject.vShortToast)
                                medicineNameAdsTiet.setText("")
                                qtyAdmTiet.setText("")
                                activeInActiveAddButton(false)
                            }
                        }
                    }
                }

                saveDsBtn.setOnClickListener {
                    MedicalUtil.showDialogConfirmation(
                        this@DirectSaleActivity, ConstantsObject.vConfirmTitle,
                        "Apakah anda yakin ingin simpan transaksi ini ?"
                    ){
                        Timber.e("transaksi")
                        MedicalUtil.snackBarMessage("No Tagihan anda " +directSaleMl[0].noTagihan,
                            this@DirectSaleActivity, ConstantsObject.vSnackBarWithOutTombol)
                    }
                }
            }
        }
    }

    private fun getMedicineStock(){
        binding.dsPg.visibility = View.VISIBLE
        directSalesViewModel.getDataSO(object :DataCallback<List<StockOpnameModel>>{
            override fun onDataLoaded(data: List<StockOpnameModel>?) {
                data?.let {
                    if(it.isNotEmpty()){
                        alMstMedicine.addAll(it)
                        it.forEach { itLoop ->
                            if(itLoop.HargaSatuan == "0"){ alMstMedicine.remove(itLoop) }
                        }
                    }
                }
                binding.dsPg.visibility = View.GONE
            }

            override fun onDataError(error: String?) {
                error?.let {
                    MedicalUtil.snackBarMessage(it, this@DirectSaleActivity, ConstantsObject.vSnackBarWithTombol)
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
                }
                else -> {
                    backgroundTintList =
                        ContextCompat.getColorStateList(this@DirectSaleActivity, R.color.gray_button)
                    isEnabled = false
                }
            }
        }
    }

    private fun initRecyleDirectSale(){
        binding.apply {
            when(directSaleMl.size){
                0 ->{
                    animEmptyDsGiv.visibility = View.VISIBLE
                    titleDataKosongDsTv.visibility = View.VISIBLE
                    saleDsRv.visibility = View.GONE
                    bottomDsCl.visibility = View.GONE
                    saveDsBtn.backgroundTintList =
                        ContextCompat.getColorStateList(this@DirectSaleActivity, R.color.gray_button)
                    saveDsBtn.isEnabled = false
                }
                else ->{
                    animEmptyDsGiv.visibility = View.GONE
                    titleDataKosongDsTv.visibility = View.GONE
                    saleDsRv.visibility = View.VISIBLE
                    bottomDsCl.visibility = View.VISIBLE
                    saveDsBtn.backgroundTintList =
                        ContextCompat.getColorStateList(this@DirectSaleActivity, R.color.text_blue1)
                    saveDsBtn.isEnabled = true
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
            stMedicineCode = selectedData.column3
            medicineNameAdsTiet.setText(selectedData.column1)
            dbPiecesPrize = selectedData.column2
            selectedStock = selectedData.column4.toInt()

            if(popUpSearchMedicine.isShowing){
                popUpSearchMedicine.dismiss()
                popUpSearchMedicine.cancel()
            }
        }
    }
}