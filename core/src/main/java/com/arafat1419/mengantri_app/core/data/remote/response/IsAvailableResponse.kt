package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class IsAvailableResponse(
    @field:SerializedName("is_available")
    val isAvailable: Boolean? = null
)