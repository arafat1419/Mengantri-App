package com.arafat1419.mengantri_app.core.utils

import android.content.Context
import android.content.SharedPreferences
import com.arafat1419.mengantri_app.core.BuildConfig
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain

class CompanySessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(BuildConfig.COMPANY_SESSION_MANAGER, Context.MODE_PRIVATE)

    fun saveCompany(companyDomain: CompanyDomain) {
        prefs.edit()
            .putInt(COMPANY_ID, companyDomain.companyId!!)
            .putString(COMPANY_NAME, companyDomain.companyName)
            .putString(COMPANY_IMAGE, companyDomain.companyImage)
            .apply()
    }

    fun clearCompany() {
        prefs.edit()
            .clear()
            .apply()
    }

    fun fetchCompanyId(): Int = prefs.getInt(COMPANY_ID, -1)

    fun fetchCompanyName(): String? = prefs.getString(COMPANY_NAME, null)

    fun fetchCompanyImage(): String? = prefs.getString(COMPANY_IMAGE, null)

    companion object {
        const val COMPANY_ID = "company_id"
        const val COMPANY_NAME = "company_name"
        const val COMPANY_IMAGE = "company_image"
    }
}