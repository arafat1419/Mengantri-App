package com.arafat1419.mengantri_app.company.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.CityDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.DistricsDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.ProvinceDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase

class CompanyProfileViewModel(private val dataUseCase: DataUseCase): ViewModel() {
    fun getProvinces(): LiveData<List<ProvinceDomain>> =
        dataUseCase.getProvinces().asLiveData()

    fun getCities(idProvince: String): LiveData<List<CityDomain>> =
        dataUseCase.getCities(idProvince).asLiveData()

    fun getDistrics(idCity: String): LiveData<List<DistricsDomain>> =
        dataUseCase.getDistrics(idCity).asLiveData()
}