package com.arafat1419.mengantri_app.ticket.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class TicketsViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun getTicketByStatus(customerId: Int) = dataUseCase.getTicketsWaiting(customerId).asLiveData()
    fun getTicketByHistory(customerId: Int) = dataUseCase.getTicketsHistory(customerId).asLiveData()
}