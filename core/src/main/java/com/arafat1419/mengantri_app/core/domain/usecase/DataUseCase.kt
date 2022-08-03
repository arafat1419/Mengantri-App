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

    // -- FILES --
    fun postUploadFile(fileName: String, isBanner: Boolean, file: File): Flow<UploadFileDomain>

    // -- LOGIN DOMAIN --
    fun getLogin(customerEmail: String): Flow<List<CustomerDomain>>
    fun checkHash(value: String, hash: String): Flow<Boolean>
    fun postRegistration(customerEmail: String): Flow<CustomerDomain>
    fun updateCustomerCode(customerId: Int, customerCode: String): Flow<CustomerDomain>
    fun updateBiodata(
        customerId: Int,
        customerName: String,
        customerPassword: String,
        customerPhone: String,
        customerLocation: String
    ): Flow<CustomerDomain>

    // -- HOME DOMAIN --
    fun getSearchCompaniesByCategory(keyword: String, categoryId: Int): Flow<List<CompanyDomain>>
    fun getServices(companyId: Int): Flow<List<ServiceDomain>>
    fun getTickets(serviceId: Int, ticketDate: String? = null): Flow<List<TicketDomain>>

    fun getTicketServed(serviceId: Int): Flow<Int>
    fun getServicesAndServed(companyId: Int): Flow<List<ServiceCountDomain>>


    fun getTicket(ticketId: Int): Flow<List<TicketServiceDomain>>

    fun updateTicket(ticketId: Int, status: String): Flow<TicketDomain>

    fun getServiceXDay(serviceId: Int, dayId: Int): Flow<List<ServiceXDayDomain>>

    // -- TICKET DOMAIN --
    fun getTicketByStatus(
        customerId: Int,
        ticketStatus: String
    ): Flow<List<TicketServiceDomain>>

    // -- PROFILE DOMAIN --
    fun updateProfile(
        customerId: Int,
        customerName: String,
        customerPhone: String
    ): Flow<CustomerDomain>

    fun updatePassword(
        customerId: Int,
        customerPassword: String
    ): Flow<CustomerDomain>

    // -- COMPANY DOMAIN --
    fun getUserCompany(customerId: Int): Flow<List<CompanyDomain>>

    fun getTicketsByService(serviceId: Int): Flow<List<TicketDomain>>

    // -- PROVINCE, CITY, DISTRICS --
    fun getProvinces(): Flow<List<ProvinceDomain>>
    fun getCities(idProvince: String): Flow<List<CityDomain>>
    fun getDistrics(idCity: String): Flow<List<DistricsDomain>>
}