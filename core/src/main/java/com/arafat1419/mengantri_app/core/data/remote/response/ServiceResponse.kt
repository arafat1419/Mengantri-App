package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class ServiceResponse(

    @field:SerializedName("service_id")
    val serviceId: Int? = null,

    @field:SerializedName("company_id")
    val companyId: Int? = null,

    @field:SerializedName("service_name")
    val serviceName: String? = null,

    @field:SerializedName("service_open_time")
    val serviceOpenTime: String? = null,

    @field:SerializedName("service_close_time")
    val serviceCloseTime: String? = null,

    @field:SerializedName("service_time")
    val serviceTime: String? = null,

    @field:SerializedName("service_announcement")
    val serviceAnnouncement: String? = null,

    @field:SerializedName("service_max_customer")
    val serviceMaxCustomer: Int? = null,

    @field:SerializedName("service_status")
    val serviceStatus: Int? = null,

    @field:SerializedName("service_date_created")
    val serviceDateCreated: String? = null,

    @field:SerializedName("service_date_updated")
    val serviceDateUpdated: String? = null

)
