package com.arafat1419.mengantri_app.home.ui.companies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class CompaniesViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun getCompanies(categoryId: Int): LiveData<List<CompanyDomain>> =
        dataUseCase.getCompanies(categoryId).asLiveData()
}