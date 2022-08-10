package com.arafat1419.mengantri_app.core.data

import com.arafat1419.mengantri_app.core.data.remote.RemoteDataSource
import com.arafat1419.mengantri_app.core.data.remote.response.*
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.CityResponse
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.DistricsResponse
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.ProvinceResponse
import com.arafat1419.mengantri_app.core.domain.model.*
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.CityDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.DistricsDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.ProvinceDomain
import com.arafat1419.mengantri_app.core.domain.repository.IDataRepository
import com.arafat1419.mengantri_app.core.utils.DataMapper
import com.arafat1419.mengantri_app.core.vo.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import java.io.File

class DataRepository(private val remoteDataSource: RemoteDataSource) : IDataRepository {
    override fun getCategories(): Flow<Resource<List<CategoryDomain>>> =
        object : NetworkBoundResource<List<CategoryDomain>, List<CategoryResponse>>() {
            override suspend fun load(data: List<CategoryResponse>): Flow<List<CategoryDomain>> =
                listOf(DataMapper.categoryResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<CategoryResponse>>> =
                remoteDataSource.getCategories()

            override suspend fun saveCallResult(data: List<CategoryResponse>) {}

        }.asFlow()

    // -- COMPANY --

    override fun getNewestCompanies(): Flow<Resource<List<CompanyDomain>>> =
        object : NetworkBoundResource<List<CompanyDomain>, List<CompanyResponse>>() {
            override suspend fun load(data: List<CompanyResponse>): Flow<List<CompanyDomain>> =
                listOf(DataMapper.companyResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<CompanyResponse>>> =
                remoteDataSource.getNewestCompanies()

            override suspend fun saveCallResult(data: List<CompanyResponse>) {}

        }.asFlow()

    override fun getCustomerCompany(customerId: Int): Flow<Resource<List<CompanyDomain>>> =
        object : NetworkBoundResource<List<CompanyDomain>, List<CompanyResponse>>() {
            override suspend fun load(data: List<CompanyResponse>): Flow<List<CompanyDomain>> =
                listOf(DataMapper.companyResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<CompanyResponse>>> =
                remoteDataSource.getCustomerCompany(customerId)

            override suspend fun saveCallResult(data: List<CompanyResponse>) {}

        }.asFlow()

    override fun getCompaniesByCategory(categoryId: Int): Flow<Resource<List<CompanyDomain>>> =
        object : NetworkBoundResource<List<CompanyDomain>, List<CompanyResponse>>() {
            override suspend fun load(data: List<CompanyResponse>): Flow<List<CompanyDomain>> =
                listOf(DataMapper.companyResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<CompanyResponse>>> =
                remoteDataSource.getCompaniesByCategory(categoryId)

            override suspend fun saveCallResult(data: List<CompanyResponse>) {}

        }.asFlow()

    override fun getCompany(companyId: Int): Flow<Resource<CompanyDomain>> =
        object : NetworkBoundResource<CompanyDomain, CompanyResponse>() {
            override suspend fun load(data: CompanyResponse): Flow<CompanyDomain> =
                listOf(DataMapper.companyResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<CompanyResponse>> =
                remoteDataSource.getCompany(companyId)

            override suspend fun saveCallResult(data: CompanyResponse) {}

        }.asFlow()

    override fun getSearchCompanies(keyword: String): Flow<Resource<List<CompanyDomain>>> =
        object : NetworkBoundResource<List<CompanyDomain>, List<CompanyResponse>>() {
            override suspend fun load(data: List<CompanyResponse>): Flow<List<CompanyDomain>> =
                listOf(DataMapper.companyResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<CompanyResponse>>> =
                remoteDataSource.getSearchCompanies(keyword)

            override suspend fun saveCallResult(data: List<CompanyResponse>) {}

        }.asFlow()

