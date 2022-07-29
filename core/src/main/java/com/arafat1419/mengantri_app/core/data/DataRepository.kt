package com.arafat1419.mengantri_app.core.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

class DataRepository(private val remoteDataSource: RemoteDataSource) : IDataRepository {

    // -- CATEGORY --
    override fun getCategories(): Flow<List<CategoryDomain>> {
        val data = MutableLiveData<List<CategoryResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getCategories().collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> data.postValue(response.data)
                }
            }
        }
        return data.map {
            DataMapper.categoryResponseToDomain(it!!)
        }.asFlow()
    }

    // -- COMPANY --
    override fun getNewestCompanies(): Flow<List<CompanyDomain>> {
        val data = MutableLiveData<List<CompanyResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getNewestCompanies().collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> data.postValue(response.data)
                }
            }
        }
        return data.map {
            DataMapper.companyResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getCustomerCompany(customerId: Int): Flow<List<CompanyDomain>> {
        val data = MutableLiveData<List<CompanyResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getCustomerCompany(customerId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> data.postValue(response.data)
                }
            }
        }
        return data.map {
            DataMapper.companyResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getCompaniesByCategory(categoryId: Int): Flow<List<CompanyDomain>> {
        val data = MutableLiveData<List<CompanyResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getCompaniesByCategory(categoryId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> data.postValue(response.data)
                }
            }
        }
        return data.map {
            DataMapper.companyResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getCompany(companyId: Int): Flow<CompanyDomain> {
        val data = MutableLiveData<CompanyResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getCompany(companyId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(CompanyResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> data.postValue(response.data)
                }
            }
        }
        return data.map {
            DataMapper.companyResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getSearchCompanies(keyword: String): Flow<List<CompanyDomain>> {
        val data = MutableLiveData<List<CompanyResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getSearchCompanies(keyword).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> data.postValue(response.data)

                }
            }
        }
        return data.map {
            DataMapper.companyResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getServicesCountByCompany(companyId: Int): Flow<List<ServiceCountDomain>> {
        val data = MutableLiveData<List<ServiceCountResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getServicesCountByCompany(companyId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> data.postValue(response.data)

                }
            }
        }
        return data.map {
            DataMapper.serviceCountResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getServiceCount(serviceId: Int): Flow<ServiceCountDomain> {
        val data = MutableLiveData<ServiceCountResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getServiceCount(serviceId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(ServiceCountResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> data.postValue(response.data)

                }
            }
        }
        return data.map {
            DataMapper.serviceCountResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getServiceEstimated(serviceId: Int): Flow<String?> {
        val data = MutableLiveData<EstimatedTimeResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getServiceEstimated(serviceId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(EstimatedTimeResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> data.postValue(response.data)

                }
            }
        }
        return data.map {
            it.estimatedTime
        }.asFlow()
    }

    override fun getSearchServices(keyword: String): Flow<List<ServiceCountDomain>> {
        val data = MutableLiveData<List<ServiceCountResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getSearchServices(keyword).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> data.postValue(response.data)

                }
            }
        }
        return data.map {
            DataMapper.serviceCountResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getTicketServiceDetail(ticketId: Int): Flow<TicketDetailDomain> {
        val data = MutableLiveData<TicketDetailResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTicketServiceDetail(ticketId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(TicketDetailResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> data.postValue(response.data)

                }
            }
        }
        return data.map {
            DataMapper.ticketDetailResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getTicketsWaiting(customerId: Int): Flow<List<TicketDetailDomain>> {
        val data = MutableLiveData<List<TicketDetailResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTicketsWaiting(customerId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> data.postValue(response.data)

                }
            }
        }
        return data.map {
            DataMapper.ticketDetailResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getTicketsHistory(customerId: Int): Flow<List<TicketDetailDomain>> {
        val data = MutableLiveData<List<TicketDetailResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTicketsHistory(customerId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> data.postValue(response.data)

                }
            }
        }
        return data.map {
            DataMapper.ticketDetailResponseToDomain(it!!)
        }.asFlow()
    }

    // -- LOGIN DOMAIN --
    override fun getLogin(customerEmail: String): Flow<List<CustomerDomain>> {
        val data = MutableLiveData<List<CustomerResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getLogin(customerEmail).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.customerResponseToDomain(it!!)
        }.asFlow()
    }

    override fun checkHash(value: String, hash: String): Flow<Boolean> {
        val data = MutableLiveData<Boolean>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.checkHash(value, hash).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(false)
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data!!)
                    }
                }
            }
        }
        return data.asFlow()
    }

    override fun postRegistration(customerResponse: CustomerResponse): Flow<CustomerDomain> {
        val data = MutableLiveData<CustomerResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.postRegistration(customerResponse).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(CustomerResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data!!)
                    }
                }
            }
        }
        return data.map {
            DataMapper.customerResponseToDomain(it)
        }.asFlow()
    }

    override fun patchCustomer(
        customerId: Int,
        customerResponse: CustomerResponse
    ): Flow<CustomerDomain> {
        val data = MutableLiveData<CustomerResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.patchCustomer(customerId, customerResponse).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(CustomerResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data!!)
                    }
                }
            }
        }
        return data.map {
            DataMapper.customerResponseToDomain(it)
        }.asFlow()
    }

    // -- HOME DOMAIN --

    override fun getSearchCompaniesByCategory(
        keyword: String,
        categoryId: Int
    ): Flow<List<CompanyDomain>> {
        val data = MutableLiveData<List<CompanyResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getSearchCompaniesByCategory(keyword, categoryId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.companyResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getServices(companyId: Int): Flow<List<ServiceDomain>> {
        val data = MutableLiveData<List<ServiceResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getServices(companyId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.serviceResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getTickets(serviceId: Int, ticketDate: String?): Flow<List<TicketDomain>> {
        val data = MutableLiveData<List<TicketResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTickets(serviceId, ticketDate).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.ticketResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getTicketServed(serviceId: Int): Flow<Int> {
        val data = MutableLiveData<CountResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTicketServed(serviceId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(CountResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data!!)
                    }
                }
            }
        }
        return data.map {
            it.filterCount!!.toInt()
        }.asFlow()
    }

    override fun getServicesAndServed(companyId: Int): Flow<List<ServiceCountDomain>> {
        val data = MutableLiveData<List<ServiceCountResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getServicesAndServed(companyId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.serviceCountResponseToDomain(it!!)
        }.asFlow()
    }

    override fun postTicket(ticketResponse: TicketResponse): Flow<TicketDomain> {
        val data = MutableLiveData<TicketResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.postTicket(ticketResponse).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(TicketResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data!!)
                    }
                }
            }
        }
        return data.map {
            DataMapper.ticketResponseToDomain(it)
        }.asFlow()
    }

    override fun getTicket(ticketId: Int): Flow<List<TicketServiceDomain>> {
        val data = MutableLiveData<List<TicketServiceResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTicket(ticketId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.ticketServiceResponseToDomain(it!!)
        }.asFlow()
    }

    override fun updateTicket(ticketId: Int, ticketResponse: TicketResponse): Flow<TicketDomain> {
        val data = MutableLiveData<TicketResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.updateTicket(ticketId, ticketResponse).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(TicketResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data!!)
                    }
                }
            }
        }
        return data.map {
            DataMapper.ticketResponseToDomain(it)
        }.asFlow()
    }

    override fun getServiceXDay(serviceId: Int, dayId: Int): Flow<List<ServiceXDayDomain>> {
        val data = MutableLiveData<List<ServiceXDayResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getServiceXDay(serviceId, dayId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.serviceXDayResponseToDomain(it!!)
        }.asFlow()
    }

    // -- TICKET DOMAIN --
    override fun getTicketByStatus(
        customerId: Int,
        ticketStatus: String
    ): Flow<List<TicketServiceDomain>> {
        val data = MutableLiveData<List<TicketServiceResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTicketByStatus(customerId, ticketStatus).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.ticketServiceResponseToDomain(it!!)
        }.asFlow()
    }

    // -- COMPANY DOMAIN --
    override fun getUserCompany(customerId: Int): Flow<List<CompanyDomain>> {
        val data = MutableLiveData<List<CompanyResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getUserCompany(customerId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.companyResponseToDomain(it!!)
        }.asFlow()
    }

    override fun postUploadFile(
        fileName: String, isBanner: Boolean, file: File
    ): Flow<UploadFileDomain> {
        val data = MutableLiveData<UploadFileResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.postUploadFile(fileName, isBanner, file).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(UploadFileResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data!!)
                    }
                }
            }
        }
        return data.map {
            UploadFileDomain(it.fileId)
        }.asFlow()
    }

    override fun postCompany(companyDomain: CompanyDomain): Flow<CompanyDomain> {
        val data = MutableLiveData<CompanyResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.postCompany(DataMapper.companyDomainToResponse(companyDomain))
                .collect { response ->
                    when (response) {
                        is ApiResponse.Empty -> data.postValue(CompanyResponse())
                        is ApiResponse.Error -> response.errorMessage
                        is ApiResponse.Success -> {
                            data.postValue(response.data!!)
                        }
                    }
                }
        }
        return data.map {
            DataMapper.companyResponseToDomain(it)
        }.asFlow()
    }

    override fun getTicketsSoon(serviceId: Int): Flow<List<TicketDomain>> {
        val data = MutableLiveData<List<TicketResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTicketsSoon(serviceId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.ticketResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getTicketsByService(serviceId: Int): Flow<List<TicketDomain>> {
        val data = MutableLiveData<List<TicketResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTicketsByService(serviceId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.ticketResponseToDomain(it!!)
        }.asFlow()
    }

    override fun postService(serviceOnlyDomain: ServiceOnlyDomain): Flow<ServiceOnlyDomain> {
        val data = MutableLiveData<ServiceOnlyResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.postService(DataMapper.serviceOnlyDomainToResponse(serviceOnlyDomain))
                .collect { response ->
                    when (response) {
                        is ApiResponse.Empty -> data.postValue(ServiceOnlyResponse())
                        is ApiResponse.Error -> response.errorMessage
                        is ApiResponse.Success -> {
                            data.postValue(response.data!!)
                        }
                    }
                }
        }
        return data.map {
            DataMapper.serviceOnlyResponseToDomain(it)
        }.asFlow()
    }

    override fun updateService(
        serviceId: Int,
        serviceOnlyResponse: ServiceOnlyResponse
    ): Flow<ServiceOnlyDomain> {
        val data = MutableLiveData<ServiceOnlyResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.updateService(serviceId, serviceOnlyResponse).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(ServiceOnlyResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data!!)
                    }
                }
            }
        }
        return data.map {
            DataMapper.serviceOnlyResponseToDomain(it)
        }.asFlow()
    }

    // -- PROVINCE, CITY, DISTRICS --
    override fun getProvinces(): Flow<List<ProvinceDomain>> {
        val data = MutableLiveData<List<ProvinceResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getProvinces().collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.provinceResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getCities(idProvince: String): Flow<List<CityDomain>> {
        val data = MutableLiveData<List<CityResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getCities(idProvince).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.cityResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getDistrics(idCity: String): Flow<List<DistricsDomain>> {
        val data = MutableLiveData<List<DistricsResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getDistrics(idCity).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.districsResponseToDomain(it!!)
        }.asFlow()
    }
}