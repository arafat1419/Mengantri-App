package com.arafat1419.mengantri_app.profile.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.arafat1419.mengantri_app.profile.R

class ProfilePrefFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.profile_preferences)
    }
}