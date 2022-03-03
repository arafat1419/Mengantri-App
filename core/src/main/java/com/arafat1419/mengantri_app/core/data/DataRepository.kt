package com.arafat1419.mengantri_app.core.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import com.arafat1419.mengantri_app.core.data.remote.RemoteDataSource
import com.arafat1419.mengantri_app.core.data.remote.response.ApiResponse
import com.arafat1419.mengantri_app.core.data.remote.response.CategoryResponse
import com.arafat1419.mengantri_app.core.data.remote.response.CustomerResponse
import com.arafat1419.mengantri_app.core.domain.model.CategoryDomain
import com.arafat1419.mengantri_app.core.domain.model.CustomerDomain
import com.arafat1419.mengantri_app.core.domain.repository.IDataRepository
import com.arafat1419.mengantri_app.core.utils.DataMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DataRepository(private val remoteDataSource: RemoteDataSource): IDataRepository {
    // -- LOGIN DOMAIN --
    override fun getLogin(customerEmail: String): Flow<List<CustomerDomain>> {
        val data = MutableLiveData<List<CustomerResponse>?>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getLogin(customerEmail).collect{ response ->
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
            remoteDataSource.getCategories().collect{ response ->
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
}