    override fun postCompany(companyDomain: CompanyDomain): Flow<Resource<CompanyDomain>> =
        object : NetworkBoundResource<CompanyDomain, CompanyResponse>() {
            override suspend fun load(data: CompanyResponse): Flow<CompanyDomain> =
                listOf(DataMapper.companyResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<CompanyResponse>> =
                remoteDataSource.postCompany(DataMapper.companyDomainToResponse(companyDomain))

            override suspend fun saveCallResult(data: CompanyResponse) {}

        }.asFlow()

    override fun updateCompany(
        companyId: Int,
        companyDomain: CompanyDomain
    ): Flow<Resource<CompanyDomain>> =
        object : NetworkBoundResource<CompanyDomain, CompanyResponse>() {
            override suspend fun load(data: CompanyResponse): Flow<CompanyDomain> =
                listOf(DataMapper.companyResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<CompanyResponse>> =
                remoteDataSource.updateCompany(
                    companyId,
                    DataMapper.companyDomainToResponse(companyDomain)
                )

            override suspend fun saveCallResult(data: CompanyResponse) {}

        }.asFlow()

    // -- SERVICE --
    override fun getServicesCountByCompany(companyId: Int): Flow<Resource<List<ServiceCountDomain>>> =
        object : NetworkBoundResource<List<ServiceCountDomain>, List<ServiceCountResponse>>() {
            override suspend fun load(data: List<ServiceCountResponse>): Flow<List<ServiceCountDomain>> =
                listOf(DataMapper.serviceCountResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<ServiceCountResponse>>> =
                remoteDataSource.getServicesCountByCompany(companyId)

            override suspend fun saveCallResult(data: List<ServiceCountResponse>) {}

        }.asFlow()

    override fun getServiceCount(
        serviceId: Int,
        ticketDate: String?
    ): Flow<Resource<ServiceCountDomain>> =
        object : NetworkBoundResource<ServiceCountDomain, ServiceCountResponse>() {
            override suspend fun load(data: ServiceCountResponse): Flow<ServiceCountDomain> =
                listOf(DataMapper.serviceCountResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<ServiceCountResponse>> =
                remoteDataSource.getServiceCount(serviceId, ticketDate)

            override suspend fun saveCallResult(data: ServiceCountResponse) {}

        }.asFlow()

    override fun getServiceEstimated(
        serviceId: Int,
        ticketDate: String
    ): Flow<Resource<EstimatedTimeDomain>> =
        object : NetworkBoundResource<EstimatedTimeDomain, EstimatedTimeResponse>() {
            override suspend fun load(data: EstimatedTimeResponse): Flow<EstimatedTimeDomain> =
                listOf(
                    EstimatedTimeDomain(
                        data.estimatedTime,
                        data.isAvailable
                    )
                ).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<EstimatedTimeResponse>> =
                remoteDataSource.getServiceEstimated(serviceId, ticketDate)

            override suspend fun saveCallResult(data: EstimatedTimeResponse) {}

        }.asFlow()

    override fun getIsAvailable(
        customerId: Int,
        ticketDate: String,
        estimatedTime: String,
        serviceId: Int
    ): Flow<Resource<Boolean>> =
        object : NetworkBoundResource<Boolean, IsAvailableResponse>() {
            override suspend fun load(data: IsAvailableResponse): Flow<Boolean> =
                listOf(data.isAvailable == true).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<IsAvailableResponse>> =
                remoteDataSource.getIsAvailable(customerId, ticketDate, estimatedTime, serviceId)

            override suspend fun saveCallResult(data: IsAvailableResponse) {}

        }.asFlow()

    override fun getSearchServices(keyword: String): Flow<Resource<List<ServiceCountDomain>>> =
        object : NetworkBoundResource<List<ServiceCountDomain>, List<ServiceCountResponse>>() {
            override suspend fun load(data: List<ServiceCountResponse>): Flow<List<ServiceCountDomain>> =
                listOf(DataMapper.serviceCountResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<ServiceCountResponse>>> =
                remoteDataSource.getSearchServices(keyword)

            override suspend fun saveCallResult(data: List<ServiceCountResponse>) {}

        }.asFlow()

