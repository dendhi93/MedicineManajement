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
import com.dracoo.medicinemanagement.utils.CheckConnectionUtil
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.MedicalUtil
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel : LoginViewModel by viewModels()
    private val checkConnection by lazy {
        CheckConnectionUtil(application)
    }
    private var isConnected = false
    private var isNameEmpty = true
    private var isAddressEmpty = true

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
            nameLoginEt.addTextChangedListener {
                isNameEmpty = it.isNullOrBlank()
                when {
                    !isNameEmpty && !isAddressEmpty && isConnected-> activeInActiveButton(true)
                    else -> activeInActiveButton(false)
                }
            }

            addressLoginTiet.addTextChangedListener {
                isAddressEmpty = it.isNullOrBlank()
                when {
                    !isNameEmpty && !isAddressEmpty && isConnected-> activeInActiveButton(true)
                    else -> activeInActiveButton(false)
                }
            }

            loginBtn.setOnClickListener {
                binding.loginLpi.apply {
                    visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        loginViewModel.saveUser(nameLoginEt.text.toString(), addressLoginTiet.text.toString())
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