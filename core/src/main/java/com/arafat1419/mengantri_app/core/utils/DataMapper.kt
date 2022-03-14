package com.arafat1419.mengantri_app.core.utils

import com.arafat1419.mengantri_app.core.BuildConfig
import com.arafat1419.mengantri_app.core.data.remote.response.*
import com.arafat1419.mengantri_app.core.domain.model.*

object DataMapper {

    // Get image from Directus
    private const val imageDirectus = "${BuildConfig.BASE_URL_MENGANTRI}assets/"

    // -- Customer Response To Domain --
    fun customerResponseToDomain(input: List<CustomerResponse>): List<CustomerDomain> =
        input.map {
            CustomerDomain(
                it.customerId,
                it.customerName,
                it.customerEmail,
                it.customerPassword,
                it.customerPhone,
                it.customerLocation,
                it.customerStatus,
                it.customerDateCreated,
                it.customerDateUpdated,
                it.customerCode
            )
        }

    fun customerResponseToDomain(input: CustomerResponse): CustomerDomain =
        CustomerDomain(
            input.customerId,
            input.customerName,
            input.customerEmail,
            input.customerPassword,
            input.customerPhone,
            input.customerLocation,
            input.customerStatus,
            input.customerDateCreated,
            input.customerDateUpdated,
            input.customerCode
        )

    // -- Category Response To Domain --
    fun categoryResponseToDomain(input: List<CategoryResponse>): List<CategoryDomain> =
        input.map {
            CategoryDomain(
                it.categoryId,
                it.categoryName,
                imageDirectus + it.categoryImage,
                it.categoryStatus
            )
        }

    // -- Company Response To Domain --
    fun companyResponseToDomain(input: List<CompanyResponse>): List<CompanyDomain> =
        input.map {
            CompanyDomain(
                it.companyId,
                it.customerId,
                it.companyName,
                it.companyPhone,
                imageDirectus + it.companyBanner,
                imageDirectus + it.companyImage,
                it.categoryId,
                it.companyAddress,
                it.companyCity,
                it.companyDistrics,
                it.companyStatus,
                it.companyDateCreated,
                it.companyDateUpdated,
                it.companyOpenTime,
                it.companyCloseTime,
                it.companyExpiredTime
            )
        }

    fun companyNameResponseToDomain(input: CompanyNameResponse): CompanyNameDomain =
        CompanyNameDomain(
            input.companyId,
            input.companyName
        )

    // -- Service Response To Domain --
    fun serviceResponseToDomain(input: List<ServiceResponse>): List<ServiceDomain> =
        input.map {
            ServiceDomain(
                it.serviceId,
                companyNameResponseToDomain(it.companyId!!),
                it.serviceName,
                it.serviceOpenTime,
                it.serviceCloseTime,
                it.serviceTime,
                it.serviceAnnouncement,
                it.serviceMaxCustomer,
                it.serviceStatus,
                it.serviceDateCreated,
                it.serviceDateUpdated
            )
        }

    fun serviceResponseToDomain(input: ServiceResponse): ServiceDomain =
        ServiceDomain(
            input.serviceId,
            companyNameResponseToDomain(input.companyId!!),
            input.serviceName,
            input.serviceOpenTime,
            input.serviceCloseTime,
            input.serviceTime,
            input.serviceAnnouncement,
            input.serviceMaxCustomer,
            input.serviceStatus,
            input.serviceDateCreated,
            input.serviceDateUpdated
        )

    // -- Ticket Response To Domain --
    fun ticketResponseToDomain(input: List<TicketResponse>): List<TicketDomain> =
        input.map {
            TicketDomain(
                it.ticketId,
                it.customerId,
                it.serviceId,
                it.ticketPersonName,
                it.ticketPersonPhone,
                it.ticketNotes,
                it.ticketDate,
                it.ticketStatus,
                it.ticketServiceFinish,
                it.ticketDateCreated
            )
        }

    fun ticketResponseToDomain(input: TicketResponse): TicketDomain =
        TicketDomain(
            input.ticketId,
            input.customerId,
            input.serviceId,
            input.ticketPersonName,
            input.ticketPersonPhone,
            input.ticketNotes,
            input.ticketDate,
            input.ticketStatus,
            input.ticketServiceFinish,
            input.ticketDateCreated
        )

    // -- serviceCount Response To Domain --
    fun serviceCountResponseToDomain(input: List<ServiceCountResponse>): List<ServiceCountDomain> =
        input.map {
            ServiceCountDomain(
                serviceResponseToDomain(it.services),
                it.total,
                it.served,
                it.waiting,
                it.cancel
            )
        }

    // Ticket With Service Response to Domain
    fun ticketWithServiceResponseToDomain(input: List<TicketWithServiceResponse>): List<TicketWithServiceDomain> =
        input.map {
            TicketWithServiceDomain(
                it.ticketId,
                it.customerId,
                serviceResponseToDomain(it.serviceId!!),
                it.ticketPersonName,
                it.ticketPersonPhone,
                it.ticketNotes,
                it.ticketDate,
                it.ticketStatus,
                it.ticketServiceFinish,
                it.ticketDateCreated
            )
        }

    // ServiceXDay Response to Domain
    fun serviceXDayResponseToDomain(input: List<ServiceXDayResponse>): List<ServiceXDayDomain> =
        input.map {
            ServiceXDayDomain(
                it.sxdId,
                it.serviceId,
                it.dayId
            )
        }
}