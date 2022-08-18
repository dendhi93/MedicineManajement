package com.dracoo.medicinemanagement.menus.new_medicine.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ActivityNewMedicineBinding
import com.dracoo.medicinemanagement.databinding.DialogBottomSheetAddMedicineBinding
import com.dracoo.medicinemanagement.menus.main.view.MainActivity
import com.dracoo.medicinemanagement.menus.new_medicine.viewmodel.NewMedicineViewModel
import com.dracoo.medicinemanagement.model.MedicineMasterModel
import com.dracoo.medicinemanagement.utils.CheckConnectionUtil
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.MedicalUtil
import com.dracoo.medicinemanagement.utils.ThousandSeparatorUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NewMedicineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewMedicineBinding
    private var isCodeMedicineEmpty = true
    private var isMedicineNameEmpty = true
    private var isPieceTypeEmpty = true
    private var isConnected = false
    private val newMedicineViewModel : NewMedicineViewModel by viewModels()
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
            if(isConnected){
                getMedicineData()
            }
        }
    }

    private fun getMedicineData(){
        binding.nmPg.visibility = View.VISIBLE
        newMedicineViewModel.getMasterMedicine(object : NewMedicineViewModel.DataCallback<List<MedicineMasterModel>>{
            override fun onDataLoaded(data: List<MedicineMasterModel>?) {
                data?.let {
                    Timber.e("array size " +it.size)
                }
                binding.nmPg.visibility = View.GONE
            }

            override fun onDataError(error: String?) {
                error?.let {
                    Timber.e("$error")
                }
                binding.nmPg.visibility = View.GONE
            }
        })
    }

    private fun initBottomSheetAddMedicine(){
        val bottomAddDialog = BottomSheetDialog(this)
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
                }
            }
            saveBsamButton.setOnClickListener {
                when {
                    piecesPrizeBsamTiet.text.toString().isNotEmpty() -> {
                        //                Timber.e("value "
//                + ThousandSeparatorUtil.trimCommaOfString(piecesPrizeBsamTiet.text.toString()))
                        if(bottomAddDialog.isShowing){
                            bottomAddDialog.dismiss()
                        }
                    }
                    else -> {
                        MedicalUtil.snackBarMessage("Mohon diisi harga satuan",
                            this@NewMedicineActivity, ConstantsObject.vSnackBarWithOutTombol)
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
                initBottomSheetAddMedicine()
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


//to get value :  NumberTextWatcherForThousand.trimCommaOfString(editText.getText().toString())