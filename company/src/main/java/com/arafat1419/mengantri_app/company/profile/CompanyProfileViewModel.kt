package com.arafat1419.mengantri_app.company.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.CategoryDomain
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain
import com.arafat1419.mengantri_app.core.domain.model.UploadFileDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.CityDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.DistricsDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.ProvinceDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.io.File

@FlowPreview
@ExperimentalCoroutinesApi
class CompanyProfileViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    fun getProvinces(): LiveData<List<ProvinceDomain>> =
        dataUseCase.getProvinces().asLiveData()

    fun getCities(idProvince: String): LiveData<List<CityDomain>> =
        dataUseCase.getCities(idProvince).asLiveData()

    fun getDistrics(idCity: String): LiveData<List<DistricsDomain>> =
        dataUseCase.getDistrics(idCity).asLiveData()

    fun getCategories(): LiveData<List<CategoryDomain>> =
        dataUseCase.getCategories().asLiveData()

    fun getCompany(companyId: Int): LiveData<CompanyDomain> =
        dataUseCase.getCompany(companyId).asLiveData()

    fun postUploadFile(
        fileName: String,
        isBanner: Boolean,
        file: File
    ): LiveData<UploadFileDomain> =
        dataUseCase.postUploadFile(fileName, isBanner, file).asLiveData()

    fun postCompany(companyDomain: CompanyDomain): LiveData<CompanyDomain> =
        dataUseCase.postCompany(companyDomain).asLiveData()

    fun updateCompany(companyId: Int, companyDomain: CompanyDomain): LiveData<CompanyDomain> =
        dataUseCase.updateCompany(companyId, companyDomain).asLiveData()
}