package com.dracoo.medicinemanagement.menus.stock_opname.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ActivityStockOpnameBinding
import com.dracoo.medicinemanagement.menus.main.view.MainActivity
import com.dracoo.medicinemanagement.menus.stock_opname.view_model.StockOpnameViewModel
import com.dracoo.medicinemanagement.utils.CheckConnectionUtil
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.MedicalUtil
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import timber.log.Timber


@AndroidEntryPoint
class StockOpnameActivity : AppCompatActivity() {
    private val stockOpnameViewModel : StockOpnameViewModel by viewModels()
    private lateinit var binding: ActivityStockOpnameBinding
    private var isFakturNoEmpty = true
    private var isQtyEmpty = true
    private var isConnected = false
    private val checkConnection by lazy {
        CheckConnectionUtil(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockOpnameBinding.inflate(layoutInflater)
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
        }

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
                Timber.e("jsonObject "+stockOpnameViewModel.getDataMedicine().toString())
            }
        }
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
}