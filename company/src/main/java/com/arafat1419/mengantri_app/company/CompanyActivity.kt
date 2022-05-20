package com.arafat1419.mengantri_app.company

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arafat1419.mengantri_app.company.databinding.ActivityCompanyBinding

class CompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompanyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.companyMaterialToolbar)

        setupBottomNavBar()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        return
    }

    // to setup bottom navigation bar
    private fun setupBottomNavBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.company_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.companyHomeFragment -> {
                    showToolbar(true, resources.getString(R.string.title_company_home))
                }
                R.id.companyServiceFragment -> {
                    showToolbar(true, resources.getString(R.string.title_company_service))
                }
                R.id.companyProfileFragment -> {
                    showToolbar(true, resources.getString(R.string.title_company_profile))
                }
                else -> {
                    actionBar?.hide()
                    binding.companyBottomNavigation.visibility = View.GONE
                }

            }

            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.companyHomeFragment,
                    R.id.companyServiceFragment,
                    R.id.companyProfileFragment
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            binding.companyBottomNavigation.setupWithNavController(navController)

        }
    }

    private fun showToolbar(status: Boolean, title: String?) {
        binding.apply {
            if (status) {
                companyMaterialToolbar.visibility = View.VISIBLE
                txtAppTitle.text = title
            } else {
                companyMaterialToolbar.visibility = View.GONE
            }
            companyBottomNavigation.visibility = View.VISIBLE
        }
    }
}