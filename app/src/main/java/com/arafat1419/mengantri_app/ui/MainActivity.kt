package com.arafat1419.mengantri_app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)

        setupBottomNavBar()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        return
    }

    // to setup bottom navigation bar
    private fun setupBottomNavBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController


        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.ticketsFragment,
                R.id.profileFragment
            ),
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.mainBottomNavigation.setupWithNavController(navController)

    }
}