    override fun getServicesByCompany(companyId: Int): Flow<Resource<List<ServiceDomain>>> =
        object : NetworkBoundResource<List<ServiceDomain>, List<ServiceResponse>>() {
            override suspend fun load(data: List<ServiceResponse>): Flow<List<ServiceDomain>> =
                listOf(DataMapper.serviceResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<ServiceResponse>>> =
                remoteDataSource.getServicesByCompany(companyId)

            override suspend fun saveCallResult(data: List<ServiceResponse>) {}

        }.asFlow()

    override fun postService(serviceDomain: ServiceDomain): Flow<Resource<ServiceDomain>> =
        object : NetworkBoundResource<ServiceDomain, ServiceResponse>() {
            override suspend fun load(data: ServiceResponse): Flow<ServiceDomain> =
                listOf(DataMapper.serviceResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<ServiceResponse>> =
                remoteDataSource.postService(DataMapper.serviceDomainToResponse(serviceDomain))

            override suspend fun saveCallResult(data: ServiceResponse) {}

        }.asFlow()

    override fun getService(serviceId: Int): Flow<Resource<ServiceDomain>> =
        object : NetworkBoundResource<ServiceDomain, ServiceResponse>() {
            override suspend fun load(data: ServiceResponse): Flow<ServiceDomain> =
                listOf(DataMapper.serviceResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<ServiceResponse>> =
                remoteDataSource.getService(serviceId)

            override suspend fun saveCallResult(data: ServiceResponse) {}

        }.asFlow()

    override fun updateService(
        serviceId: Int,
        serviceDomain: ServiceDomain
    ): Flow<Resource<ServiceDomain>> =
        object : NetworkBoundResource<ServiceDomain, ServiceResponse>() {
            override suspend fun load(data: ServiceResponse): Flow<ServiceDomain> =
                listOf(DataMapper.serviceResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<ServiceResponse>> =
                remoteDataSource.updateService(
                    serviceId,
                    DataMapper.serviceDomainToResponse(serviceDomain)
                )

            override suspend fun saveCallResult(data: ServiceResponse) {}

        }.asFlow()

    override fun deleteService(serviceId: Int): Flow<Resource<Boolean>> =
        object : NetworkBoundResource<Boolean, Boolean>() {
            override suspend fun load(data: Boolean): Flow<Boolean> =
                listOf(data).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<Boolean>> =
                remoteDataSource.deleteService(serviceId)

            override suspend fun saveCallResult(data: Boolean) {}

        }.asFlow()

    // -- TICKET --
    override fun getTicketServiceDetail(ticketId: Int): Flow<Resource<TicketDetailDomain>> =
        object : NetworkBoundResource<TicketDetailDomain, TicketDetailResponse>() {
            override suspend fun load(data: TicketDetailResponse): Flow<TicketDetailDomain> =
                listOf(DataMapper.ticketDetailResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<TicketDetailResponse>> =
                remoteDataSource.getTicketServiceDetail(ticketId)

            override suspend fun saveCallResult(data: TicketDetailResponse) {}

        }.asFlow()

    override fun getTicketsWaiting(customerId: Int): Flow<Resource<List<TicketDetailDomain>>> =
        object : NetworkBoundResource<List<TicketDetailDomain>, List<TicketDetailResponse>>() {
            override suspend fun load(data: List<TicketDetailResponse>): Flow<List<TicketDetailDomain>> =
                listOf(DataMapper.ticketDetailResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<TicketDetailResponse>>> =
                remoteDataSource.getTicketsWaiting(customerId)

            override suspend fun saveCallResult(data: List<TicketDetailResponse>) {}

        }.asFlow()

