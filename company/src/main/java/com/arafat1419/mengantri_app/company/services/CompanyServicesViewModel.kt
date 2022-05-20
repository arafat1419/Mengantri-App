package com.arafat1419.mengantri_app.company.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.ServiceDomain
import com.arafat1419.mengantri_app.core.domain.model.ServiceOnlyDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class CompanyServicesViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun getServices(companyId: Int): LiveData<List<ServiceDomain>> =
        dataUseCase.getServices(companyId).asLiveData()

    fun postService(serviceOnlyDomain: ServiceOnlyDomain): LiveData<ServiceOnlyDomain> =
        dataUseCase.postService(serviceOnlyDomain).asLiveData()

    fun updateService(
        serviceId: Int,
        serviceName: String,
        serviceOpenTime: String,
        serviceCloseTime: String,
        serviceAnnouncement: String,
        serviceMaxCustomer: Int,
        serviceStatus: Int,
        serviceDay: List<String>
    ): LiveData<ServiceOnlyDomain> =
        dataUseCase.updateService(
            serviceId,
            serviceName,
            serviceOpenTime,
            serviceCloseTime,
            serviceAnnouncement,
            serviceMaxCustomer,
            serviceStatus,
            serviceDay
        ).asLiveData()
}
