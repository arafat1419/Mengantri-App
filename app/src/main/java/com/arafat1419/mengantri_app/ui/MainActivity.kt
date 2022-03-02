package com.arafat1419.mengantri_app.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavBar()
    }

    private fun setupBottomNavBar() {
        binding.mainBottomNavigation.setOnItemSelectedListener {
            val fragment: Fragment?
            when(it.itemId) {
                R.id.home_menu -> {
                    fragment = instantiateFragment("com.arafat1419.mengantri_app.home.ui.home.HomeFragment")
                    setActiveFragment(fragment)
                    true
                }
                R.id.ticket_menu -> {
                    fragment = instantiateFragment("com.arafat1419.mengantri_app.ticket.ui.TicketsFragment")
                    setActiveFragment(fragment)
                    true
                }
                R.id.favorite_menu -> {
                    fragment = instantiateFragment("com.arafat1419.mengantri_app.favorite.ui.FavoritesFragment")
                    setActiveFragment(fragment)
                    true
                }
                R.id.profile_menu -> {
                    fragment = instantiateFragment("com.arafat1419.mengantri_app.profile.ui.ProfileFragment")
                    setActiveFragment(fragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun instantiateFragment(className: String): Fragment? {
        return try {
            Class.forName(className).newInstance() as Fragment
        } catch (e: Exception) {
            Toast.makeText(this, "Module not found", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun setActiveFragment(fragment: Fragment?) {
        supportFragmentManager.beginTransaction().apply {
            if (fragment != null) {
                replace(R.id.fragment_container, fragment)
            }
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }.commit()
    }
}