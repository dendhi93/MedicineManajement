package com.dracoo.medicinemanagement.menus.login.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ActivityLoginBinding
import com.dracoo.medicinemanagement.menus.login.viewmodel.LoginViewModel
import com.dracoo.medicinemanagement.menus.main.view.MainActivity
import com.dracoo.medicinemanagement.model.UserModel
import com.dracoo.medicinemanagement.utils.CheckConnectionUtil
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.DataCallback
import com.dracoo.medicinemanagement.utils.MedicalUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel : LoginViewModel by viewModels()
    private val checkConnection by lazy {
        CheckConnectionUtil(application)
    }
    private var isConnected = false
    private var isNameEmpty = true
    private var isPasswordEmpty = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        checkConnection.observe(this){
            isConnected = when {
                !it -> false
                else -> true
            }
            initNotConnectedDialog()
        }

        binding.apply {
            nameLoginEt.addTextChangedListener {
                isNameEmpty = it.isNullOrBlank()
                when {
                    !isNameEmpty && !isPasswordEmpty && isConnected-> activeInActiveButton(true)
                    else -> activeInActiveButton(false)
                }
            }

            passwordLoginEt.addTextChangedListener {
                isPasswordEmpty = it.isNullOrBlank()
                when {
                    !isNameEmpty && !isPasswordEmpty && isConnected-> activeInActiveButton(true)
                    else -> activeInActiveButton(false)
                }
            }

            loginBtn.setOnClickListener {
                binding.loginLpi.apply {
                    visibility = View.VISIBLE
                    loginViewModel.postLogin(UserModel(
                        createDate = "",
                        username = binding.nameLoginEt.text.toString(),
                        password = binding.passwordLoginEt.text.toString()
                    ), object :DataCallback<UserModel>{
                        override fun onDataLoaded(data: UserModel?) {
                                Handler(Looper.getMainLooper()).post {
                                    data?.let {
                                        loginViewModel.saveUser(
                                            name = it.username,
                                            address = it.address,
                                            password = it.password,
                                            role = it.userRole
                                        )
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            visibility = View.GONE

                                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                            finish()
                                        },90)
                                    } ?: apply {
                                        MedicalUtil.snackBarMessage("data null", this@LoginActivity, ConstantsObject.vSnackBarWithTombol)
                                    }
                                }
                            }

                        override fun onDataError(error: String?) {
                            Handler(Looper.getMainLooper()).post {
                                visibility = View.GONE
                                error?.let {
                                    MedicalUtil.snackBarMessage(it, this@LoginActivity, ConstantsObject.vSnackBarWithOutTombol)
                                } ?: apply { MedicalUtil.snackBarMessage("failed Login ", this@LoginActivity,
                                    ConstantsObject.vSnackBarWithOutTombol) }
                            }
                        }
                    })
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        loginViewModel.saveUser(nameLoginEt.text.toString(), addressLoginTiet.text.toString())
//                        visibility = View.GONE
//
//                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                        finish()
//                    },950)
                }
            }
        }
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

    private fun activeInActiveButton(isActive : Boolean){
        binding.loginBtn.apply {
            when(isActive){
                true -> {
                    backgroundTintList =
                        ContextCompat.getColorStateList(this@LoginActivity, R.color.text_blue1)
                    isEnabled = true
                }
                else -> {
                    backgroundTintList =
                        ContextCompat.getColorStateList(this@LoginActivity, R.color.gray_button)
                    isEnabled = false
                }
            }
        }
    }
}