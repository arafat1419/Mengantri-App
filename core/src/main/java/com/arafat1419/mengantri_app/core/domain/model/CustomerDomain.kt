package com.arafat1419.mengantri_app.core.domain.model

data class CustomerDomain(
    val customerId: Int? = null,
    val customerName: String? = null,
    val customerEmail: String? = null,
    val customerPassword: String? = null,
    val customerPhone: String? = null,
    val customerLocation: String? = null,
    val customerStatus: Int? = null,
    val customerDateCreated: String? = null,
    val customerDateUpdated: String? = null
)