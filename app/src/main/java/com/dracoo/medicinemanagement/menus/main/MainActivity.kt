package com.dracoo.medicinemanagement.menus.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ActivityMainBinding
import com.dracoo.medicinemanagement.databinding.ActivitySplashBinding
import com.dracoo.medicinemanagement.utils.CheckConnectionUtil
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.MedicalUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel : MainViewModel by viewModels()
    private val checkConnection by lazy {
        CheckConnectionUtil(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
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
            binding.apply {
                nameValueHomeTv.text = it
                dateValueHomeTv.text = MedicalUtil.getCurrentDateTime(ConstantsObject.vDateSetripJamMinute)
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


        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 1000)
    }
}