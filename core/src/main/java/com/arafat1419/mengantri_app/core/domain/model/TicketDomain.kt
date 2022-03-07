package com.arafat1419.mengantri_app.core.domain.model

import com.google.gson.annotations.SerializedName

data class TicketDomain(
    val ticketId: Int? = null,
    val customerId: Int? = null,
    val serviceId: Int? = null,
    val ticketPersonName: String? = null,
    val ticketPersonPhone: String? = null,
    val ticketNotes: String? = null,
    val ticketDate: String? = null,
    val ticketStatus: String? = null,
    val ticketServiceTime: String? = null,
    val ticketServiceFinish: String? = null,
    val ticketDateCreated: String? = null
)
