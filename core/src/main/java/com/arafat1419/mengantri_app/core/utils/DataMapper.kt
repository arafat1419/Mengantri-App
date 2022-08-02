package com.arafat1419.mengantri_app.core.utils

import com.arafat1419.mengantri_app.core.BuildConfig
import com.arafat1419.mengantri_app.core.data.remote.response.*
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.CityResponse
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.DistricsResponse
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.ProvinceResponse
import com.arafat1419.mengantri_app.core.domain.model.*
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.CityDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.DistricsDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.ProvinceDomain

object DataMapper {

    // Get image from Directus
    const val imageDirectus = "${BuildConfig.BASE_URL_MENGANTRI}assets/"

    // -- CATEGORY --
    fun categoryResponseToDomain(input: List<CategoryResponse>): List<CategoryDomain> =
        input.map {
            CategoryDomain(
                it.categoryId,
                it.categoryName,
                it.categoryImage,
                it.categoryStatus
            )
        }

    // -- COMPANY --
    fun companyResponseToDomain(input: List<CompanyResponse>): List<CompanyDomain> =
        input.map {
            companyResponseToDomain(it)
        }

    fun companyResponseToDomain(input: CompanyResponse): CompanyDomain =
        CompanyDomain(
            input.companyId,
            input.customerId,
            input.companyName,
            input.companyPhone,
            input.companyBanner,
            input.companyImage,
            input.categoryId,
            input.companyAddress,
            input.companyProvince,
            input.companyCity,
            input.companyDistrics,
            input.companyStatus,
            input.companyDateCreated,
            input.companyDateUpdated,
            input.companyOpenTime,
            input.companyCloseTime,
            input.companyExpiredTime
        )

    fun companyDomainToResponse(input: CompanyDomain): CompanyResponse =
        CompanyResponse(
            input.companyId,
            input.customerId,
            input.companyName,
            input.companyPhone,
            input.companyBanner,
            input.companyImage,
            input.categoryId,
            input.companyAddress,
            input.companyProvince,
            input.companyCity,
            input.companyDistrics,
            input.companyStatus,
            input.companyDateCreated,
            input.companyDateUpdated,
            input.companyOpenTime,
            input.companyCloseTime,
            input.companyExpiredTime
        )

    // -- SERVICE --
    fun serviceCountResponseToDomain(input: List<ServiceCountResponse>): List<ServiceCountDomain> =
        input.map {
            serviceCountResponseToDomain(it)
        }

    fun serviceCountResponseToDomain(input: ServiceCountResponse): ServiceCountDomain =
        ServiceCountDomain(
            input.service?.let { serviceResponseToDomain(it) },
            input.total,
            input.served,
            input.waiting,
            input.cancel
        )

    // -- TICKET --
    fun ticketDetailResponseToDomain(input: List<TicketDetailResponse>): List<TicketDetailDomain> =
        input.map {
            ticketDetailResponseToDomain(it)
        }

    fun ticketDetailResponseToDomain(input: TicketDetailResponse): TicketDetailDomain =
        TicketDetailDomain(
            input.ticket?.let { ticketServiceResponseToDomain(it) },
            input.queueNumber,
            input.estimatedTime,
            input.isProcess
        )

    fun ticketServiceResponseToDomain(input: List<TicketServiceResponse>): List<TicketServiceDomain> =
        input.map {
            ticketServiceResponseToDomain(it)
        }

    fun ticketServiceResponseToDomain(input: TicketServiceResponse): TicketServiceDomain =
        TicketServiceDomain(
            input.ticketId,
            input.customerId,
            serviceResponseToDomain(input.serviceId!!),
            input.ticketPersonName,
            input.ticketPersonPhone,
            input.ticketNotes,
            input.ticketDate,
            input.ticketStatus,
            input.ticketEstimatedTime,
            input.ticketQrImage,
            input.ticketServiceFinish,
            input.ticketDateCreated
        )

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

    // -- Service Response To Domain --
    fun serviceResponseToDomain(input: List<ServiceResponse>): List<ServiceDomain> =
        input.map {
            serviceResponseToDomain(it)
        }

    fun serviceResponseToDomain(input: ServiceResponse): ServiceDomain =
        ServiceDomain(
            input.serviceId,
            input.companyId,
            input.serviceName,
            input.serviceOpenTime,
            input.serviceCloseTime,
            input.serviceTime,
            input.serviceAnnouncement,
            input.serviceMaxCustomer,
            input.serviceCashier,
            input.serviceStatus,
            input.serviceDay,
            input.serviceDateCreated,
            input.serviceDateUpdated
        )

    fun serviceDomainToResponse(input: ServiceDomain): ServiceResponse =
        ServiceResponse(
            input.serviceId,
            input.companyId,
            input.serviceName,
            input.serviceOpenTime,
            input.serviceCloseTime,
            input.serviceTime,
            input.serviceAnnouncement,
            input.serviceMaxCustomer,
            input.serviceCashier,
            input.serviceStatus,
            input.serviceDay,
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
                it.ticketEstimatedTime,
                it.ticketQrImage,
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
            input.ticketEstimatedTime,
            input.ticketQrImage,
            input.ticketServiceFinish,
            input.ticketDateCreated
        )

    // ServiceXDay Response to Domain
    fun serviceXDayResponseToDomain(input: List<ServiceXDayResponse>): List<ServiceXDayDomain> =
        input.map {
            ServiceXDayDomain(
                it.sxdId,
                it.serviceId,
                it.dayId
            )
        }

    // Province Response to Domain
    fun provinceResponseToDomain(input: List<ProvinceResponse>): List<ProvinceDomain> =
        input.map {
            ProvinceDomain(
                it.id,
                it.provinceName
            )
        }

    // City Response to Domain
    fun cityResponseToDomain(input: List<CityResponse>): List<CityDomain> =
        input.map {
            CityDomain(
                it.id,
                it.idProvince,
                it.cityName
            )
        }

    // Districs Response to Domain
    fun districsResponseToDomain(input: List<DistricsResponse>): List<DistricsDomain> =
        input.map {
            DistricsDomain(
                it.id,
                it.idCity,
                it.districsName
            )
        }
}