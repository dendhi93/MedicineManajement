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
import com.dracoo.medicinemanagement.databinding.ActivitySplashBinding
import com.dracoo.medicinemanagement.menus.login.viewmodel.LoginViewModel
import com.dracoo.medicinemanagement.menus.main.MainActivity
import com.dracoo.medicinemanagement.utils.CheckConnectionUtil
import com.dracoo.medicinemanagement.utils.ConstantsObject
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        checkConnection.observe(this){
            when{
                !it -> {
                    MedicalUtil.alertDialogDismiss(
                        ConstantsObject.vNoConnectionTitle,
                        ConstantsObject.vNoConnectionMessage, this, false)
                    isConnected = false
                }
                else -> {
                    MedicalUtil.alertDialogDismiss(
                        ConstantsObject.vNoConnectionTitle,
                        ConstantsObject.vNoConnectionMessage, this, true)
                    isConnected = true
                }
            }
        }

        binding.apply {
            nikLoginEt.addTextChangedListener {
                when{
                    it.isNullOrBlank() -> activeInActiveButton(false)
                    else -> {
                        when(isConnected){
                            true -> activeInActiveButton(true)
                            else -> activeInActiveButton(false)
                        }
                    }
                }
            }

            loginBtn.setOnClickListener {
                binding.loginLpi.apply {
                    visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        loginViewModel.saveUser(nikLoginEt.text.toString())
                        visibility = View.GONE

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    },950)
                }
            }
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