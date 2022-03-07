package com.arafat1419.mengantri_app.home.ui.detail.detailservice

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.TicketDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class DetailServiceViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun getTickets(serviceId: Int): LiveData<List<TicketDomain>> =
        dataUseCase.getTickets(serviceId).asLiveData()

    fun postTicket(
        customerId: Int,
        serviceId: Int,
        ticketPersonName: String,
        ticketPersonPhone: String,
        ticketNotes: String,
        ticketServiceTime: String,
        ticketDate: String
    ): LiveData<TicketDomain> =
        dataUseCase.postTicket(
            customerId,
            serviceId,
            ticketPersonName,
            ticketPersonPhone,
            ticketNotes,
            ticketServiceTime,
            ticketDate
        ).asLiveData()
}