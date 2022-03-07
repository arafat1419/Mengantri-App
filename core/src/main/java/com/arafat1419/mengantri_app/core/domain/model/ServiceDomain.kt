package com.arafat1419.mengantri_app.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceDomain(
    val serviceId: Int? = null,
    val companyId: Int? = null,
    val serviceName: String? = null,
    val serviceOpenTime: String? = null,
    val serviceCloseTime: String? = null,
    val serviceTime: String? = null,
    val serviceAnnouncement: String? = null,
    val serviceMaxCustomer: Int? = null,
    val serviceStatus: Int? = null,
    val serviceDateCreated: String? = null,
    val serviceDateUpdated: String? = null
) : Parcelable
