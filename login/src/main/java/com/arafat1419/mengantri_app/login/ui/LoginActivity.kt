package com.arafat1419.mengantri_app.login.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arafat1419.mengantri_app.core.utils.LanguageSessionManager
import com.arafat1419.mengantri_app.login.databinding.ActivityLoginBinding
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}