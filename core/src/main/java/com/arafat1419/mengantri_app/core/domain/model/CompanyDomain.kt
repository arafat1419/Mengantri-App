package com.arafat1419.mengantri_app.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompanyDomain(
    val companyId: Int? = null,
    val customerId: Int? = null,
    val companyName: String? = null,
    val companyPhone: String? = null,
    val companyBanner: String? = null,
    val companyImage: String? = null,
    val categoryId: Int? = null,
    val companyAddress: String? = null,
    val companyProvince: String? = null,
    val companyCity: String? = null,
    val companyDistrics: String? = null,
    val companyStatus: Int? = null,
    val companyDateCreated: String? = null,
    val companyDateUpdated: String? = null,
    val companyOpenTime: String? = null,
    val companyCloseTime: String? = null,
    val companyExpiredTime: String? = null,
) : Parcelable
