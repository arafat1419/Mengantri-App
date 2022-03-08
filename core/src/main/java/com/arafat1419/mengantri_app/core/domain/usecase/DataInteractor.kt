package com.arafat1419.mengantri_app.core.domain.usecase

import com.arafat1419.mengantri_app.core.data.remote.response.CustomerResponse
import com.arafat1419.mengantri_app.core.data.remote.response.TicketResponse
import com.arafat1419.mengantri_app.core.data.remote.response.TicketStatusResponse
import com.arafat1419.mengantri_app.core.domain.model.*
import com.arafat1419.mengantri_app.core.domain.repository.IDataRepository
import kotlinx.coroutines.flow.Flow

class DataInteractor(private val iDataRepository: IDataRepository) : DataUseCase {
    // -- LOGIN DOMAIN --
    override fun getLogin(customerEmail: String): Flow<List<CustomerDomain>> =
        iDataRepository.getLogin(customerEmail)

    override fun postRegistration(
        customerName: String,
        customerEmail: String,
        customerPassword: String,
        customerPhone: String
    ): Flow<CustomerDomain> =
        iDataRepository.postRegistration(
            CustomerResponse(
                null,
                customerName,
                customerEmail,
                customerPassword,
                customerPhone,
                null,
                null,
                null,
                null
            )
        )

    // -- HOME DOMAIN --
    override fun getCategories(): Flow<List<CategoryDomain>> =
        iDataRepository.getCategories()

    override fun getCompanies(categoryId: Int): Flow<List<CompanyDomain>> =
        iDataRepository.getCompanies(categoryId)

    override fun getServices(companyId: Int): Flow<List<ServiceDomain>> =
        iDataRepository.getServices(companyId)

    override fun getTickets(serviceId: Int): Flow<List<TicketDomain>> =
        iDataRepository.getTickets(serviceId)

    override fun getTicketServed(serviceId: Int): Flow<Int> =
        iDataRepository.getTicketServed(serviceId)

    override fun getServicesAndServed(companyId: Int): Flow<List<ServiceCountDomain>> =
        iDataRepository.getServicesAndServed(companyId)

    override fun postTicket(
        customerId: Int,
        serviceId: Int,
        ticketPersonName: String,
        ticketPersonPhone: String,
        ticketNotes: String,
        ticketServiceTime: String,
        ticketDate: String
    ): Flow<TicketDomain> =
        iDataRepository.postTicket(
            TicketResponse(
                null,
                customerId,
                serviceId,
                ticketPersonName,
                ticketPersonPhone,
                ticketNotes,
                ticketDate,
                null,
                null,
                null,
                null
            )
        )

    override fun getTicket(ticketId: Int): Flow<List<TicketWithServiceDomain>> =
        iDataRepository.getTicket(ticketId)

    override fun updateTicket(ticketId: Int, status: String): Flow<TicketDomain> =
        iDataRepository.updateTicket(ticketId, TicketStatusResponse(status))
}