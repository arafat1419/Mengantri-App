package com.arafat1419.mengantri_app.core.utils

import android.content.Context
import android.content.SharedPreferences
import com.arafat1419.mengantri_app.core.BuildConfig
import com.arafat1419.mengantri_app.core.domain.model.CustomerDomain

class CustomerSessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(BuildConfig.CUSTOMER_SESSION_MANAGER, Context.MODE_PRIVATE)

    fun saveCustomer(customerDomain: CustomerDomain) {
        val editor = prefs.edit()
            .putInt(CUSTOMER_ID, customerDomain.customerId!!)
            .putString(CUSTOMER_EMAIL, customerDomain.customerEmail)
            .putString(CUSTOMER_NAME, customerDomain.customerName)
            .putString(CUSTOMER_PASS, customerDomain.customerPassword)
            .apply()
    }

    fun fetchCustomerId(): Int = prefs.getInt(CUSTOMER_ID, -1)

    fun fetchCustomerEmail(): String? = prefs.getString(CUSTOMER_EMAIL, null)

    fun fetchCustomerName(): String? = prefs.getString(CUSTOMER_NAME, null)

    fun fetchCustomerPass(): String? = prefs.getString(CUSTOMER_PASS, null)

    companion object {
        const val CUSTOMER_ID = "customer_id"
        const val CUSTOMER_EMAIL = "customer_email"
        const val CUSTOMER_NAME = "customer_name"
        const val CUSTOMER_PASS = "customer_pass"
    }
}