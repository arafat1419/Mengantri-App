package com.arafat1419.mengantri_app.company.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arafat1419.mengantri_app.company.R
import com.arafat1419.mengantri_app.company.databinding.ActivityRegistrationStatusBinding

class RegistrationStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}