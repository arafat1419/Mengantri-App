package com.arafat1419.mengantri_app.core.domain.model

import com.google.gson.annotations.SerializedName

data class CompanyDomain(
    val companyId: Int? = null,
    val customerId: Int? = null,
    val companyName: String? = null,
    val companyPhone: String? = null,
    val companyBanner: String? = null,
    val companyImage: String? = null,
    val categoryId: Int? = null,
    val companyAddress: String? = null,
    val companyCity: String? = null,
    val companyDistrics: String? = null,
    val companyStatus: Int? = null,
    val companyDateCreated: String? = null,
    val companyDateUpdated: Any? = null,
    val companyOpenTime: String? = null,
    val companyCloseTime: String? = null,
    val companyExpiredTime: String? = null,
)
