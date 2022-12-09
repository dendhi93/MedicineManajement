package com.dracoo.medicinemanagement.menus.main.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.dracoo.medicinemanagement.databinding.ActivityMainBinding
import com.dracoo.medicinemanagement.databinding.DialogBottomSheetInfoBinding
import com.dracoo.medicinemanagement.menus.main.adapter.CallBackExitApps
import com.dracoo.medicinemanagement.menus.main.adapter.MainActivityAdapter
import com.dracoo.medicinemanagement.menus.main.viewmodel.MainViewModel
import com.dracoo.medicinemanagement.model.MenuModel
import com.dracoo.medicinemanagement.utils.CheckConnectionUtil
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.MedicalUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CallBackExitApps {
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
                    ConstantsObject.vNoConnectionMessage,    this, false)
                else -> MedicalUtil.alertDialogDismiss(ConstantsObject.vNoConnectionTitle,
                    ConstantsObject.vNoConnectionMessage, this, true)
            }
        }

        mainViewModel.getUser().observe(this){ itGetUser ->
            binding.apply {
                nameValueHomeTv.text = itGetUser
                dateValueHomeTv.text = MedicalUtil.getCurrentDateTime(ConstantsObject.vDateSetripJamMinute)

                val alListMainMenu = ArrayList<MenuModel>()
                alListMainMenu.addAll(mainViewModel.initMenu())
                chooseMenuHomeRv.layoutManager = GridLayoutManager(this@MainActivity, 2)
                chooseMenuHomeRv.setHasFixedSize(true)
                chooseMenuHomeRv.adapter = MainActivityAdapter(alListMainMenu, this@MainActivity,
                    this@MainActivity)

                infoHomeIv.setOnClickListener {
                    mainViewModel.getAddress().observe(this@MainActivity){ itGetAddress ->
                        val bottomDialog = BottomSheetDialog(this@MainActivity)
                        val bottomSheetBinding = DialogBottomSheetInfoBinding.inflate(this@MainActivity.layoutInflater)
                        val view = bottomSheetBinding.root
                        bottomDialog.setContentView(view)
                        if(!bottomDialog.isShowing){
                            bottomDialog.show()
                        }

                        bottomSheetBinding.apply {
                            valueNameBtiTv.text = itGetUser
                            valueAddressBtiTv.text = itGetAddress
                            btiCl.setOnClickListener {
                                bottomDialog.dismiss()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onExitApps() {
        MedicalUtil.showDialogConfirmation(this,"Konfirmasi",
            "Apakah anda yakin ingin keluar aplikasi ?"
        ) {
            mainViewModel.clearData()
            finishAffinity()
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