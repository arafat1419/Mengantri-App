package com.arafat1419.mengantri_app.home.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun getCustomerCompany(customerId: Int) =
        dataUseCase.getCustomerCompany(customerId).asLiveData()

    fun getCategories() = dataUseCase.getCategories().asLiveData()

    fun getNewestComapanies() = dataUseCase.getNewestCompanies().asLiveData()
}