package com.arafat1419.mengantri_app.home.ui.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.ServiceDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class ServicesViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun getService(companyId: Int): LiveData<List<ServiceDomain>> =
        dataUseCase.getServices(companyId).asLiveData()
}