package com.arafat1419.mengantri_app.core.data.remote

import com.arafat1419.mengantri_app.core.data.remote.api.ApiService
import com.arafat1419.mengantri_app.core.data.remote.response.*
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.CityResponse
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.DistricsResponse
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.ProvinceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RemoteDataSource(private val apiService: ApiService) {

    // -- CATEGORY --
    suspend fun getCategories(): Flow<ApiResponse<List<CategoryResponse>>> =
        flow {
            try {
                val response = apiService.getCategories()

                if (response.result != null) {
                    emit(ApiResponse.Success(response.result))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    // -- COMPANY --
    suspend fun getNewestCompanies(): Flow<ApiResponse<List<CompanyResponse>>> =
        flow {
            try {
                val response = apiService.getNewestCompanies()

                if (response.result != null) {
                    emit(ApiResponse.Success(response.result))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getCustomerCompany(customerId: Int): Flow<ApiResponse<List<CompanyResponse>>> =
        flow {
            try {
                val response = apiService.getCustomerCompany(customerId)

                if (response.result != null) {
                    emit(ApiResponse.Success(response.result))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getCompaniesByCategory(categoryId: Int): Flow<ApiResponse<List<CompanyResponse>>> =
        flow {
            try {
                val response = apiService.getCompaniesByCategory(categoryId = categoryId)

                if (response.result != null) {
                    emit(ApiResponse.Success(response.result))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getCompany(companyId: Int): Flow<ApiResponse<CompanyResponse>> =
        flow {
            try {
                val response = apiService.getCompany(companyId)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getSearchCompanies(keyword: String): Flow<ApiResponse<List<CompanyResponse>>> =
        flow {
            try {
                val response = apiService.getSearchCompanies(keyword = keyword)

                if (response.result != null) {
                    emit(ApiResponse.Success(response.result))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun postCompany(companyResponse: CompanyResponse): Flow<ApiResponse<CompanyResponse>> {
        return flow {
            try {
                val response = apiService.postCompany(companyResponse)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateCompany(
        companyId: Int,
        companyResponse: CompanyResponse
    ): Flow<ApiResponse<CompanyResponse>> {
        return flow {
            try {
                val response = apiService.updateCompany(companyId, companyResponse)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    // -- SERVICE --
    suspend fun getServicesCountByCompany(companyId: Int): Flow<ApiResponse<List<ServiceCountResponse>>> =
        flow {
            try {
                val response = apiService.getServicesCountByCompany(companyId)

                if (response.result != null) {
                    emit(ApiResponse.Success(response.result))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getServiceCount(
        serviceId: Int,
        ticketDate: String?
    ): Flow<ApiResponse<ServiceCountResponse>> =
        flow {
            try {
                val response = apiService.getServiceCount(serviceId, ticketDate)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getServiceEstimated(
        serviceId: Int,
        ticketDate: String
    ): Flow<ApiResponse<EstimatedTimeResponse>> =
        flow {
            try {
                val response = apiService.getServiceEstimated(serviceId, ticketDate)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getSearchServices(keyword: String): Flow<ApiResponse<List<ServiceCountResponse>>> =
        flow {
            try {
                val response = apiService.getSearchServices(keyword)

                if (response.result != null) {
                    emit(ApiResponse.Success(response.result))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getServicesByCompany(companyId: Int): Flow<ApiResponse<List<ServiceResponse>>> =
        flow {
            try {
                val response = apiService.getServicesByCompany(companyId)

                if (response.result != null) {
                    emit(ApiResponse.Success(response.result))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getService(serviceId: Int): Flow<ApiResponse<ServiceResponse>> {
        return flow {
            try {
                val response = apiService.getService(serviceId)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postService(serviceResponse: ServiceResponse): Flow<ApiResponse<ServiceResponse>> {
        return flow {
            try {
                val response = apiService.postService(serviceResponse)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateService(
        serviceId: Int,
        serviceResponse: ServiceResponse
    ): Flow<ApiResponse<ServiceResponse>> {
        return flow {
            try {
                val response = apiService.updateService(serviceId, serviceResponse)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun deleteService(
        serviceId: Int
    ): Flow<ApiResponse<Boolean>> {
        return flow {
            try {
                val response = apiService.deleteService(serviceId)
                emit(ApiResponse.Success(response.isSuccessful))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    // -- TICKET --
    suspend fun getTicketServiceDetail(ticketId: Int): Flow<ApiResponse<TicketDetailResponse>> =
        flow {
            try {
                val response = apiService.getTicketServiceDetail(ticketId)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getTicketsWaiting(customerId: Int): Flow<ApiResponse<List<TicketDetailResponse>>> =
        flow {
            try {
                val response = apiService.getTicketsWaiting(customerId)

                if (response.result != null) {
                    emit(ApiResponse.Success(response.result))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getTicketsHistory(
        customerId: Int?,
        serviceId: Int?
    ): Flow<ApiResponse<List<TicketDetailResponse>>> =
        flow {
            try {
                val response = apiService.getTicketsHistory(customerId, serviceId)

                if (response.result != null) {
                    emit(ApiResponse.Success(response.result))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getTicketsToday(serviceId: Int?): Flow<ApiResponse<List<TicketDetailResponse>>> =
        flow {
            try {
                val response = apiService.getTicketsToday(serviceId)

                if (response.result != null) {
                    emit(ApiResponse.Success(response.result))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getTicketsSoon(serviceId: Int?): Flow<ApiResponse<List<TicketDetailResponse>>> =
        flow {
            try {
                val response = apiService.getTicketsSoon(serviceId)

                if (response.result != null) {
                    emit(ApiResponse.Success(response.result))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun postTicket(ticketResponse: TicketResponse): Flow<ApiResponse<TicketResponse>> {
        return flow {
            try {
                val response = apiService.postTicket(ticketResponse)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateTicket(
        ticketId: Int,
        ticketResponse: TicketResponse
    ): Flow<ApiResponse<TicketResponse>> {
        return flow {
            try {
                val response = apiService.updateTicket(ticketId, ticketResponse)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    // -- CUSTOMER --
    suspend fun getCustomer(customerId: Int): Flow<ApiResponse<CustomerResponse>> = flow {
        try {
            val response = apiService.getCustomer(customerId)

            emit(ApiResponse.Success(response.data))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getLogin(customerEmail: String): Flow<ApiResponse<List<CustomerResponse>>> {
        return flow {
            try {

                val response = apiService.getLogin(customerEmail)

                if (response.result != null) {
                    emit(ApiResponse.Success(response.result))
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

    suspend fun patchCustomer(
        customerId: Int,
        customerResponse: CustomerResponse
    ): Flow<ApiResponse<CustomerResponse>> {
        return flow {
            try {
                val response = apiService.patchCustomer(customerId, customerResponse)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    // -- FILES --
    suspend fun postUploadFile(
        fileName: String,
        isBanner: Boolean,
        file: File
    ): Flow<ApiResponse<UploadFileResponse>> {
        return flow {
            try {
                val bannerFolder = "cd600aaa-6e43-4d13-b832-469f29a9f5a4"
                val logoFolder = "0f0080e2-6c83-45eb-964e-f1c969f2b40d"

                val folder = if (isBanner) bannerFolder else logoFolder

                val requestFileName =
                    fileName.toRequestBody(MultipartBody.FORM)

                val requestFolder =
                    folder.toRequestBody(MultipartBody.FORM)

                val requestBody = MultipartBody.Part.createFormData(
                    "",
                    file.name,
                    file.asRequestBody("image/jpeg".toMediaType())
                )

                val response =
                    apiService.postUploadFile(requestFileName, requestFolder, requestBody)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }


    // -- UTILS --
    suspend fun checkHash(value: String, hash: String): Flow<ApiResponse<Boolean>> {
        return flow {
            try {
                val rawMap = HashMap<String, String>()
                rawMap["string"] = value
                rawMap["hash"] = hash

                val response = apiService.checkHash(rawMap)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    // -- PROVINCE, CITY, DISTRICS --
    suspend fun getProvinces(): Flow<ApiResponse<List<ProvinceResponse>>> {
        return flow {
            try {
                val url = "https://dev.farizdotid.com/api/daerahindonesia/provinsi"
                val response = apiService.getProvinces(url)

                emit(ApiResponse.Success(response.result))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCities(idProvince: String): Flow<ApiResponse<List<CityResponse>>> {
        return flow {
            try {
                val url = "https://dev.farizdotid.com/api/daerahindonesia/kota"
                val response = apiService.getCities(url, idProvince)

                emit(ApiResponse.Success(response.result))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDistrics(idCity: String): Flow<ApiResponse<List<DistricsResponse>>> {
        return flow {
            try {
                val url = "https://dev.farizdotid.com/api/daerahindonesia/kecamatan"
                val response = apiService.getDistrics(url, idCity)

                emit(ApiResponse.Success(response.result))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}