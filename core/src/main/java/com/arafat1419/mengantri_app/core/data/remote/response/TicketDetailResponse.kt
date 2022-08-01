package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class TicketDetailResponse(
    @field:SerializedName("ticket")
    val ticket: TicketServiceResponse? = null,

    @field:SerializedName("queue_number")
    val queueNumber: Int? = null,

    @field:SerializedName("estimated_time")
    val estimatedTime: String? = null,

    @field:SerializedName("is_process")
    val isProcess: Boolean? = null,
)
