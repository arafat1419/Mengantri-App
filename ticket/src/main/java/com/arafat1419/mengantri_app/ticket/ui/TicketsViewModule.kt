package com.arafat1419.mengantri_app.ticket.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.TicketWithServiceDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class TicketsViewModule(private val dataUseCase: DataUseCase): ViewModel() {
    fun getTicketByStatus(ticketStatus: String): LiveData<List<TicketWithServiceDomain>> =
        dataUseCase.getTicketByStatus(ticketStatus).asLiveData()
}