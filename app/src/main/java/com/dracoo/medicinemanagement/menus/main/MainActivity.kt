package com.dracoo.medicinemanagement.menus.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.utils.CheckConnectionUtil
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.MedicalUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private val mainViewModel : MainViewModel by viewModels()
    private val checkConnection by lazy {
        CheckConnectionUtil(application)
    }

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

        mainViewModel.getUser().observe(this){
            MedicalUtil.snackBarMessage("user $it", this, ConstantsObject.vSnackBarWithOutTombol)
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


        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 1000)
    }
}