package com.arafat1419.mengantri_app.core.data

import com.arafat1419.mengantri_app.core.data.remote.response.ApiResponse
import com.arafat1419.mengantri_app.core.vo.Resource
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType> {
    private var result: Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading())
        when (val apiResponse = createCall().first()) {
            is ApiResponse.Success -> {
                saveCallResult(apiResponse.data)
                emitAll(load(apiResponse.data).map { Resource.Success(it) })
            }
            is ApiResponse.Error -> {
                onFetchFailed()
                emit(Resource.Error(apiResponse.errorMessage))
            }
            else -> {}
        }
    }

    protected open fun onFetchFailed() {}

    protected abstract suspend fun load(data: RequestType): Flow<ResultType>

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow(): Flow<Resource<ResultType>> = result
}