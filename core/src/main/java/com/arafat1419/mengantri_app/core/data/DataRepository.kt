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

    override fun postCompany(companyDomain: CompanyDomain): Flow<CompanyDomain> {
        val data = MutableLiveData<CompanyResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.postCompany(DataMapper.companyDomainToResponse(companyDomain))
                .collect { response ->
                    when (response) {
                        is ApiResponse.Empty -> data.postValue(CompanyResponse())
                        is ApiResponse.Error -> response.errorMessage
                        is ApiResponse.Success -> {
                            data.postValue(response.data)
                        }
                    }
                }
        }
        return data.map {
            DataMapper.companyResponseToDomain(it)
        }.asFlow()
    }

    override fun updateCompany(companyId: Int, companyDomain: CompanyDomain): Flow<CompanyDomain> {
        val data = MutableLiveData<CompanyResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.updateCompany(
                companyId,
                DataMapper.companyDomainToResponse(companyDomain)
            )
                .collect { response ->
                    when (response) {
                        is ApiResponse.Empty -> data.postValue(CompanyResponse())
                        is ApiResponse.Error -> response.errorMessage
                        is ApiResponse.Success -> {
                            data.postValue(response.data)
                        }
                    }
                }
        }
        return data.map {
            DataMapper.companyResponseToDomain(it)
        }.asFlow()
    }

    // -- SERVICE --
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

    override fun getServiceCount(serviceId: Int, ticketDate: String?): Flow<ServiceCountDomain> {
        val data = MutableLiveData<ServiceCountResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getServiceCount(serviceId, ticketDate).collect { response ->
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

    override fun getServiceEstimated(
        serviceId: Int,
        ticketDate: String
    ): Flow<EstimatedTimeDomain?> {
        val data = MutableLiveData<EstimatedTimeResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getServiceEstimated(serviceId, ticketDate).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(EstimatedTimeResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> data.postValue(response.data)

                }
            }
        }
        return data.map {
            EstimatedTimeDomain(it.estimatedTime, it.isAvailable)
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

    override fun getServicesByCompany(companyId: Int): Flow<List<ServiceDomain>> {
        val data = MutableLiveData<List<ServiceResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getServicesByCompany(companyId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> data.postValue(response.data)

                }
            }
        }
        return data.map {
            DataMapper.serviceResponseToDomain(it!!)
        }.asFlow()
    }

    override fun postService(serviceDomain: ServiceDomain): Flow<ServiceDomain> {
        val data = MutableLiveData<ServiceResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.postService(DataMapper.serviceDomainToResponse(serviceDomain))
                .collect { response ->
                    when (response) {
                        is ApiResponse.Empty -> data.postValue(ServiceResponse())
                        is ApiResponse.Error -> response.errorMessage
                        is ApiResponse.Success -> {
                            data.postValue(response.data)
                        }
                    }
                }
        }
        return data.map {
            DataMapper.serviceResponseToDomain(it)
        }.asFlow()
    }

    override fun getService(serviceId: Int): Flow<ServiceDomain> {
        val data = MutableLiveData<ServiceResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getService(serviceId)
                .collect { response ->
                    when (response) {
                        is ApiResponse.Empty -> data.postValue(ServiceResponse())
                        is ApiResponse.Error -> response.errorMessage
                        is ApiResponse.Success -> {
                            data.postValue(response.data)
                        }
                    }
                }
        }
        return data.map {
            DataMapper.serviceResponseToDomain(it)
        }.asFlow()
    }

    override fun updateService(serviceId: Int, serviceDomain: ServiceDomain): Flow<ServiceDomain> {
        val data = MutableLiveData<ServiceResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.updateService(
                serviceId,
                DataMapper.serviceDomainToResponse(serviceDomain)
            ).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(ServiceResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.serviceResponseToDomain(it)
        }.asFlow()
    }

    override fun deleteService(serviceId: Int): Flow<Boolean> {
        val data = MutableLiveData<Boolean>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.deleteService(serviceId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(false)
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.asFlow()
    }

    // -- TICKET --
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

    override fun getTicketsHistory(
        customerId: Int?,
        serviceId: Int?
    ): Flow<List<TicketDetailDomain>> {
        val data = MutableLiveData<List<TicketDetailResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTicketsHistory(customerId, serviceId).collect { response ->
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

    override fun getTicketsToday(serviceId: Int?): Flow<List<TicketDetailDomain>> {
        val data = MutableLiveData<List<TicketDetailResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTicketsToday(serviceId).collect { response ->
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

    override fun getTicketsSoon(serviceId: Int?): Flow<List<TicketDetailDomain>> {
        val data = MutableLiveData<List<TicketDetailResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTicketsSoon(serviceId).collect { response ->
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

    override fun postTicket(ticketResponse: TicketResponse): Flow<TicketDomain> {
        val data = MutableLiveData<TicketResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.postTicket(ticketResponse).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(TicketResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.ticketResponseToDomain(it)
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
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.ticketResponseToDomain(it)
        }.asFlow()
    }

    // -- CUSTOMER --

    override fun getCustomer(customerId: Int): Flow<CustomerDomain> {
        val data = MutableLiveData<CustomerResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getCustomer(customerId).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(CustomerResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.customerResponseToDomain(it)
        }.asFlow()
    }

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

    override fun postRegistration(customerResponse: CustomerResponse): Flow<CustomerDomain> {
        val data = MutableLiveData<CustomerResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.postRegistration(customerResponse).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(CustomerResponse())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
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
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            DataMapper.customerResponseToDomain(it)
        }.asFlow()
    }

    // -- FILES --
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
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.map {
            UploadFileDomain(it.fileId)
        }.asFlow()
    }

    // -- UTILS --
    override fun checkHash(value: String, hash: String): Flow<Boolean> {
        val data = MutableLiveData<Boolean>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.checkHash(value, hash).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> data.postValue(false)
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> {
                        data.postValue(response.data)
                    }
                }
            }
        }
        return data.asFlow()
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