package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class ServiceXDayResponse(
    @field:SerializedName("sxd_id")
    val sxdId: Int? = null,

    @field:SerializedName("service_id")
    val serviceId: Int? = null,

    @field:SerializedName("day_id")
    val dayId: Int? = null
)
