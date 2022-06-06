package com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse

import com.google.gson.annotations.SerializedName

data class DistricsResponse(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("id_kota")
    val idCity: String? = null,

    @field:SerializedName("nama")
    val districsName: String? = null
)