    override fun getTicketsHistory(
        customerId: Int?,
        serviceId: Int?
    ): Flow<Resource<List<TicketDetailDomain>>> =
        object : NetworkBoundResource<List<TicketDetailDomain>, List<TicketDetailResponse>>() {
            override suspend fun load(data: List<TicketDetailResponse>): Flow<List<TicketDetailDomain>> =
                listOf(DataMapper.ticketDetailResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<TicketDetailResponse>>> =
                remoteDataSource.getTicketsHistory(customerId, serviceId)

            override suspend fun saveCallResult(data: List<TicketDetailResponse>) {}

        }.asFlow()

    override fun getTicketsToday(serviceId: Int?): Flow<Resource<List<TicketDetailDomain>>> =
        object : NetworkBoundResource<List<TicketDetailDomain>, List<TicketDetailResponse>>() {
            override suspend fun load(data: List<TicketDetailResponse>): Flow<List<TicketDetailDomain>> =
                listOf(DataMapper.ticketDetailResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<TicketDetailResponse>>> =
                remoteDataSource.getTicketsToday(serviceId)

            override suspend fun saveCallResult(data: List<TicketDetailResponse>) {}

        }.asFlow()

    override fun getTicketsSoon(serviceId: Int?): Flow<Resource<List<TicketDetailDomain>>> =
        object : NetworkBoundResource<List<TicketDetailDomain>, List<TicketDetailResponse>>() {
            override suspend fun load(data: List<TicketDetailResponse>): Flow<List<TicketDetailDomain>> =
                listOf(DataMapper.ticketDetailResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<TicketDetailResponse>>> =
                remoteDataSource.getTicketsSoon(serviceId)

            override suspend fun saveCallResult(data: List<TicketDetailResponse>) {}

        }.asFlow()

    override fun postTicket(ticketResponse: TicketResponse): Flow<Resource<TicketDomain>> =
        object : NetworkBoundResource<TicketDomain, TicketResponse>() {
            override suspend fun load(data: TicketResponse): Flow<TicketDomain> =
                listOf(DataMapper.ticketResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<TicketResponse>> =
                remoteDataSource.postTicket(ticketResponse)

            override suspend fun saveCallResult(data: TicketResponse) {}

        }.asFlow()

    override fun updateTicket(
        ticketId: Int,
        ticketResponse: TicketResponse
    ): Flow<Resource<TicketDomain>> =
        object : NetworkBoundResource<TicketDomain, TicketResponse>() {
            override suspend fun load(data: TicketResponse): Flow<TicketDomain> =
                listOf(DataMapper.ticketResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<TicketResponse>> =
                remoteDataSource.updateTicket(ticketId, ticketResponse)

            override suspend fun saveCallResult(data: TicketResponse) {}

        }.asFlow()

    // -- CUSTOMER --

    override fun getCustomer(customerId: Int): Flow<Resource<CustomerDomain>> =
        object : NetworkBoundResource<CustomerDomain, CustomerResponse>() {
            override suspend fun load(data: CustomerResponse): Flow<CustomerDomain> =
                listOf(DataMapper.customerResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<CustomerResponse>> =
                remoteDataSource.getCustomer(customerId)

            override suspend fun saveCallResult(data: CustomerResponse) {}

        }.asFlow()


    override fun getLogin(customerEmail: String): Flow<Resource<List<CustomerDomain>>> =
        object : NetworkBoundResource<List<CustomerDomain>, List<CustomerResponse>>() {
            override suspend fun load(data: List<CustomerResponse>): Flow<List<CustomerDomain>> =
                listOf(DataMapper.customerResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<CustomerResponse>>> =
                remoteDataSource.getLogin(customerEmail)

            override suspend fun saveCallResult(data: List<CustomerResponse>) {}

        }.asFlow()

    override fun postRegistration(customerResponse: CustomerResponse): Flow<Resource<CustomerDomain>> =
        object : NetworkBoundResource<CustomerDomain, CustomerResponse>() {
            override suspend fun load(data: CustomerResponse): Flow<CustomerDomain> =
                listOf(DataMapper.customerResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<CustomerResponse>> =
                remoteDataSource.postRegistration(customerResponse)

            override suspend fun saveCallResult(data: CustomerResponse) {}

        }.asFlow()

