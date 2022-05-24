package com.arafat1419.mengantri_app.core.domain.model

data class TicketWithServiceDomain(
    val ticketId: Int? = null,
    val customerId: Int? = null,
    val serviceId: ServiceDomain? = null,
    val ticketPersonName: String? = null,
    val ticketPersonPhone: String? = null,
    val ticketNotes: String? = null,
    val ticketDate: String? = null,
    val ticketStatus: String? = null,
    val ticketQrImage: String? = null,
    val ticketServiceStart: String? = null,
    val ticketServiceFinish: String? = null,
    val ticketDateCreated: String? = null
)
