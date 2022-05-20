package com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse

import com.google.gson.annotations.SerializedName

data class ListCity(
    @field:SerializedName("kota_kabupaten")
    val result: List<CityResponse>
)
