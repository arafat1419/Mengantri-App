package com.arafat1419.mengantri_app.company.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class RegisterStatusViewModel(private val dataUseCase: DataUseCase): ViewModel() {
    fun getUserCompany(customerId: Int): LiveData<List<CompanyDomain>> =
        dataUseCase.getUserCompany(customerId).asLiveData()
}