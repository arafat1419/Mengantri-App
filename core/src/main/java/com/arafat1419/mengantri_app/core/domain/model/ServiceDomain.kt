package com.arafat1419.mengantri_app.core.domain.model

import com.google.gson.annotations.SerializedName

data class ServiceDomain(
    val serviceId: Int? = null,
    val companyId: Int? = null,
    val serviceName: String? = null,
    val serviceOpenTime: String? = null,
    val serviceCloseTime: String? = null,
    val serviceAnnouncement: String? = null,
    val serviceMaxCustomer: Int? = null,
    val serviceStatus: Int? = null,
    val serviceDateCreated: String? = null,
    val serviceDateUpdated: Any? = null
)
