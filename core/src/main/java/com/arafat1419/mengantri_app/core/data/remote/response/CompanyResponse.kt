package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class CompanyResponse(

    @field:SerializedName("company_id")
    val companyId: Int? = null,

    @field:SerializedName("customer_id")
    val customerId: Int? = null,

    @field:SerializedName("company_name")
    val companyName: String? = null,

    @field:SerializedName("company_phone")
    val companyPhone: String? = null,

    @field:SerializedName("company_banner")
    val companyBanner: String? = null,

    @field:SerializedName("company_image")
    val companyImage: String? = null,

    @field:SerializedName("category_id")
    val categoryId: Int? = null,

    @field:SerializedName("company_address")
    val companyAddress: String? = null,

    @field:SerializedName("company_province")
    val companyProvince: String? = null,

    @field:SerializedName("company_city")
    val companyCity: String? = null,

    @field:SerializedName("company_districs")
    val companyDistrics: String? = null,

    @field:SerializedName("company_status")
    val companyStatus: Int? = null,

    @field:SerializedName("company_date_created")
    val companyDateCreated: String? = null,

    @field:SerializedName("company_date_updated")
    val companyDateUpdated: String? = null,

    @field:SerializedName("company_open_time")
    val companyOpenTime: String? = null,

    @field:SerializedName("company_close_time")
    val companyCloseTime: String? = null,

    @field:SerializedName("company_expired_time")
    val companyExpiredTime: String? = null,
)
