package com.arafat1419.mengantri_app.core.domain.usecase

import android.util.Log
import com.arafat1419.mengantri_app.core.data.remote.response.CustomerResponse
import com.arafat1419.mengantri_app.core.data.remote.response.TicketResponse
import com.arafat1419.mengantri_app.core.domain.model.*
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.CityDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.DistricsDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.ProvinceDomain
import com.arafat1419.mengantri_app.core.domain.repository.IDataRepository
import com.arafat1419.mengantri_app.core.utils.StatusHelper
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DataInteractor(private val iDataRepository: IDataRepository) : DataUseCase {
    // -- CATEGORY --
    override fun getCategories(): Flow<List<CategoryDomain>> =
        iDataRepository.getCategories()

    // -- COMPANY --
    override fun getNewestCompanies(): Flow<List<CompanyDomain>> =
        iDataRepository.getNewestCompanies()

    override fun getCustomerCompany(customerId: Int): Flow<List<CompanyDomain>> =
        iDataRepository.getCustomerCompany(customerId)

    override fun getCompaniesByCategory(categoryId: Int): Flow<List<CompanyDomain>> =
        iDataRepository.getCompaniesByCategory(categoryId)

    override fun getCompany(companyId: Int): Flow<CompanyDomain> =
        iDataRepository.getCompany(companyId)

    override fun getSearchCompanies(keyword: String): Flow<List<CompanyDomain>> =
        iDataRepository.getSearchCompanies(keyword)

    override fun postCompany(companyDomain: CompanyDomain): Flow<CompanyDomain> =
        iDataRepository.postCompany(companyDomain)

    override fun updateCompany(companyId: Int, companyDomain: CompanyDomain): Flow<CompanyDomain> =
        iDataRepository.updateCompany(companyId, companyDomain)

    // -- SERVICE --
    override fun getServicesCountByCompany(companyId: Int): Flow<List<ServiceCountDomain>> =
        iDataRepository.getServicesCountByCompany(companyId)

    override fun getServiceCount(serviceId: Int, ticketDate: String?): Flow<ServiceCountDomain> =
        iDataRepository.getServiceCount(serviceId, ticketDate)

    override fun getServiceEstimated(
        serviceId: Int,
        ticketDate: String
    ): Flow<EstimatedTimeDomain?> =
        iDataRepository.getServiceEstimated(serviceId, ticketDate)

    override fun getSearchServices(keyword: String): Flow<List<ServiceCountDomain>> =
        iDataRepository.getSearchServices(keyword)

    override fun getServicesByCompany(companyId: Int): Flow<List<ServiceDomain>> =
        iDataRepository.getServicesByCompany(companyId)

    override fun getService(serviceId: Int): Flow<ServiceDomain> =
        iDataRepository.getService(serviceId)

    override fun postService(serviceDomain: ServiceDomain): Flow<ServiceDomain> =
        iDataRepository.postService(serviceDomain)

    override fun updateService(serviceId: Int, serviceDomain: ServiceDomain): Flow<ServiceDomain> =
        iDataRepository.updateService(serviceId, serviceDomain)

    override fun deleteService(serviceId: Int): Flow<Boolean> =
        iDataRepository.deleteService(serviceId)

    // -- TICKET --
    override fun getTicketServiceDetail(ticketId: Int): Flow<TicketDetailDomain> =
        iDataRepository.getTicketServiceDetail(ticketId)

    override fun getTicketsWaiting(customerId: Int): Flow<List<TicketDetailDomain>> =
        iDataRepository.getTicketsWaiting(customerId)

    override fun getTicketsHistory(
        customerId: Int?,
        serviceId: Int?
    ): Flow<List<TicketDetailDomain>> =
        iDataRepository.getTicketsHistory(customerId, serviceId)

    override fun getTicketsToday(serviceId: Int?): Flow<List<TicketDetailDomain>> =
        iDataRepository.getTicketsToday(serviceId)

    override fun getTicketsSoon(serviceId: Int?): Flow<List<TicketDetailDomain>> =
        iDataRepository.getTicketsSoon(serviceId)

    // -- FILES --
    override fun postUploadFile(
        fileName: String,
        isBanner: Boolean,
        file: File
    ): Flow<UploadFileDomain> =
        iDataRepository.postUploadFile(fileName, isBanner, file)

    // -- LOGIN DOMAIN --
    override fun getLogin(customerEmail: String): Flow<List<CustomerDomain>> =
        iDataRepository.getLogin(customerEmail)

    override fun checkHash(value: String, hash: String): Flow<Boolean> =
        iDataRepository.checkHash(value, hash)

    override fun postRegistration(customerEmail: String): Flow<CustomerDomain> =
        iDataRepository.postRegistration(
            CustomerResponse(
                customerEmail = customerEmail
            )
        )

    override fun updateCustomerCode(customerId: Int, customerCode: String): Flow<CustomerDomain> =
        iDataRepository.patchCustomer(
            customerId,
            CustomerResponse(customerCode = customerCode)
        )

    override fun updateBiodata(
        customerId: Int,
        customerName: String,
        customerPassword: String,
        customerPhone: String,
        customerLocation: String
    ): Flow<CustomerDomain> =
        iDataRepository.patchCustomer(
            customerId,
            CustomerResponse(
                customerName = customerName,
                customerPassword = customerPassword,
                customerPhone = customerPhone,
                customerLocation = customerLocation,
                customerStatus = 1
            )
        )

    // -- HOME DOMAIN --
    override fun getSearchCompaniesByCategory(
        keyword: String,
        categoryId: Int
    ): Flow<List<CompanyDomain>> =
        iDataRepository.getSearchCompaniesByCategory(keyword, categoryId)

    override fun getServices(companyId: Int): Flow<List<ServiceDomain>> =
        iDataRepository.getServices(companyId)

    override fun getTickets(serviceId: Int, ticketDate: String?): Flow<List<TicketDomain>> =
        iDataRepository.getTickets(serviceId, ticketDate)

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
        ticketDate: String,
        ticketEstimatedTime: String
    ): Flow<TicketDomain> =
        iDataRepository.postTicket(
            TicketResponse(
                customerId = customerId,
                serviceId = serviceId,
                ticketPersonName = ticketPersonName,
                ticketPersonPhone = ticketPersonPhone,
                ticketNotes = ticketNotes,
                ticketDate = ticketDate,
                ticketEstimatedTime = ticketEstimatedTime
            )
        )

    override fun getTicket(ticketId: Int): Flow<List<TicketServiceDomain>> =
        iDataRepository.getTicket(ticketId)

    override fun updateTicket(ticketId: Int, status: String): Flow<TicketDomain> {
        val df: DateFormat =
            SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentTime: String = df.format(Date())
        Log.d("CHCK", currentTime)
        return iDataRepository.updateTicket(
            ticketId,
            when (status) {
                StatusHelper.TICKET_PROGRESS -> TicketResponse(
                    ticketStatus = status,
                    ticketServiceStart = currentTime
                )
                StatusHelper.TICKET_SUCCESS -> TicketResponse(
                    ticketStatus = status,
                    ticketServiceFinish = currentTime
                )
                else -> {
                    TicketResponse(ticketStatus = status)
                }
            }
        )
    }

    override fun getServiceXDay(serviceId: Int, dayId: Int): Flow<List<ServiceXDayDomain>> =
        iDataRepository.getServiceXDay(serviceId, dayId)

    // -- TICKET DOMAIN --
    override fun getTicketByStatus(
        customerId: Int,
        ticketStatus: String
    ): Flow<List<TicketServiceDomain>> =
        iDataRepository.getTicketByStatus(customerId, ticketStatus)

    // -- PROFILE DOMAIN --
    override fun updateProfile(
        customerId: Int,
        customerName: String,
        customerPhone: String
    ): Flow<CustomerDomain> =
        iDataRepository.patchCustomer(
            customerId,
            CustomerResponse(
                customerName = customerName,
                customerPhone = customerPhone
            )
        )

    override fun updatePassword(customerId: Int, customerPassword: String): Flow<CustomerDomain> =
        iDataRepository.patchCustomer(
            customerId,
            CustomerResponse(customerPassword = customerPassword)
        )

    override fun getUserCompany(customerId: Int): Flow<List<CompanyDomain>> =
        iDataRepository.getUserCompany(customerId)

    override fun getTicketsByService(serviceId: Int): Flow<List<TicketDomain>> =
        iDataRepository.getTicketsByService(serviceId)


    // -- PROVINCE, CITY, DISTRICS --
    override fun getProvinces(): Flow<List<ProvinceDomain>> =
        iDataRepository.getProvinces()

    override fun getCities(idProvince: String): Flow<List<CityDomain>> =
        iDataRepository.getCities(idProvince)

    override fun getDistrics(idCity: String): Flow<List<DistricsDomain>> =
        iDataRepository.getDistrics(idCity)
}

