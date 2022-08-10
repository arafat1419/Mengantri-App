package com.arafat1419.mengantri_app.login.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class LoginViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun getLogin(customerEmail: String) = dataUseCase.getLogin(customerEmail).asLiveData()

    fun checkHash(value: String, hash: String) = dataUseCase.checkHash(value, hash).asLiveData()

    fun getCustomerCompany(customerId: Int) =
        dataUseCase.getCustomerCompany(customerId).asLiveData()

    fun subscribeTopic(customerMessageToken: String) =
        dataUseCase.subscribeTopic(customerMessageToken).asLiveData()
}