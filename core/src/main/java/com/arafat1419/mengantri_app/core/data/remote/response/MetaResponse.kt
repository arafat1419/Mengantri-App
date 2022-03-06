package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class MetaResponse<T>(

    @field:SerializedName("meta")
    val meta: T
)