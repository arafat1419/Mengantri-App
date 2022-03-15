package com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse

import com.google.gson.annotations.SerializedName

data class ListDistrics(
    @field:SerializedName("kecamatan")
    val result: List<DistricsResponse>
)
