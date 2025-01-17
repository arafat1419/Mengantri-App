package com.arafat1419.mengantri_app.core.domain.usecase

import android.util.Log
import com.arafat1419.mengantri_app.core.data.remote.response.CustomerResponse
import com.arafat1419.mengantri_app.core.data.remote.response.ServiceOnlyResponse
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
    override fun getCategories(): Flow<List<CategoryDomain>> =
        iDataRepository.getCategories()

    override fun getCompanies(categoryId: Int): Flow<List<CompanyDomain>> =
        iDataRepository.getCompanies(categoryId)

    override fun getSearchCompanies(keyword: String): Flow<List<CompanyDomain>> =
        iDataRepository.getSearchCompanies(keyword)

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
        ticketDate: String
    ): Flow<TicketDomain> =
        iDataRepository.postTicket(
            TicketResponse(
                customerId = customerId,
                serviceId = serviceId,
                ticketPersonName = ticketPersonName,
                ticketPersonPhone = ticketPersonPhone,
                ticketNotes = ticketNotes,
                ticketDate = ticketDate
            )
        )

    override fun getTicket(ticketId: Int): Flow<List<TicketWithServiceDomain>> =
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
    ): Flow<List<TicketWithServiceDomain>> =
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

    override fun postUploadFile(
        fileName: String,
        isBanner: Boolean,
        file: File
    ): Flow<UploadFileDomain> =
        iDataRepository.postUploadFile(fileName, isBanner, file)

    override fun postCompany(companyDomain: CompanyDomain): Flow<CompanyDomain> =
        iDataRepository.postCompany(companyDomain)

    override fun getTicketsSoon(serviceId: Int): Flow<List<TicketDomain>> =
        iDataRepository.getTicketsSoon(serviceId)

    override fun getTicketsByService(serviceId: Int): Flow<List<TicketDomain>> =
        iDataRepository.getTicketsByService(serviceId)

    override fun postService(serviceOnlyDomain: ServiceOnlyDomain): Flow<ServiceOnlyDomain> =
        iDataRepository.postService(serviceOnlyDomain)

    override fun updateService(
        serviceId: Int,
        serviceName: String,
        serviceOpenTime: String,
        serviceCloseTime: String,
        serviceAnnouncement: String,
        serviceMaxCustomer: Int,
        serviceStatus: Int,
        serviceDay: List<String>
    ): Flow<ServiceOnlyDomain> = iDataRepository.updateService(
        serviceId,
        serviceOnlyResponse = ServiceOnlyResponse(
            serviceName = serviceName,
            serviceOpenTime = serviceOpenTime,
            serviceCloseTime = serviceCloseTime,
            serviceAnnouncement = serviceAnnouncement,
            serviceMaxCustomer = serviceMaxCustomer,
            serviceStatus = serviceStatus,
            serviceDay = serviceDay
        )
    )


    // -- PROVINCE, CITY, DISTRICS --
    override fun getProvinces(): Flow<List<ProvinceDomain>> =
        iDataRepository.getProvinces()

    override fun getCities(idProvince: String): Flow<List<CityDomain>> =
        iDataRepository.getCities(idProvince)

    override fun getDistrics(idCity: String): Flow<List<DistricsDomain>> =
        iDataRepository.getDistrics(idCity)
}

