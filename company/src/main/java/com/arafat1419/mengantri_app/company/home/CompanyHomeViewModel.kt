package com.arafat1419.mengantri_app.company.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.ServiceCountDomain
import com.arafat1419.mengantri_app.core.domain.model.TicketDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class CompanyHomeViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun getServiceAndServed(companyId: Int): LiveData<List<ServiceCountDomain>> =
        dataUseCase.getServicesAndServed(companyId).asLiveData()

    fun getTickets(serviceId: Int): LiveData<List<TicketDomain>> =
        dataUseCase.getTickets(serviceId).asLiveData()

    fun getTicketsSoon(serviceId: Int): LiveData<List<TicketDomain>> =
        dataUseCase.getTicketsSoon(serviceId).asLiveData()

    fun getTicketsByService(serviceId: Int): LiveData<List<TicketDomain>> =
        dataUseCase.getTicketsByService(serviceId).asLiveData()
}