package com.arafat1419.mengantri_app.core.data.remote.api

import com.arafat1419.mengantri_app.core.data.remote.response.CustomerResponse
import com.arafat1419.mengantri_app.core.data.remote.response.DataResponse
import com.arafat1419.mengantri_app.core.data.remote.response.ListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("items/customer")
    suspend fun getLogin(
        @Query("filter[customer_status]") customerStatus: Int = 1,
        @Query("filter[customer_email]") customerEmail: String
    ) : ListResponse<CustomerResponse>

    @POST("items/customer")
    suspend fun postRegistration(
        @Body customerResponse: CustomerResponse
    ) : DataResponse<CustomerResponse>
}