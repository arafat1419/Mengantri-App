package com.arafat1419.mengantri_app.core.data.remote

import com.arafat1419.mengantri_app.core.data.remote.api.ApiService
import com.arafat1419.mengantri_app.core.data.remote.response.*
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.CityResponse
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.DistricsResponse
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.ProvinceResponse
import com.arafat1419.mengantri_app.core.utils.StatusHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class RemoteDataSource(private val apiService: ApiService) {

    // -- LOGIN DOMAIN --
    suspend fun getLogin(customerEmail: String): Flow<ApiResponse<List<CustomerResponse>>> {
        return flow {
            try {

                val response = apiService.getLogin(customerEmail)
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

    suspend fun getSearchCompanies(keyword: String): Flow<ApiResponse<List<CompanyResponse>>> {
        return flow {
            try {

                val response = apiService.getSearchCompanies(keyword = keyword)
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

    suspend fun getSearchCompaniesByCategory(keyword: String, categoryId: Int): Flow<ApiResponse<List<CompanyResponse>>> {
        return flow {
            try {

                val response = apiService.getSearchCompaniesByCategory(keyword, categoryId)
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

    suspend fun getTickets(serviceId: Int): Flow<ApiResponse<List<TicketResponse>>> {
        return flow {
            try {
                val df: DateFormat =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                val currentDate: String = df.format(Date())

                val response = apiService.getTickets(serviceId, currentDate)
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

    suspend fun getTicketServed(serviceId: Int): Flow<ApiResponse<CountResponse>> {
        return flow {
            try {
                val df: DateFormat =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                val currentDate: String = df.format(Date())
                val fields = "ticket_id"
                val response = apiService.getTicketServed(serviceId, currentDate, fields)

                emit(ApiResponse.Success(response.meta))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getServicesAndServed(companyId: Int): Flow<ApiResponse<List<ServiceCountResponse>>> {
        return flow {
            try {
                val data = mutableListOf<ServiceCountResponse>()

                val df: DateFormat =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDate: String = df.format(Date())

                val servicesResponse = apiService.getServices(companyId = companyId)
                val filterServiceStatus = servicesResponse.result?.filter {
                    it.serviceStatus == 1
                }
                filterServiceStatus?.forEach { service ->
                    var served = 0
                    var waiting = 0
                    var cancel = 0

                    val response = apiService.getTickets(service.serviceId!!, currentDate)
                    response.result?.forEach { ticket ->
                        when (ticket.ticketStatus) {
                            StatusHelper.TICKET_SUCCESS -> served++
                            StatusHelper.TICKET_WAITING -> waiting++
                            StatusHelper.TICKET_CANCEL -> cancel++
                        }
                    }
                    val total = response.result?.size!!
                    data.add(
                        ServiceCountResponse(
                            service,
                            total,
                            served,
                            waiting,
                            cancel
                        )
                    )
                }

                emit(ApiResponse.Success(data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

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

    suspend fun getTicket(ticketId: Int): Flow<ApiResponse<List<TicketWithServiceResponse>>> {
        return flow {
            try {
                val response = apiService.getTicket(ticketId)
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

    suspend fun getServiceXDay(
        serviceId: Int,
        dayId: Int
    ): Flow<ApiResponse<List<ServiceXDayResponse>>> {
        return flow {
            try {
                val response = apiService.getServiceXDay(serviceId, dayId)
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

    // -- TICKET MODULE --
    suspend fun getTicketByStatus(
        customerId: Int,
        ticketStatus: String
    ): Flow<ApiResponse<List<TicketWithServiceResponse>>> {
        return flow {
            try {
                val response = apiService.getTicketByStatus(customerId, ticketStatus)
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

    // -- COMPANY MODULE --
    suspend fun getUserCompany(customerId: Int): Flow<ApiResponse<List<CompanyResponse>>> {
        return flow {
            try {

                val response = apiService.getUserCompany(customerId)
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

                val response = apiService.postUploadFile(requestFileName, requestFolder, requestBody)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

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

    suspend fun getTicketsSoon(serviceId: Int): Flow<ApiResponse<List<TicketResponse>>> {
        return flow {
            try {
                val df: DateFormat =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                val currentDate: String = df.format(Date())

                val response = apiService.getTicketsSoon(serviceId, currentDate)
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

    suspend fun getTicketsByService(serviceId: Int): Flow<ApiResponse<List<TicketResponse>>> {
        return flow {
            try {

                val response = apiService.getTicketsByService(serviceId)
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

    suspend fun postService(serviceOnlyResponse: ServiceOnlyResponse): Flow<ApiResponse<ServiceOnlyResponse>> {
        return flow {
            try {
                val response = apiService.postService(serviceOnlyResponse)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateService(serviceId: Int, serviceOnlyResponse: ServiceOnlyResponse): Flow<ApiResponse<ServiceOnlyResponse>> {
        return flow {
            try {
                val response = apiService.updateService(serviceId, serviceOnlyResponse)

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
                val listResponse = response.result
                if (listResponse.isNotEmpty()) {
                    emit(ApiResponse.Success(response.result))
                } else {
                    emit(ApiResponse.Empty)
                }

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
                val listResponse = response.result
                if (listResponse.isNotEmpty()) {
                    emit(ApiResponse.Success(response.result))
                } else {
                    emit(ApiResponse.Empty)
                }

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
                val listResponse = response.result
                if (listResponse.isNotEmpty()) {
                    emit(ApiResponse.Success(response.result))
                } else {
                    emit(ApiResponse.Empty)
                }

            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}