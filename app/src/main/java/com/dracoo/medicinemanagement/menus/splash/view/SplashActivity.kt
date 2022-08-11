package com.dracoo.medicinemanagement.menus.splash.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dracoo.medicinemanagement.databinding.ActivitySplashBinding
import com.dracoo.medicinemanagement.menus.splash.viewmodel.SplashViewModel
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dracoo.medicinemanagement.menus.login.view.LoginActivity
import com.dracoo.medicinemanagement.menus.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val mSplashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenCreated {
            mSplashViewModel.getUser().observe(this@SplashActivity){
                when(it){
                    "" -> {
                        Handler(Looper.getMainLooper()).postDelayed({
                            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                            finish()
                        },950)
                    }
                    else -> {
                        Handler(Looper.getMainLooper()).postDelayed({
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        },950)
                    }
                }
            }
        }
    }
}