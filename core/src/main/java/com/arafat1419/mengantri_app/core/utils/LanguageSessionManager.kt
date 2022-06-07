package com.arafat1419.mengantri_app.core.utils

import android.content.Context
import android.content.SharedPreferences

class LanguageSessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences("Language", Context.MODE_PRIVATE)

    fun saveLanguage(language: String) {
        prefs.edit()
            .putString(LANGUAGE_SETTINGS, language)
            .apply()
    }

    fun fetchLanguage(): String? = prefs.getString(LANGUAGE_SETTINGS, "en")

    companion object {
        const val LANGUAGE_SETTINGS = "language_settings"
    }
}