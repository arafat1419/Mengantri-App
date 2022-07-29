package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class ServiceCountResponse(
    @field:SerializedName("service")
    val service: ServiceResponse? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("served")
    val served: Int? = null,

    @field:SerializedName("waiting")
    val waiting: Int? = null,

    @field:SerializedName("cancel")
    val cancel: Int? = null
)