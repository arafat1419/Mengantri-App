package com.arafat1419.mengantri_app.core.domain.usecase

import com.arafat1419.mengantri_app.core.domain.model.*
import kotlinx.coroutines.flow.Flow

interface DataUseCase {
    // -- LOGIN DOMAIN --
    fun getLogin(customerEmail: String, customerStatus: Int): Flow<List<CustomerDomain>>
    fun postRegistration(customerEmail: String): Flow<CustomerDomain>
    fun updateCustomerId(customerId: Int, customerCode: String): Flow<CustomerDomain>

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
        ticketServiceTime: String,
        ticketDate: String
    ): Flow<TicketDomain>

    fun getTicket(ticketId: Int): Flow<List<TicketWithServiceDomain>>

    fun updateTicket(ticketId: Int, status: String): Flow<TicketDomain>

    fun getTicketByStatus(
        customerId: Int,
        ticketStatus: String
    ): Flow<List<TicketWithServiceDomain>>

}