    override fun patchCustomer(
        customerId: Int,
        customerResponse: CustomerResponse
    ): Flow<Resource<CustomerDomain>> =
        object : NetworkBoundResource<CustomerDomain, CustomerResponse>() {
            override suspend fun load(data: CustomerResponse): Flow<CustomerDomain> =
                listOf(DataMapper.customerResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<CustomerResponse>> =
                remoteDataSource.patchCustomer(customerId, customerResponse)

            override suspend fun saveCallResult(data: CustomerResponse) {}

        }.asFlow()

    // -- FILES --
    override fun postUploadFile(
        fileName: String, isBanner: Boolean, file: File
    ): Flow<Resource<UploadFileDomain>> =
        object : NetworkBoundResource<UploadFileDomain, UploadFileResponse>() {
            override suspend fun load(data: UploadFileResponse): Flow<UploadFileDomain> =
                listOf(UploadFileDomain(data.fileId)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<UploadFileResponse>> =
                remoteDataSource.postUploadFile(fileName, isBanner, file)

            override suspend fun saveCallResult(data: UploadFileResponse) {}

        }.asFlow()

    // -- UTILS --
    override fun checkHash(value: String, hash: String): Flow<Resource<Boolean>> =
        object : NetworkBoundResource<Boolean, Boolean>() {
            override suspend fun load(data: Boolean): Flow<Boolean> =
                listOf(data).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<Boolean>> =
                remoteDataSource.checkHash(value, hash)

            override suspend fun saveCallResult(data: Boolean) {}

        }.asFlow()

    // -- PROVINCE, CITY, DISTRICS --
    override fun getProvinces(): Flow<Resource<List<ProvinceDomain>>> =
        object : NetworkBoundResource<List<ProvinceDomain>, List<ProvinceResponse>>() {
            override suspend fun load(data: List<ProvinceResponse>): Flow<List<ProvinceDomain>> =
                listOf(DataMapper.provinceResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<ProvinceResponse>>> =
                remoteDataSource.getProvinces()

            override suspend fun saveCallResult(data: List<ProvinceResponse>) {}

        }.asFlow()

    override fun getCities(idProvince: String): Flow<Resource<List<CityDomain>>> =
        object : NetworkBoundResource<List<CityDomain>, List<CityResponse>>() {
            override suspend fun load(data: List<CityResponse>): Flow<List<CityDomain>> =
                listOf(DataMapper.cityResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<CityResponse>>> =
                remoteDataSource.getCities(idProvince)

            override suspend fun saveCallResult(data: List<CityResponse>) {}

        }.asFlow()

    override fun getDistricts(idCity: String): Flow<Resource<List<DistricsDomain>>> =
        object : NetworkBoundResource<List<DistricsDomain>, List<DistricsResponse>>() {
            override suspend fun load(data: List<DistricsResponse>): Flow<List<DistricsDomain>> =
                listOf(DataMapper.districsResponseToDomain(data)).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<List<DistricsResponse>>> =
                remoteDataSource.getDistrics(idCity)

            override suspend fun saveCallResult(data: List<DistricsResponse>) {}

        }.asFlow()

    override fun getToken(): Flow<Resource<String>> =
        object : NetworkBoundResource<String, String>() {
            override suspend fun load(data: String): Flow<String> =
                listOf(data).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<String>> =
                remoteDataSource.getToken()

            override suspend fun saveCallResult(data: String) {}

        }.asFlow()

    override fun deleteToken(): Flow<Resource<Boolean>> =
        object : NetworkBoundResource<Boolean, Boolean>() {
            override suspend fun load(data: Boolean): Flow<Boolean> =
                listOf(data).asFlow()

            override suspend fun createCall(): Flow<ApiResponse<Boolean>> =
                remoteDataSource.deleteToken()

            override suspend fun saveCallResult(data: Boolean) {}

        }.asFlow()
}