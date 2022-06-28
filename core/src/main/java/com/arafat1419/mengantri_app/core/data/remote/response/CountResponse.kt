package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName


data class CountResponse(

    @field:SerializedName("filter_count")
    val filterCount: Int? = null
)
