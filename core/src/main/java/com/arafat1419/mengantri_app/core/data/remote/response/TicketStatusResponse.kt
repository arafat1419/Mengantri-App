package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class TicketStatusResponse(
    @field:SerializedName("ticket_status")
    val ticketStatus: String? = null
)
