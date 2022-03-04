package com.arafat1419.mengantri_app.core.data.remote

import com.arafat1419.mengantri_app.core.data.remote.api.ApiService
import com.arafat1419.mengantri_app.core.data.remote.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {

    // -- LOGIN DOMAIN --
    suspend fun getLogin(customerEmail: String): Flow<ApiResponse<List<CustomerResponse>>> {
        return flow {
            try {

                val response = apiService.getLogin(customerEmail = customerEmail)
                val listResponse = response.result
                if (listResponse != null) {
                    if (listResponse.isNotEmpty()) {
                        emit(ApiResponse.Success(response.result))
                    } else {
                        emit(ApiResponse.Empty)
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postRegistration(customerResponse: CustomerResponse): Flow<ApiResponse<CustomerResponse>> {
        return flow {
            try {
                val response = apiService.postRegistration(customerResponse)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    // -- HOME DOMAIN --
    suspend fun getCategories(): Flow<ApiResponse<List<CategoryResponse>>> {
        return flow {
            try {

                val response = apiService.getCategories()
                val listResponse = response.result
                if (listResponse != null) {
                    if (listResponse.isNotEmpty()) {
                        emit(ApiResponse.Success(response.result))
                    } else {
                        emit(ApiResponse.Empty)
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCompanies(categoryId: Int): Flow<ApiResponse<List<CompanyResponse>>> {
        return flow {
            try {

                val response = apiService.getCompanies(categoryId = categoryId)
                val listResponse = response.result
                if (listResponse != null) {
                    if (listResponse.isNotEmpty()) {
                        emit(ApiResponse.Success(response.result))
                    } else {
                        emit(ApiResponse.Empty)
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getServices(companyId: Int): Flow<ApiResponse<List<ServiceResponse>>> {
        return flow {
            try {

                val response = apiService.getServices(companyId = companyId)
                val listResponse = response.result
                if (listResponse != null) {
                    if (listResponse.isNotEmpty()) {
                        emit(ApiResponse.Success(response.result))
                    } else {
                        emit(ApiResponse.Empty)
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}