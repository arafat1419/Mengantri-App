package com.arafat1419.mengantri_app.company.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.ServiceCountDomain
import com.arafat1419.mengantri_app.core.domain.model.ServiceDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class CompanyHomeViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun getServiceAndServed(companyId: Int): LiveData<List<ServiceCountDomain>> =
        dataUseCase.getServicesAndServed(companyId).asLiveData()
}