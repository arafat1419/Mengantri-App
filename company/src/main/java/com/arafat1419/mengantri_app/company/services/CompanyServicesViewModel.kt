package com.arafat1419.mengantri_app.company.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.ServiceDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class CompanyServicesViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun getService(serviceId: Int) =
        dataUseCase.getService(serviceId).asLiveData()

    fun getServices(companyId: Int) =
        dataUseCase.getServicesByCompany(companyId).asLiveData()

    fun postService(serviceDomain: ServiceDomain) =
        dataUseCase.postService(serviceDomain).asLiveData()

    fun updateService(serviceId: Int, serviceDomain: ServiceDomain) =
        dataUseCase.updateService(serviceId, serviceDomain).asLiveData()

    fun deleteService(serviceId: Int) =
        dataUseCase.deleteService(serviceId).asLiveData()
}
