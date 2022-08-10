package com.arafat1419.mengantri_app.core.domain.usecase

import com.arafat1419.mengantri_app.core.data.remote.response.CustomerResponse
import com.arafat1419.mengantri_app.core.data.remote.response.TicketResponse
import com.arafat1419.mengantri_app.core.domain.model.*
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.CityDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.DistricsDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.ProvinceDomain
import com.arafat1419.mengantri_app.core.domain.repository.IDataRepository
import com.arafat1419.mengantri_app.core.utils.StatusHelper
import com.arafat1419.mengantri_app.core.vo.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DataInteractor(private val iDataRepository: IDataRepository) : DataUseCase {
    // -- CATEGORY --
    override fun getCategories(): Flow<Resource<List<CategoryDomain>>> =
        iDataRepository.getCategories()

    // -- COMPANY --
    override fun getNewestCompanies(): Flow<Resource<List<CompanyDomain>>> =
        iDataRepository.getNewestCompanies()

    override fun getCustomerCompany(customerId: Int): Flow<Resource<List<CompanyDomain>>> =
        iDataRepository.getCustomerCompany(customerId)

    override fun getCompaniesByCategory(categoryId: Int): Flow<Resource<List<CompanyDomain>>> =
        iDataRepository.getCompaniesByCategory(categoryId)

    override fun getCompany(companyId: Int): Flow<Resource<CompanyDomain>> =
        iDataRepository.getCompany(companyId)

    override fun getSearchCompanies(keyword: String): Flow<Resource<List<CompanyDomain>>> =
        iDataRepository.getSearchCompanies(keyword)

    override fun postCompany(companyDomain: CompanyDomain): Flow<Resource<CompanyDomain>> =
        iDataRepository.postCompany(companyDomain)

    override fun updateCompany(
        companyId: Int,
        companyDomain: CompanyDomain
    ): Flow<Resource<CompanyDomain>> =
        iDataRepository.updateCompany(companyId, companyDomain)

    // -- SERVICE --
    override fun getServicesCountByCompany(companyId: Int): Flow<Resource<List<ServiceCountDomain>>> =
        iDataRepository.getServicesCountByCompany(companyId)

    override fun getServiceCount(
        serviceId: Int,
        ticketDate: String?
    ): Flow<Resource<ServiceCountDomain>> =
        iDataRepository.getServiceCount(serviceId, ticketDate)

    override fun getServiceEstimated(
        serviceId: Int,
        ticketDate: String
    ): Flow<Resource<EstimatedTimeDomain>> =
        iDataRepository.getServiceEstimated(serviceId, ticketDate)

    override fun getIsAvailable(
        customerId: Int,
        ticketDate: String,
        estimatedTime: String,
        serviceId: Int
    ): Flow<Resource<Boolean>> =
        iDataRepository.getIsAvailable(customerId, ticketDate, estimatedTime, serviceId)

    override fun getSearchServices(keyword: String): Flow<Resource<List<ServiceCountDomain>>> =
        iDataRepository.getSearchServices(keyword)

    override fun getServicesByCompany(companyId: Int): Flow<Resource<List<ServiceDomain>>> =
        iDataRepository.getServicesByCompany(companyId)

    override fun getService(serviceId: Int): Flow<Resource<ServiceDomain>> =
        iDataRepository.getService(serviceId)

    override fun postService(serviceDomain: ServiceDomain): Flow<Resource<ServiceDomain>> =
        iDataRepository.postService(serviceDomain)

    override fun updateService(
        serviceId: Int,
        serviceDomain: ServiceDomain
    ): Flow<Resource<ServiceDomain>> =
        iDataRepository.updateService(serviceId, serviceDomain)

    override fun deleteService(serviceId: Int): Flow<Resource<Boolean>> =
        iDataRepository.deleteService(serviceId)

    // -- TICKET --
    override fun getTicketServiceDetail(ticketId: Int): Flow<Resource<TicketDetailDomain>> =
        iDataRepository.getTicketServiceDetail(ticketId)

    override fun getTicketsWaiting(customerId: Int): Flow<Resource<List<TicketDetailDomain>>> =
        iDataRepository.getTicketsWaiting(customerId)

    override fun getTicketsHistory(
        customerId: Int?,
        serviceId: Int?
    ): Flow<Resource<List<TicketDetailDomain>>> =
        iDataRepository.getTicketsHistory(customerId, serviceId)

    override fun getTicketsToday(serviceId: Int?): Flow<Resource<List<TicketDetailDomain>>> =
        iDataRepository.getTicketsToday(serviceId)

    override fun getTicketsSoon(serviceId: Int?): Flow<Resource<List<TicketDetailDomain>>> =
        iDataRepository.getTicketsSoon(serviceId)

    override fun postTicket(
        customerId: Int,
        serviceId: Int,
        ticketPersonName: String,
        ticketPersonPhone: String,
        ticketNotes: String,
        ticketDate: String,
        ticketEstimatedTime: String
    ): Flow<Resource<TicketDomain>> =
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

    override fun updateTicket(ticketId: Int, status: String): Flow<Resource<TicketDomain>> {
        val df: DateFormat =
            SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentTime: String = df.format(Date())
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

    // -- CUSTOMER --
    override fun getCustomer(customerId: Int): Flow<Resource<CustomerDomain>> =
        iDataRepository.getCustomer(customerId)

    override fun updateProfile(
        customerId: Int,
        customerName: String,
        customerPhone: String
    ): Flow<Resource<CustomerDomain>> =
        iDataRepository.patchCustomer(
            customerId,
            CustomerResponse(
                customerName = customerName,
                customerPhone = customerPhone
            )
        )

    override fun updatePassword(
        customerId: Int,
        customerPassword: String
    ): Flow<Resource<CustomerDomain>> =
        iDataRepository.patchCustomer(
            customerId,
            CustomerResponse(customerPassword = customerPassword)
        )

    override fun getLogin(customerEmail: String): Flow<Resource<List<CustomerDomain>>> =
        iDataRepository.getLogin(customerEmail)

    override fun postRegistration(customerEmail: String): Flow<Resource<CustomerDomain>> =
        iDataRepository.postRegistration(
            CustomerResponse(
                customerEmail = customerEmail
            )
        )

    override fun updateCustomerCode(
        customerId: Int,
        customerCode: String
    ): Flow<Resource<CustomerDomain>> =
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
    ): Flow<Resource<CustomerDomain>> =
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

    // -- FILES --
    override fun postUploadFile(
        fileName: String,
        isBanner: Boolean,
        file: File
    ): Flow<Resource<UploadFileDomain>> =
        iDataRepository.postUploadFile(fileName, isBanner, file)

    // -- UTILS --
    override fun checkHash(value: String, hash: String): Flow<Resource<Boolean>> =
        iDataRepository.checkHash(value, hash)

    // -- PROVINCE, CITY, DISTRICS --
    override fun getProvinces(): Flow<Resource<List<ProvinceDomain>>> =
        iDataRepository.getProvinces()

    override fun getCities(idProvince: String): Flow<Resource<List<CityDomain>>> =
        iDataRepository.getCities(idProvince)

    override fun getDistrics(idCity: String): Flow<Resource<List<DistricsDomain>>> =
        iDataRepository.getDistricts(idCity)

    override fun getToken(): Flow<Resource<String>> =
        iDataRepository.getToken()

    override fun deleteToken(): Flow<Resource<Boolean>> =
        iDataRepository.deleteToken()
}

