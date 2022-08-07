package com.arafat1419.mengantri_app.profile.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.CustomerDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class ProfileViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun updateProfile(
        customerId: Int,
        customerName: String,
        customerPhone: String
    ) =
        dataUseCase.updateProfile(customerId, customerName, customerPhone).asLiveData()

    fun updatePassword(customerId: Int, customerPassword: String) =
        dataUseCase.updatePassword(customerId, customerPassword).asLiveData()

    fun checkHash(value: String, hash: String) = dataUseCase.checkHash(value, hash).asLiveData()
}