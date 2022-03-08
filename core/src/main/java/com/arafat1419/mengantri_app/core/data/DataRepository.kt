package com.arafat1419.mengantri_app.core.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import com.arafat1419.mengantri_app.core.data.remote.RemoteDataSource
import com.arafat1419.mengantri_app.core.data.remote.response.*
import com.arafat1419.mengantri_app.core.domain.model.*
import com.arafat1419.mengantri_app.core.domain.repository.IDataRepository
import com.arafat1419.mengantri_app.core.utils.DataMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DataRepository(private val remoteDataSource: RemoteDataSource) : IDataRepository {
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

    // -- HOME DOMAIN --
    override fun getCategories(): Flow<List<CategoryDomain>> {
        val data = MutableLiveData<List<CategoryResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getCategories().collect { response ->
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
            DataMapper.categoryResponseToDomain(it!!)
        }.asFlow()
    }

    override fun getCompanies(categoryId: Int): Flow<List<CompanyDomain>> {
        val data = MutableLiveData<List<CompanyResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getCompanies(categoryId).collect { response ->
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

    override fun getTickets(serviceId: Int): Flow<List<TicketDomain>> {
        val data = MutableLiveData<List<TicketResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTickets(serviceId).collect { response ->
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

    override fun getTicket(ticketId: Int): Flow<List<TicketWithServiceDomain>> {
        val data = MutableLiveData<List<TicketWithServiceResponse>?>()
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
            DataMapper.ticketWithServiceResponseToDomain(it!!)
        }.asFlow()
    }

    override fun updateTicket(ticketId: Int, status: String): Flow<TicketDomain> {
        val data = MutableLiveData<TicketResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.updateTicket(ticketId, status).collect { response ->
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
}