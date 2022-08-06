package com.arafat1419.mengantri_app.core.domain.usecase

import com.arafat1419.mengantri_app.core.domain.model.*
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.CityDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.DistricsDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.ProvinceDomain
import com.arafat1419.mengantri_app.core.vo.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface DataUseCase {
    // -- CATEGORY --
    fun getCategories(): Flow<Resource<List<CategoryDomain>>>

    // -- COMPANY --
    fun getNewestCompanies(): Flow<Resource<List<CompanyDomain>>>
    fun getCustomerCompany(customerId: Int): Flow<Resource<List<CompanyDomain>>>
    fun getCompaniesByCategory(categoryId: Int): Flow<Resource<List<CompanyDomain>>>
    fun getCompany(companyId: Int): Flow<Resource<CompanyDomain>>
    fun getSearchCompanies(keyword: String): Flow<Resource<List<CompanyDomain>>>
    fun postCompany(companyDomain: CompanyDomain): Flow<Resource<CompanyDomain>>
    fun updateCompany(companyId: Int, companyDomain: CompanyDomain): Flow<Resource<CompanyDomain>>

    // -- SERVICE --
    fun getServicesCountByCompany(companyId: Int): Flow<Resource<List<ServiceCountDomain>>>
    fun getServiceCount(serviceId: Int, ticketDate: String?): Flow<Resource<ServiceCountDomain>>
    fun getServiceEstimated(serviceId: Int, ticketDate: String): Flow<Resource<EstimatedTimeDomain>>
    fun getSearchServices(keyword: String): Flow<Resource<List<ServiceCountDomain>>>
    fun getServicesByCompany(companyId: Int): Flow<Resource<List<ServiceDomain>>>
    fun getService(serviceId: Int): Flow<Resource<ServiceDomain>>
    fun postService(serviceDomain: ServiceDomain): Flow<Resource<ServiceDomain>>
    fun updateService(serviceId: Int, serviceDomain: ServiceDomain): Flow<Resource<ServiceDomain>>
    fun deleteService(serviceId: Int): Flow<Resource<Boolean>>

    // -- TICKET --
    fun getTicketServiceDetail(ticketId: Int): Flow<Resource<TicketDetailDomain>>
    fun getTicketsWaiting(customerId: Int): Flow<Resource<List<TicketDetailDomain>>>
    fun getTicketsHistory(customerId: Int?, serviceId: Int?): Flow<Resource<List<TicketDetailDomain>>>
    fun getTicketsToday(serviceId: Int?): Flow<Resource<List<TicketDetailDomain>>>
    fun getTicketsSoon(serviceId: Int?): Flow<Resource<List<TicketDetailDomain>>>
    fun postTicket(
        customerId: Int,
        serviceId: Int,
        ticketPersonName: String,
        ticketPersonPhone: String,
        ticketNotes: String,
        ticketDate: String,
        ticketEstimatedTime: String
    ): Flow<Resource<TicketDomain>>

    fun updateTicket(ticketId: Int, status: String): Flow<Resource<TicketDomain>>

    // -- CUSTOMER --
    fun getCustomer(customerId: Int): Flow<Resource<CustomerDomain>>
    fun updateProfile(
        customerId: Int,
        customerName: String,
        customerPhone: String
    ): Flow<Resource<CustomerDomain>>

    fun updatePassword(
        customerId: Int,
        customerPassword: String
    ): Flow<Resource<CustomerDomain>>

    fun getLogin(customerEmail: String): Flow<Resource<List<CustomerDomain>>>
    fun postRegistration(customerEmail: String): Flow<Resource<CustomerDomain>>
    fun updateCustomerCode(customerId: Int, customerCode: String): Flow<Resource<CustomerDomain>>
    fun updateBiodata(
        customerId: Int,
        customerName: String,
        customerPassword: String,
        customerPhone: String,
        customerLocation: String
    ): Flow<Resource<CustomerDomain>>

    // -- FILES --
    fun postUploadFile(fileName: String, isBanner: Boolean, file: File): Flow<Resource<UploadFileDomain>>

    // -- UTILS --
    fun checkHash(value: String, hash: String): Flow<Resource<Boolean>>


    // -- PROVINCE, CITY, DISTRICS --
    fun getProvinces(): Flow<Resource<List<ProvinceDomain>>>
    fun getCities(idProvince: String): Flow<Resource<List<CityDomain>>>
    fun getDistrics(idCity: String): Flow<Resource<List<DistricsDomain>>>
}