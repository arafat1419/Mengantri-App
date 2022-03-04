package com.arafat1419.mengantri_app.core.domain.repository

import com.arafat1419.mengantri_app.core.data.remote.response.CustomerResponse
import com.arafat1419.mengantri_app.core.domain.model.CategoryDomain
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain
import com.arafat1419.mengantri_app.core.domain.model.CustomerDomain
import com.arafat1419.mengantri_app.core.domain.model.ServiceDomain
import kotlinx.coroutines.flow.Flow

interface IDataRepository {
    // -- LOGIN DOMAIN --
    fun getLogin(customerEmail: String): Flow<List<CustomerDomain>>
    fun postRegistration(customerResponse: CustomerResponse): Flow<CustomerDomain>

    // -- HOME DOMAIN --
    fun getCategories(): Flow<List<CategoryDomain>>
    fun getCompanies(categoryId: Int): Flow<List<CompanyDomain>>
    fun getServices(companyId: Int): Flow<List<ServiceDomain>>
}