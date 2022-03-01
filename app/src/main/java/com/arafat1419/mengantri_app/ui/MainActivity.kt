package com.arafat1419.mengantri_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.ui.companies.CompaniesFragment
import com.arafat1419.mengantri_app.ui.detail.detailservice.DetailServiceFragment
import com.arafat1419.mengantri_app.ui.detail.detailticket.DetailTicketFragment
import com.arafat1419.mengantri_app.ui.home.HomeFragment
import com.arafat1419.mengantri_app.ui.services.ServicesFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, CompaniesFragment())
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }.commit()
    }
}