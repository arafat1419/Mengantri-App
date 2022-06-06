package com.arafat1419.mengantri_app.home.ui.detail.detailticket

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.TicketDomain
import com.arafat1419.mengantri_app.core.domain.model.TicketWithServiceDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class DetailTicketViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun getTicket(ticketId: Int): LiveData<List<TicketWithServiceDomain>> =
        dataUseCase.getTicket(ticketId).asLiveData()

    fun updateTicket(ticketId: Int, status: String): LiveData<TicketDomain> =
        dataUseCase.updateTicket(ticketId, status).asLiveData()

    fun getTickets(serviceId: Int): LiveData<List<TicketDomain>> =
        dataUseCase.getTickets(serviceId).asLiveData()
}