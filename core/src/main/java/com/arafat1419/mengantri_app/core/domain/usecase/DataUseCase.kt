package com.arafat1419.mengantri_app.core.domain.usecase

import com.arafat1419.mengantri_app.core.domain.model.*
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.CityDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.DistricsDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.ProvinceDomain
import kotlinx.coroutines.flow.Flow
import java.io.File

interface DataUseCase {
    // -- CATEGORY --
    fun getCategories(): Flow<List<CategoryDomain>>

    // -- COMPANY --
    fun getNewestCompanies(): Flow<List<CompanyDomain>>
    fun getCustomerCompany(customerId: Int): Flow<List<CompanyDomain>>
    fun getCompaniesByCategory(categoryId: Int): Flow<List<CompanyDomain>>
    fun getCompany(companyId: Int): Flow<CompanyDomain>
    fun getSearchCompanies(keyword: String): Flow<List<CompanyDomain>>
    fun postCompany(companyDomain: CompanyDomain): Flow<CompanyDomain>
    fun updateCompany(companyId: Int, companyDomain: CompanyDomain): Flow<CompanyDomain>

    // -- SERVICE --
    fun getServicesCountByCompany(companyId: Int): Flow<List<ServiceCountDomain>>
    fun getServiceCount(serviceId: Int, ticketDate: String?): Flow<ServiceCountDomain>
    fun getServiceEstimated(serviceId: Int, ticketDate: String): Flow<EstimatedTimeDomain?>
    fun getSearchServices(keyword: String): Flow<List<ServiceCountDomain>>
    fun getServicesByCompany(companyId: Int): Flow<List<ServiceDomain>>
    fun getService(serviceId: Int): Flow<ServiceDomain>
    fun postService(serviceDomain: ServiceDomain): Flow<ServiceDomain>
    fun updateService(serviceId: Int, serviceDomain: ServiceDomain): Flow<ServiceDomain>
    fun deleteService(serviceId: Int): Flow<Boolean>

    // -- TICKET --
    fun getTicketServiceDetail(ticketId: Int): Flow<TicketDetailDomain>
    fun getTicketsWaiting(customerId: Int): Flow<List<TicketDetailDomain>>
    fun getTicketsHistory(customerId: Int?, serviceId: Int?): Flow<List<TicketDetailDomain>>
    fun getTicketsToday(serviceId: Int?): Flow<List<TicketDetailDomain>>
    fun getTicketsSoon(serviceId: Int?): Flow<List<TicketDetailDomain>>
    fun postTicket(
        customerId: Int,
        serviceId: Int,
        ticketPersonName: String,
        ticketPersonPhone: String,
        ticketNotes: String,
        ticketDate: String,
        ticketEstimatedTime: String
    ): Flow<TicketDomain>

    fun updateTicket(ticketId: Int, status: String): Flow<TicketDomain>

    // -- CUSTOMER --
    fun getCustomer(customerId: Int): Flow<CustomerDomain>
    fun updateProfile(
        customerId: Int,
        customerName: String,
        customerPhone: String
    ): Flow<CustomerDomain>

    fun updatePassword(
        customerId: Int,
        customerPassword: String
    ): Flow<CustomerDomain>

    fun getLogin(customerEmail: String): Flow<List<CustomerDomain>>
    fun postRegistration(customerEmail: String): Flow<CustomerDomain>
    fun updateCustomerCode(customerId: Int, customerCode: String): Flow<CustomerDomain>
    fun updateBiodata(
        customerId: Int,
        customerName: String,
        customerPassword: String,
        customerPhone: String,
        customerLocation: String
    ): Flow<CustomerDomain>

    // -- FILES --
    fun postUploadFile(fileName: String, isBanner: Boolean, file: File): Flow<UploadFileDomain>

    // -- UTILS --
    fun checkHash(value: String, hash: String): Flow<Boolean>


    // -- PROVINCE, CITY, DISTRICS --
    fun getProvinces(): Flow<List<ProvinceDomain>>
    fun getCities(idProvince: String): Flow<List<CityDomain>>
    fun getDistrics(idCity: String): Flow<List<DistricsDomain>>
}