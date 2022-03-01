package com.arafat1419.mengantri_app.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.ui.detail.detailservice.DetailServiceFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = instantiateFragment()
        if (fragment != null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, fragment)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }.commit()
        }
    }


    private fun instantiateFragment(): Fragment? {
        return try {
            Class.forName("com.arafat1419.mengantri_app.profile.ui.ProfileFragment").newInstance() as Fragment
        } catch (e: Exception) {
            Toast.makeText(this, "Module not found", Toast.LENGTH_SHORT).show()
            null
        }
    }
}