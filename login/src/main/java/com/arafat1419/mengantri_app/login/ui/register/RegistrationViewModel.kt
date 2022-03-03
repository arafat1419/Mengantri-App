package com.arafat1419.mengantri_app.login.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.CustomerDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class RegistrationViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun postRegistration(
        customerName: String,
        customerEmail: String,
        customerPassword: String,
        customerPhone: String
    ): LiveData<CustomerDomain> = dataUseCase.postRegistration(
        customerName,
        customerEmail,
        customerPassword,
        customerPhone
    ).asLiveData()
}