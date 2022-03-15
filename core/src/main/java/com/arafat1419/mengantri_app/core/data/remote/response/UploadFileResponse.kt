package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class UploadFileResponse(
    @field:SerializedName("service_id")
    val fileId: Int? = null
)
