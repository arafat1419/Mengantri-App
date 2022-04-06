package com.arafat1419.mengantri_app.company.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.company.databinding.ActivityRegistrationStatusBinding
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class RegistrationStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}