package com.arafat1419.mengantri_app.login.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arafat1419.mengantri_app.login.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    // initialize binding
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}