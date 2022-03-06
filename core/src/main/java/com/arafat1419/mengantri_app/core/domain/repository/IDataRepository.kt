package com.arafat1419.mengantri_app.core.domain.repository

import com.arafat1419.mengantri_app.core.data.remote.response.CustomerResponse
import com.arafat1419.mengantri_app.core.domain.model.*
import kotlinx.coroutines.flow.Flow

interface IDataRepository {
    // -- LOGIN DOMAIN --
    fun getLogin(customerEmail: String): Flow<List<CustomerDomain>>
    fun postRegistration(customerResponse: CustomerResponse): Flow<CustomerDomain>

    // -- HOME DOMAIN --
    fun getCategories(): Flow<List<CategoryDomain>>
    fun getCompanies(categoryId: Int): Flow<List<CompanyDomain>>
    fun getServices(companyId: Int): Flow<List<ServiceDomain>>
    fun getTicketServed(serviceId: Int): Flow<Int>
    fun getServicesAndServed(companyId: Int): Flow<List<ServiceCountDomain>>
}