package com.arafat1419.mengantri_app.core.domain.model

data class TicketDetailDomain(
    val ticket: TicketServiceDomain? = null,
    val queueNumber: String? = null,
    val estimatedTime: String? = null,
    val isProcess: Boolean? = null,
)