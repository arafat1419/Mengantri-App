package com.arafat1419.mengantri_app.company

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
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

        setSupportActionBar(binding.materialToolbar)

        setupBottomNavBar()
    }

    override fun onBackPressed() {
        if (Navigation.findNavController(
                this,
                R.id.fragment_container
            ).currentDestination?.id == R.id.companyHomeFragment
        ) {
            super.onBackPressed()
        } else {
            Navigation.findNavController(this, R.id.fragment_container).navigateUp()
        }
        return
    }

    // to setup bottom navigation bar
    private fun setupBottomNavBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.companyHomeFragment -> {
                    showToolbar(true)
                }
                R.id.companyServiceFragment -> {
                    showToolbar(true)
                }
                R.id.companyProfileFragment -> {
                    showToolbar(true)
                }
                else -> {
                    actionBar?.hide()
                    showToolbar(false)
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
            binding.bottomNavigation.setupWithNavController(navController)

        }
    }

    private fun showToolbar(status: Boolean) {
        binding.apply {
            if (status) {
                materialToolbar.visibility = View.VISIBLE
                bottomNavigation.visibility = View.VISIBLE
            } else {
                materialToolbar.visibility = View.GONE
                bottomNavigation.visibility = View.GONE
            }
        }
    }
}