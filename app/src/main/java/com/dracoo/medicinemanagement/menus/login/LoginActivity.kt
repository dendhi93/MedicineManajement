package com.dracoo.medicinemanagement.menus.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ActivityLoginBinding
import com.dracoo.medicinemanagement.databinding.ActivitySplashBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}