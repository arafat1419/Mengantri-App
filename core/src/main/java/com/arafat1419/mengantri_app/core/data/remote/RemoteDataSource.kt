package com.arafat1419.mengantri_app.core.data.remote

import com.arafat1419.mengantri_app.core.data.remote.api.ApiService
import com.arafat1419.mengantri_app.core.data.remote.response.*
import com.arafat1419.mengantri_app.core.utils.StatusHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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

    suspend fun patchCustomer(customerId: Int, customerResponse: CustomerResponse): Flow<ApiResponse<CustomerResponse>> {
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
                servicesResponse.result?.forEach { service ->
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

    suspend fun updateTicket(ticketId: Int, ticketResponse: TicketResponse): Flow<ApiResponse<TicketResponse>> {
        return flow {
            try {
                val response = apiService.updateTicket(ticketId, ticketResponse)

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getServiceXDay(serviceId: Int, dayId: Int): Flow<ApiResponse<List<ServiceXDayResponse>>> {
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
    suspend fun getTicketByStatus(customerId: Int, ticketStatus: String): Flow<ApiResponse<List<TicketWithServiceResponse>>> {
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
}