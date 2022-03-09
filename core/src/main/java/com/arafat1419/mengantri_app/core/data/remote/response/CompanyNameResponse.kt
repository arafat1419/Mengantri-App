package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class CompanyNameResponse(
    @field:SerializedName("company_id")
    val companyId: Int? = null,

    @field:SerializedName("company_name")
    val companyName: String? = null,
)
