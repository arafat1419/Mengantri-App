package com.arafat1419.mengantri_app.core.domain.usecase

import com.arafat1419.mengantri_app.core.domain.model.*
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.CityDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.DistricsDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.ProvinceDomain
import kotlinx.coroutines.flow.Flow
import java.io.File

interface DataUseCase {
    // -- LOGIN DOMAIN --
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

    // -- HOME DOMAIN --
    fun getCategories(): Flow<List<CategoryDomain>>
    fun getCompanies(categoryId: Int): Flow<List<CompanyDomain>>
    fun getServices(companyId: Int): Flow<List<ServiceDomain>>
    fun getTickets(serviceId: Int): Flow<List<TicketDomain>>

    fun getTicketServed(serviceId: Int): Flow<Int>
    fun getServicesAndServed(companyId: Int): Flow<List<ServiceCountDomain>>

    fun postTicket(
        customerId: Int,
        serviceId: Int,
        ticketPersonName: String,
        ticketPersonPhone: String,
        ticketNotes: String,
        ticketDate: String
    ): Flow<TicketDomain>

    fun getTicket(ticketId: Int): Flow<List<TicketWithServiceDomain>>

    fun updateTicket(ticketId: Int, status: String): Flow<TicketDomain>

    fun getServiceXDay(serviceId: Int, dayId: Int): Flow<List<ServiceXDayDomain>>

    // -- TICKET DOMAIN --
    fun getTicketByStatus(
        customerId: Int,
        ticketStatus: String
    ): Flow<List<TicketWithServiceDomain>>

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
    fun postUploadFile(fileName: String, isBanner: Boolean, file: File): Flow<UploadFileDomain>
    fun postCompany(
        companyDomain: CompanyDomain
    ): Flow<CompanyDomain>

    // -- PROVINCE, CITY, DISTRICS --
    fun getProvinces(): Flow<List<ProvinceDomain>>
    fun getCities(idProvince: String): Flow<List<CityDomain>>
    fun getDistrics(idCity: String): Flow<List<DistricsDomain>>
}