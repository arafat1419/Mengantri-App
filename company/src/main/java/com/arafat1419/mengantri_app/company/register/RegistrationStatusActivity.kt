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

@ExperimentalCoroutinesApi
@FlowPreview
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

        viewModel.getUserCompany(sessionManager.fetchCustomerId()).observe(this) { listCompany ->
            if (listCompany.isNullOrEmpty()) {
                statusHandler(4)
            } else {
                val status = listCompany[0].companyStatus!!
                statusHandler(status)
            }
        }
    }

    private fun statusHandler(status: Int) {
        when (status) {
            0 -> {
                binding.apply {
                    imgRStatus.setImageResource(R.drawable.ic_progress_time)
                    txtRStatus.text =
                        getString(com.arafat1419.mengantri_app.company.R.string.registration_on_process)
                    navigateToProfile()
                }
            }
            1 -> "active"
            2 -> {
                binding.apply {
                    imgRStatus.setImageResource(R.drawable.ic_expired_time)
                    txtRStatus.text =
                        getString(com.arafat1419.mengantri_app.company.R.string.registration_expired)
                    navigateToProfile()
                }
            }
            else -> {

            }
        }
    }

    private fun navigateToProfile() {
        binding.btnRStatus.setOnClickListener { onBackPressed() }
    }
}