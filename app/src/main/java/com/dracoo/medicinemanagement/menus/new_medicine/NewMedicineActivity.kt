package com.dracoo.medicinemanagement.menus.new_medicine

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ActivityNewMedicineBinding
import com.dracoo.medicinemanagement.databinding.DialogBottomSheetAddMedicineBinding
import com.dracoo.medicinemanagement.menus.main.view.MainActivity
import com.dracoo.medicinemanagement.utils.ThousandSeparatorUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import timber.log.Timber
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class NewMedicineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewMedicineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back_32)
        }
    }

    private fun initBottomSheetAddMedicine(){
        val bottomAddDialog = BottomSheetDialog(this)
        val bottomSheetADdBinding = DialogBottomSheetAddMedicineBinding.inflate(this.layoutInflater)
        val view = bottomSheetADdBinding.root
        bottomAddDialog.setContentView(view)
        bottomAddDialog.setCancelable(false)

        if(!bottomAddDialog.isShowing){
            bottomAddDialog.show()
        }
        bottomSheetADdBinding.apply {
            piecesPrizeBsamTiet.addTextChangedListener(ThousandSeparatorUtil(piecesPrizeBsamTiet))

            cancelBsamButton.setOnClickListener {
                if(bottomAddDialog.isShowing){
                    bottomAddDialog.dismiss()
                }
            }
            saveBsamButton.setOnClickListener {
                if(bottomAddDialog.isShowing){
                    bottomAddDialog.dismiss()
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
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return true
            }
            R.id.menu_item ->{
                initBottomSheetAddMedicine()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}