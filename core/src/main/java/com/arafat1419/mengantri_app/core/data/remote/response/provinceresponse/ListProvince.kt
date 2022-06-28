package com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse

import com.google.gson.annotations.SerializedName

data class ListProvince(
    @field:SerializedName("provinsi")
    val result: List<ProvinceResponse>
)
