package com.arafat1419.mengantri_app.home.ui.detail.detailticket

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.TicketDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class DetailTicketViewModel(private val dataUseCase: DataUseCase) : ViewModel() {

    fun updateTicket(ticketId: Int, status: String): LiveData<TicketDomain> =
        dataUseCase.updateTicket(ticketId, status).asLiveData()

    fun getTicket(ticketId: Int) = dataUseCase.getTicketServiceDetail(ticketId).asLiveData()

    fun getCompany(companyId: Int) = dataUseCase.getCompany(companyId).asLiveData()
}