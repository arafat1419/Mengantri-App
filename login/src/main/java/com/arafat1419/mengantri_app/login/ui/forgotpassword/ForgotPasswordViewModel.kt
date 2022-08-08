package com.arafat1419.mengantri_app.login.ui.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class ForgotPasswordViewModel(private val dataUseCase: DataUseCase): ViewModel() {
    fun updateCustomerCode(customerId: Int, customerCode: String) =
        dataUseCase.updateCustomerCode(customerId, customerCode).asLiveData()

    fun updatePassword(customerId: Int, customerPassword: String) =
        dataUseCase.updatePassword(customerId, customerPassword).asLiveData()
}