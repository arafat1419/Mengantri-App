package com.arafat1419.mengantri_app.ticket.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.TicketWithServiceDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class TicketsViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun getTicketByStatus(
        customerId: Int,
        ticketStatus: String
    ): LiveData<List<TicketWithServiceDomain>> =
        dataUseCase.getTicketByStatus(customerId, ticketStatus).asLiveData()
}