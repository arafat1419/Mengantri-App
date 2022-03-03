package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class DataResponse<T>(
    @field:SerializedName("data")
    val result: List<T>? = null
)
