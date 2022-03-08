package com.arafat1419.mengantri_app.core.domain.usecase

import com.arafat1419.mengantri_app.core.data.remote.response.TicketResponse
import com.arafat1419.mengantri_app.core.domain.model.*
import kotlinx.coroutines.flow.Flow

interface DataUseCase {
    // -- LOGIN DOMAIN --
    fun getLogin(customerEmail: String): Flow<List<CustomerDomain>>
    fun postRegistration(
        customerName: String,
        customerEmail: String,
        customerPassword: String,
        customerPhone: String
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
        ticketServiceTime: String,
        ticketDate: String
        ): Flow<TicketDomain>

    fun getTicket(ticketId: Int): Flow<List<TicketWithServiceDomain>>
}