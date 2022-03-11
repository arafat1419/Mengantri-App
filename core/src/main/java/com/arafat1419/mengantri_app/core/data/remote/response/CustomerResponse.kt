package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class CustomerResponse(
    @field:SerializedName("customer_id")
    val customerId: Int? = null,

    @field:SerializedName("customer_name")
    val customerName: String? = null,

    @field:SerializedName("customer_email")
    val customerEmail: String? = null,

    @field:SerializedName("customer_password")
    val customerPassword: String? = null,

    @field:SerializedName("customer_phone")
    val customerPhone: String? = null,

    @field:SerializedName("customer_location")
    val customerLocation: String? = null,

    @field:SerializedName("customer_status")
    val customerStatus: Int? = null,

    @field:SerializedName("customer_date_created")
    val customerDateCreated: String? = null,

    @field:SerializedName("customer_date_updated")
    val customerDateUpdated: String? = null,

    @field:SerializedName("customer_code")
    val customerCode: String? = null
)
