package com.arafat1419.mengantri_app.home.ui.detail.detailticket

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.ServiceCountDomain
import com.arafat1419.mengantri_app.core.domain.model.TicketWithServiceDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class DetailTicketViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun getTicket(ticketId: Int): LiveData<List<TicketWithServiceDomain>> =
        dataUseCase.getTicket(ticketId).asLiveData()

    fun getServiceAndServed(companyId: Int): LiveData<List<ServiceCountDomain>> =
        dataUseCase.getServicesAndServed(companyId).asLiveData()
}