package com.arafat1419.mengantri_app.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
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
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    showToolbar(false, null)
                }
                R.id.ticketsFragment -> {
                    showToolbar(true, resources.getString(R.string.title_ticket))
                }
                R.id.favoritesFragment -> {
                    showToolbar(true, resources.getString(R.string.title_favorite))
                }
                R.id.profileFragment -> {
                    showToolbar(true, resources.getString(R.string.title_profile))
                }
                else -> showToolbar(true, null)

            }

            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.homeFragment,
                    R.id.ticketsFragment,
                    R.id.favoritesFragment,
                    R.id.profileFragment
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            binding.mainBottomNavigation.setupWithNavController(navController)

        }
    }

    private fun showToolbar(status: Boolean, title: String?) {
        if (status) {
            binding.materialToolbar.visibility = View.VISIBLE
            binding.txtAppTitle.text = title
        } else {
            binding.materialToolbar.visibility = View.GONE
        }
    }
}