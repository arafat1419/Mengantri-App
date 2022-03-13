package com.arafat1419.mengantri_app.profile.ui

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import com.arafat1419.mengantri_app.profile.R

class ProfilePrefFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.profile_preferences, rootKey)
    }
}