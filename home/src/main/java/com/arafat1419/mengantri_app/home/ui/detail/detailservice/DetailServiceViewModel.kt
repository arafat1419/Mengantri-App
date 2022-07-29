package com.arafat1419.mengantri_app.home.ui.detail.detailservice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class DetailServiceViewModel(private val dataUseCase: DataUseCase) : ViewModel() {

    fun getServiceCounted(serviceId: Int) = dataUseCase.getServiceCount(serviceId).asLiveData()

    fun getEstimatedTime(serviceId: Int) =
        dataUseCase.getServiceEstimated(serviceId).asLiveData()

    fun getCompany(companyId: Int) =
        dataUseCase.getCompany(companyId).asLiveData()

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