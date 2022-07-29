package com.arafat1419.mengantri_app.core.domain.repository

import com.arafat1419.mengantri_app.core.data.remote.response.*
import com.arafat1419.mengantri_app.core.domain.model.*
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.CityDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.DistricsDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.ProvinceDomain
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IDataRepository {
    // -- CATEGORY --
    fun getCategories(): Flow<List<CategoryDomain>>

    // -- COMPANY --
    fun getNewestCompanies(): Flow<List<CompanyDomain>>
    fun getCustomerCompany(customerId: Int): Flow<List<CompanyDomain>>
    fun getCompaniesByCategory(categoryId: Int): Flow<List<CompanyDomain>>
    fun getCompany(companyId: Int): Flow<CompanyDomain>
    fun getSearchCompanies(keyword: String): Flow<List<CompanyDomain>>

    // -- SERVICE --
    fun getServicesCountByCompany(companyId: Int): Flow<List<ServiceCountDomain>>
    fun getServiceCount(serviceId: Int): Flow<ServiceCountDomain>
    fun getServiceEstimated(serviceId: Int): Flow<String?>
    fun getSearchServices(keyword: String): Flow<List<ServiceCountDomain>>

    // -- TICKET --
    fun getTicketServiceDetail(ticketId: Int): Flow<TicketDetailDomain>
    fun getTicketsWaiting(customerId: Int): Flow<List<TicketDetailDomain>>
    fun getTicketsHistory(customerId: Int): Flow<List<TicketDetailDomain>>

    // -- LOGIN DOMAIN --
    fun getLogin(customerEmail: String): Flow<List<CustomerDomain>>
    fun checkHash(value: String, hash: String): Flow<Boolean>
    fun postRegistration(customerResponse: CustomerResponse): Flow<CustomerDomain>
    fun patchCustomer(customerId: Int, customerResponse: CustomerResponse): Flow<CustomerDomain>

    // -- HOME DOMAIN --
    fun getSearchCompaniesByCategory(keyword: String, categoryId: Int): Flow<List<CompanyDomain>>
    fun getServices(companyId: Int): Flow<List<ServiceDomain>>
    fun getTickets(serviceId: Int, ticketDate: String?): Flow<List<TicketDomain>>

    fun getTicketServed(serviceId: Int): Flow<Int>
    fun getServicesAndServed(companyId: Int): Flow<List<ServiceCountDomain>>

    fun postTicket(ticketResponse: TicketResponse): Flow<TicketDomain>

    fun getTicket(ticketId: Int): Flow<List<TicketServiceDomain>>

    fun updateTicket(ticketId: Int, ticketResponse: TicketResponse): Flow<TicketDomain>

    fun getServiceXDay(serviceId: Int, dayId: Int): Flow<List<ServiceXDayDomain>>

    // -- TICKET DOMAIN --
    fun getTicketByStatus(
        customerId: Int,
        ticketStatus: String
    ): Flow<List<TicketServiceDomain>>

    // -- COMPANY DOMAIN --
    fun getUserCompany(customerId: Int): Flow<List<CompanyDomain>>
    fun postUploadFile(fileName: String, isBanner: Boolean, file: File): Flow<UploadFileDomain>
    fun postCompany(companyDomain: CompanyDomain): Flow<CompanyDomain>

    fun getTicketsSoon(serviceId: Int): Flow<List<TicketDomain>>
    fun getTicketsByService(serviceId: Int): Flow<List<TicketDomain>>

    fun postService(serviceOnlyDomain: ServiceOnlyDomain): Flow<ServiceOnlyDomain>
    fun updateService(
        serviceId: Int,
        serviceOnlyResponse: ServiceOnlyResponse
    ): Flow<ServiceOnlyDomain>

    // -- PROVINCE, CITY, DISTRICS --
    fun getProvinces(): Flow<List<ProvinceDomain>>
    fun getCities(idProvince: String): Flow<List<CityDomain>>
    fun getDistrics(idCity: String): Flow<List<DistricsDomain>>
}