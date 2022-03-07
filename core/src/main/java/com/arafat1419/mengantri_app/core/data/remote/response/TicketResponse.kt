package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class TicketResponse(

    @field:SerializedName("ticket_id")
    val ticketId: Int? = null,

    @field:SerializedName("customer_id")
    val customerId: Int? = null,

    @field:SerializedName("service_id")
    val serviceId: Int? = null,

    @field:SerializedName("ticket_person_name")
    val ticketPersonName: String? = null,

    @field:SerializedName("ticket_person_phone")
    val ticketPersonPhone: String? = null,

    @field:SerializedName("ticket_notes")
    val ticketNotes: String? = null,

    @field:SerializedName("ticket_date")
    val ticketDate: String? = null,

    @field:SerializedName("ticket_status")
    val ticketStatus: String? = null,

    @field:SerializedName("ticket_service_time")
    val ticketServiceTime: String? = null,

    @field:SerializedName("ticket_date_created")
    val ticketDateCreated: String? = null

)