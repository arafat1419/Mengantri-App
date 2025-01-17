package com.arafat1419.mengantri_app.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.core.utils.LanguageSessionManager
import com.arafat1419.mengantri_app.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val languageSessionManager: LanguageSessionManager by lazy { LanguageSessionManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLanguage()

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

        navController.addOnDestinationChangedListener { controller, destination, _ ->
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
                else -> {
                    actionBar?.hide()
                    showToolbar(false, null)
                }

            }

            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.homeFragment,
                    R.id.ticketsFragment,
                    R.id.favoritesFragment,
                    R.id.profileFragment
                ),
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            binding.mainBottomNavigation.setupWithNavController(navController)
        }
    }

    private fun showToolbar(status: Boolean, title: String?) {
        binding.apply {
            if (status) {
                materialToolbar.visibility = View.VISIBLE
                txtAppTitle.text = title
            } else {
                materialToolbar.visibility = View.GONE
            }
        }
    }

    private fun setLanguage() {
        val language = Locale(languageSessionManager.fetchLanguage()!!)
        Locale.setDefault(language)
        val config = resources.configuration
        config.setLocale(language)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
}