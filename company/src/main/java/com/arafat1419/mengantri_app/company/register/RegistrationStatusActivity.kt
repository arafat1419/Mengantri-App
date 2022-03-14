package com.arafat1419.mengantri_app.company.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arafat1419.mengantri_app.company.R
import com.arafat1419.mengantri_app.company.databinding.ActivityRegistrationStatusBinding
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class RegistrationStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationStatusBinding

    // Initialize viewModel with koin
    private val viewModel: RegisterStatusViewModel by viewModel()

    private lateinit var sessionManager: CustomerSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load koin manually for multi modules
        loadKoinModules(companyModule)

        // Initialize session manager from customer session manager
        sessionManager = CustomerSessionManager(this)
    }
}