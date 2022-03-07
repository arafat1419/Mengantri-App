package com.arafat1419.mengantri_app.core.data.remote.api

import com.arafat1419.mengantri_app.core.data.remote.response.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // -- LOGIN MODULE --
    @GET("items/customer")
    suspend fun getLogin(
        @Query("filter[customer_status]") customerStatus: Int = 1,
        @Query("filter[customer_email]") customerEmail: String
    ): ListResponse<CustomerResponse>

    @POST("items/customer")
    suspend fun postRegistration(
        @Body customerResponse: CustomerResponse
    ): DataResponse<CustomerResponse>

    // -- HOME MODULE --
    @GET("/items/category")
    suspend fun getCategories(
        @Query("filter[category_status]") categoryStatus: Int = 1
    ): ListResponse<CategoryResponse>

    @GET("/items/company")
    suspend fun getCompanies(
        @Query("filter[company_status]") companyStatus: Int = 1,
        @Query("filter[category_id]") categoryId: Int
    ): ListResponse<CompanyResponse>

    @GET("/items/service")
    suspend fun getServices(
        @Query("filter[service_status]") serviceStatus: Int = 1,
        @Query("filter[company_id]") companyId: Int
    ): ListResponse<ServiceResponse>

    @GET("/items/ticket")
    suspend fun getTickets(
        @Query("filter[service_id]") serviceId: Int,
        @Query("filter[ticket_date][_gt]") ticketDate: String
    ): ListResponse<TicketResponse>

    @GET("/items/ticket")
    suspend fun getTicketServed(
        @Query("filter[service_id]") serviceId: Int,
        @Query("filter[ticket_date][_gt]") ticketDate: String,
        @Query("fields") fields: String,
        @Query("filter[ticket_status]") ticketStatus: String = "success",
        @Query("meta") metType: String = "filter_count"
    ): MetaResponse<CountResponse>
}