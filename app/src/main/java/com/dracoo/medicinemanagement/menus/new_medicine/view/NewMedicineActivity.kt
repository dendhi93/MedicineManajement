package com.dracoo.medicinemanagement.menus.new_medicine.view

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ActivityNewMedicineBinding
import com.dracoo.medicinemanagement.databinding.DialogBottomSheetAddMedicineBinding
import com.dracoo.medicinemanagement.menus.main.view.MainActivity
import com.dracoo.medicinemanagement.menus.new_medicine.adapter.NewMedicineAdapter
import com.dracoo.medicinemanagement.menus.new_medicine.viewmodel.NewMedicineViewModel
import com.dracoo.medicinemanagement.model.MedicineMasterModel
import com.dracoo.medicinemanagement.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.reflect.Type


@AndroidEntryPoint
class NewMedicineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewMedicineBinding
    private var isCodeMedicineEmpty = true
    private var isMedicineNameEmpty = true
    private var isPieceTypeEmpty = true
    private var isConnected = false
    private var selectedInputMode = ""
    private val newMedicineViewModel : NewMedicineViewModel by viewModels()
    private lateinit var newMedicineAdapter: NewMedicineAdapter
    private var aLMasterMedical: ArrayList<MedicineMasterModel> = ArrayList()
    private val checkConnection by lazy {
        CheckConnectionUtil(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back_32)
        }
    }

    override fun onStart() {
        super.onStart()

        initListAdapter(aLMasterMedical)
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

            newMedicineViewModel.getDataMedicine().observe(this){ itObserve ->
                itObserve.let { itLet ->
                    when{
                        itLet?.isNotEmpty() == true -> {
                            val type: Type = object : TypeToken<List<MedicineMasterModel?>?>() {}.type
                            val tempMedicineList: List<MedicineMasterModel> = Gson().fromJson(itObserve, type)
                            if(tempMedicineList.isNotEmpty()){
                                val tempList = tempMedicineList.sortedByDescending { obj -> obj.Timestamp }
                                aLMasterMedical.addAll(tempList)
                                newMedicineAdapter.initAdapter(aLMasterMedical)

                                binding.apply {
                                    nmPg.visibility = View.GONE
                                    medicineBmRv.visibility = View.VISIBLE
                                    animEmptyNmGiv.visibility = View.GONE
                                    titleDataKosongAiscTv.visibility = View.GONE
                                }
                            }
                        }
                        else -> if(isConnected){ getMedicineData(false) }
                    }
                }
            }
        }

        binding.apply {
            searchNmTiet.addTextChangedListener {
                when(aLMasterMedical.size){
                    0 -> MedicalUtil.snackBarMessage(getString(R.string.empty_data),
                        this@NewMedicineActivity, ConstantsObject.vSnackBarWithOutTombol)
                    else ->{
                        when(it?.length){
                            0 -> newMedicineAdapter.initAdapter(aLMasterMedical)
                            else -> {
                                //remove duplicate
                                val selectedArrayList = MedicalUtil.filterMedicineMaster(it.toString(), aLMasterMedical).distinct().toList()
                                newMedicineAdapter.initAdapter(ArrayList(selectedArrayList))
                            }
                        }
                    }
                }
            }

            refreshNmSrl.setOnRefreshListener{
                refreshNmSrl.isRefreshing = true
                when(isConnected){
                    true -> getMedicineData(true)
                    else ->{
                        Handler(Looper.getMainLooper()).postDelayed({
                            refreshNmSrl.isRefreshing = false
                        },100)
                    }
                }
            }
        }
    }

    private fun getMedicineData(isFromSwipe : Boolean){
        binding.nmPg.visibility = View.VISIBLE
        if(isFromSwipe){
            aLMasterMedical.clear()
            binding.medicineBmRv.visibility = View.GONE
            binding.animEmptyNmGiv.visibility = View.VISIBLE
            binding.titleDataKosongAiscTv.visibility = View.VISIBLE
        }

        newMedicineViewModel.getMasterMedicine(object : DataCallback<List<MedicineMasterModel>> {
            override fun onDataLoaded(data: List<MedicineMasterModel>?) {
                data?.let {
                    binding.apply {
                        when(it.size){
                            0 ->{
                                medicineBmRv.visibility = View.GONE
                                animEmptyNmGiv.visibility = View.VISIBLE
                                titleDataKosongAiscTv.visibility = View.VISIBLE
                            }
                            else ->{
                                val tempList = it.sortedByDescending { obj -> obj.Timestamp }
                                aLMasterMedical.addAll(tempList)
                                newMedicineAdapter.initAdapter(aLMasterMedical)
                                medicineBmRv.visibility = View.VISIBLE
                                animEmptyNmGiv.visibility = View.GONE
                                titleDataKosongAiscTv.visibility = View.GONE
                            }
                        }
                    }
                }

                binding.nmPg.visibility = View.GONE
                if(isFromSwipe){ binding.refreshNmSrl.isRefreshing = false }
            }

            override fun onDataError(error: String?) {
                error?.let {
                    Timber.e("$error")
                    MedicalUtil.snackBarMessage(it, this@NewMedicineActivity, ConstantsObject.vSnackBarWithOutTombol)
                }
                binding.nmPg.visibility = View.GONE
            }
        })
    }

    private fun initBottomSheetAddMedicine(selectedModel : MedicineMasterModel?){
        val bottomAddDialog = BottomSheetDialog(this)
        bottomAddDialog.setOnShowListener {
            val bottomSheet: FrameLayout = bottomAddDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet) ?: return@setOnShowListener
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            if (bottomSheet.layoutParams != null) {
                val layoutParams = bottomSheet.layoutParams
                layoutParams.height = Resources.getSystem().displayMetrics.heightPixels
                bottomSheet.layoutParams = layoutParams
            }
            bottomSheet.setBackgroundResource(android.R.color.transparent)
            bottomSheetBehavior.skipCollapsed = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        val bottomSheetAddBinding = DialogBottomSheetAddMedicineBinding.inflate(this.layoutInflater)
        val view = bottomSheetAddBinding.root
        bottomAddDialog.setContentView(view)

        if(!bottomAddDialog.isShowing){
            bottomAddDialog.show()
        }
        bottomSheetAddBinding.apply {
            piecesPrizeBsamTiet.addTextChangedListener(ThousandSeparatorUtil(piecesPrizeBsamTiet))
            medicineCodeBsamTiet.addTextChangedListener {
                isCodeMedicineEmpty = it.isNullOrBlank()
                activeInActiveDialogButton(bottomSheetAddBinding)
            }
            medicineNameBsamTiet.addTextChangedListener {
                isMedicineNameEmpty = it.isNullOrBlank()
                activeInActiveDialogButton(bottomSheetAddBinding)
            }
            piecesTypeBsamTiet.addTextChangedListener {
                isPieceTypeEmpty = it.isNullOrBlank()
                activeInActiveDialogButton(bottomSheetAddBinding)
            }

            cancelBsamButton.setOnClickListener {
                if(bottomAddDialog.isShowing){
                    bottomAddDialog.dismiss()
                    bottomAddDialog.cancel()
                }
            }
            saveBsamButton.setOnClickListener {
                when {
                    piecesPrizeBsamTiet.text.toString().isNotEmpty() -> {
                        //                Timber.e("value "
//                + ThousandSeparatorUtil.trimCommaOfString(piecesPrizeBsamTiet.text.toString()))
                        bottomSheetAddBinding.bottomLp.visibility = View.VISIBLE
                        bottomSheetAddBinding.saveBsamButton.isEnabled = false
                        val stPrize = when(piecesPrizeBsamTiet.text.toString()){
                            "1" -> "0"
                            else -> ThousandSeparatorUtil.trimCommaOfString(piecesPrizeBsamTiet.text.toString())
                        }
                        newMedicineViewModel.postNewMedicine(MedicineMasterModel(
                            Timestamp = MedicalUtil.getCurrentDateTime(ConstantsObject.vDateGaringJam),
                            kodeobat = medicineCodeBsamTiet.text.toString(),
                            satuanobat = piecesTypeBsamTiet.text.toString(),
                            hargasatuan = stPrize,
                            namaobat = medicineNameBsamTiet.text.toString(),
                            kategoriObat = medicineCategoryBsamTiet.text.toString()

                        ), object :DataCallback<MedicineMasterModel>{
                            override fun onDataLoaded(data: MedicineMasterModel?) {
                                data?.let {
                                    aLMasterMedical.add(data)
//                                    newMedicineAdapter = NewMedicineAdapter(aLMasterMedical, this@NewMedicineActivity)
                                    newMedicineAdapter.initAdapter(aLMasterMedical)
                                }
                                bottomSheetAddBinding.bottomLp.visibility = View.GONE
                                bottomSheetAddBinding.saveBsamButton.isEnabled = true
                                if(bottomAddDialog.isShowing){
                                    bottomAddDialog.dismiss()
                                    bottomAddDialog.cancel()
                                }
                            }

                            override fun onDataError(error: String?) {
                                bottomSheetAddBinding.bottomLp.visibility = View.GONE
                                bottomSheetAddBinding.saveBsamButton.isEnabled = true
                                if(bottomAddDialog.isShowing){
                                    bottomAddDialog.dismiss()
                                    bottomAddDialog.cancel()
                                }
                                MedicalUtil.snackBarMessage("failed $error", this@NewMedicineActivity, ConstantsObject.vSnackBarWithOutTombol)
                            }
                        })
                    }
                    else -> {
                        MedicalUtil.snackBarMessage("Mohon diisi harga satuan",
                            this@NewMedicineActivity, ConstantsObject.vSnackBarWithOutTombol)
                    }
                }
            }

            when(selectedInputMode){
                ConstantsObject.vNewData -> {
                    saveBsamButton.visibility = View.VISIBLE
                    cancelBsamButton.visibility = View.VISIBLE
                }
                else -> {
                    saveBsamButton.visibility = View.GONE
                    cancelBsamButton.visibility = View.GONE
                    selectedModel?.let {
                        medicineCodeBsamTiet.setText(it.kodeobat)
                        medicineNameBsamTiet.setText(it.namaobat)
                        medicineCategoryBsamTiet.setText(it.kategoriObat)
                        piecesTypeBsamTiet.setText(it.satuanobat)
                        when(val piecesPrize = it.hargasatuan){
                            "0" -> piecesPrizeBsamTiet.setText(piecesPrize)
                            else -> piecesPrizeBsamTiet.setText(MedicalUtil.moneyFormat(piecesPrize.toDouble()))
                        }
                    }
                }
            }
        }
    }

    private fun activeInActiveDialogButton(dialogBinding : DialogBottomSheetAddMedicineBinding){
        dialogBinding.apply {
            when{
                !isCodeMedicineEmpty && !isMedicineNameEmpty
                        && !isPieceTypeEmpty
                        && piecesPrizeBsamTiet.text.toString().isNotEmpty()
                        && isConnected -> {
                            saveBsamButton.backgroundTintList =
                                ContextCompat.getColorStateList(this@NewMedicineActivity, R.color.text_blue1)
                            saveBsamButton.isEnabled = true
                        }
                else ->{
                    saveBsamButton.backgroundTintList =
                        ContextCompat.getColorStateList(this@NewMedicineActivity, R.color.gray_button)
                    saveBsamButton.isEnabled = false
                }
            }
        }
    }

    private fun initListAdapter(list: ArrayList<MedicineMasterModel>){
        newMedicineAdapter = NewMedicineAdapter(this,onItemClick = {
            selectedInputMode = ConstantsObject.vShowData
            initBottomSheetAddMedicine(it)
        })

        newMedicineAdapter.initAdapter(list)
        binding.medicineBmRv.apply {
            layoutManager = LinearLayoutManager(
                this@NewMedicineActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
            adapter = newMedicineAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.menu_item ->{
                selectedInputMode = ConstantsObject.vNewData
                initBottomSheetAddMedicine(null)
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