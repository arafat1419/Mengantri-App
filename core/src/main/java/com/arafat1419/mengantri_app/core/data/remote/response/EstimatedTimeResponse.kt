package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class EstimatedTimeResponse(
    @field:SerializedName("estimated_time")
    val estimatedTime: String? = null
)
