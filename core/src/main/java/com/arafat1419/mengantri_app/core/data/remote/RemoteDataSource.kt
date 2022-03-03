package com.arafat1419.mengantri_app.core.data.remote

import com.arafat1419.mengantri_app.core.data.remote.api.ApiService
import com.arafat1419.mengantri_app.core.data.remote.response.ApiResponse
import com.arafat1419.mengantri_app.core.data.remote.response.CustomerResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getLogin(customerEmail: String): Flow<ApiResponse<List<CustomerResponse>>> {
        return flow {
            try {

                val response = apiService.getLogin(customerEmail = customerEmail)
                val customerList = response.result
                if (customerList != null) {
                    if (customerList.isNotEmpty()) {
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