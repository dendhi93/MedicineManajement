package com.dracoo.medicinemanagement.menus.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dracoo.medicinemanagement.R

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
    }
}