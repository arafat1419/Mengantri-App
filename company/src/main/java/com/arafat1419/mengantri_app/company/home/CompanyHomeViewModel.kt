package com.arafat1419.mengantri_app.company.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.ServiceCountDomain
import com.arafat1419.mengantri_app.core.domain.model.TicketDetailDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class CompanyHomeViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun getServicesCount(companyId: Int): LiveData<List<ServiceCountDomain>> =
        dataUseCase.getServicesCountByCompany(companyId).asLiveData()

    fun getTicketsToday(serviceId: Int): LiveData<List<TicketDetailDomain>> =
        dataUseCase.getTicketsToday(serviceId).asLiveData()

    fun getTicketsSoon(serviceId: Int): LiveData<List<TicketDetailDomain>> =
        dataUseCase.getTicketsSoon(serviceId).asLiveData()

    fun getTicketsHistory(serviceId: Int): LiveData<List<TicketDetailDomain>> =
        dataUseCase.getTicketsHistory(null, serviceId).asLiveData()

    fun getEstimatedTime(serviceId: Int, ticketDate: String) =
        dataUseCase.getServiceEstimated(serviceId, ticketDate).asLiveData()

    fun postTicket(
        customerId: Int,
        serviceId: Int,
        ticketPersonName: String,
        ticketPersonPhone: String,
        ticketNotes: String,
        ticketDate: String,
        ticketEstimatedTime: String
    ) = dataUseCase.postTicket(
        customerId,
        serviceId,
        ticketPersonName,
        ticketPersonPhone,
        ticketNotes,
        ticketDate,
        ticketEstimatedTime
    ).asLiveData()
}