package com.dracoo.medicinemanagement.menus.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.utils.CheckConnectionUtil
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.MedicalUtil

class MainActivity : AppCompatActivity() {
    var doubleBackToExitPressedOnce = false
    private val checkConnection by lazy {
        CheckConnectionUtil(application)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        checkConnection.observe(this){
            when{
                !it ->MedicalUtil.alertDialogDismiss(ConstantsObject.vNoConnectionTitle,
                    ConstantsObject.vNoConnectionMessage, this, false)
                else -> MedicalUtil.alertDialogDismiss(ConstantsObject.vNoConnectionTitle,
                    ConstantsObject.vNoConnectionMessage, this, true)
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        MedicalUtil.snackBarMessage("Tekan 'Back' sekali lagi untuk keluar",
            this, ConstantsObject.vSnackBarWithOutTombol)


        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}