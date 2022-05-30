package com.arafat1419.mengantri_app.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TicketDomain(
    val ticketId: Int? = null,
    val customerId: Int? = null,
    val serviceId: Int? = null,
    val ticketPersonName: String? = null,
    val ticketPersonPhone: String? = null,
    val ticketNotes: String? = null,
    val ticketDate: String? = null,
    val ticketStatus: String? = null,
    val ticketQrImage: String? = null,
    val ticketServiceStart: String? = null,
    val ticketServiceFinish: String? = null,
    val ticketDateCreated: String? = null
): Parcelable