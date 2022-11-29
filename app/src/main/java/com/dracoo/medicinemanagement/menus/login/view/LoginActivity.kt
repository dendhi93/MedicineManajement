package com.dracoo.medicinemanagement.menus.login.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ActivityLoginBinding
import com.dracoo.medicinemanagement.menus.login.viewmodel.LoginViewModel
import com.dracoo.medicinemanagement.model.UserModel
import com.dracoo.medicinemanagement.utils.CheckConnectionUtil
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.DataCallback
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
                    Timber.e("un " +binding.nameLoginEt.text.toString())
                    Timber.e("pass " +binding.passwordLoginEt.text.toString())
                    loginViewModel.postLogin(UserModel(
                        createDate = "",
                        username = binding.nameLoginEt.text.toString(),
                        password = binding.passwordLoginEt.text.toString()
                    ), object :DataCallback<UserModel>{
                        override fun onDataLoaded(data: UserModel?) {
                            visibility = View.GONE
                        }

                        override fun onDataError(error: String?) {
                            visibility = View.GONE
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