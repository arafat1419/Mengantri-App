package com.arafat1419.mengantri_app.login.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class RegistrationViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun postRegistration(customerEmail: String) =
        dataUseCase.postRegistration(customerEmail).asLiveData()

    fun updateCustomerCode(customerId: Int, customerCode: String) =
        dataUseCase.updateCustomerCode(customerId, customerCode).asLiveData()

    fun updateBiodata(
        customerId: Int,
        customerName: String,
        customerPassword: String,
        customerPhone: String,
        customerLocation: String
    ) = dataUseCase.updateBiodata(
        customerId,
        customerName,
        customerPassword,
        customerPhone,
        customerLocation
    ).asLiveData()

    fun getCustomer(customerId: Int) = dataUseCase.getCustomer(customerId).asLiveData()

    fun getLogin(customerEmail: String) = dataUseCase.getLogin(customerEmail).asLiveData()
}