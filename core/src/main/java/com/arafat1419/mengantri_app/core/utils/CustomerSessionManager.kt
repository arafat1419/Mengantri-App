package com.arafat1419.mengantri_app.core.utils

import android.content.Context
import android.content.SharedPreferences
import com.arafat1419.mengantri_app.core.BuildConfig
import com.arafat1419.mengantri_app.core.domain.model.CustomerDomain

class CustomerSessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(BuildConfig.CUSTOMER_SESSION_MANAGER, Context.MODE_PRIVATE)

    fun saveCustomer(customerDomain: CustomerDomain) {
        prefs.edit()
            .putInt(CUSTOMER_ID, customerDomain.customerId!!)
            .putString(CUSTOMER_EMAIL, customerDomain.customerEmail)
            .putString(CUSTOMER_NAME, customerDomain.customerName)
            .putString(CUSTOMER_PASS, customerDomain.customerPassword)
            .putString(CUSTOMER_PHONE, customerDomain.customerPhone)
            .apply()
    }

    fun saveLanguage(language: String) {
        prefs.edit()
            .putString(LANGUAGE_SETTINGS, language)
            .apply()
    }

    fun clearCustomer() {
        prefs.edit()
            .clear()
            .apply()
    }

    fun fetchCustomerId(): Int = prefs.getInt(CUSTOMER_ID, -1)

    fun fetchCustomerEmail(): String? = prefs.getString(CUSTOMER_EMAIL, null)

    fun fetchCustomerName(): String? = prefs.getString(CUSTOMER_NAME, null)

    fun fetchCustomerPass(): String? = prefs.getString(CUSTOMER_PASS, null)

    fun fetchCustomerPhone(): String? = prefs.getString(CUSTOMER_PHONE, null)

    fun fetchLanguage(): String? = prefs.getString(LANGUAGE_SETTINGS, "en")

    companion object {
        const val CUSTOMER_ID = "customer_id"
        const val CUSTOMER_EMAIL = "customer_email"
        const val CUSTOMER_NAME = "customer_name"
        const val CUSTOMER_PASS = "customer_pass"
        const val CUSTOMER_PHONE = "customer_phone"
        const val LANGUAGE_SETTINGS = "language_settings"
    }
}