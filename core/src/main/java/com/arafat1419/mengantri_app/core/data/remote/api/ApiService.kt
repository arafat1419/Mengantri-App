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
    ) : ListResponse<CustomerResponse>

    @POST("items/customer")
    suspend fun postRegistration(
        @Body customerResponse: CustomerResponse
    ) : DataResponse<CustomerResponse>

    // -- HOME MODULE --
    @GET("/items/category")
    suspend fun getCategories(
        @Query("filter[category_status]") categoryStatus: Int = 1,
        @Query("limit") limit: Int = 5
    ) : ListResponse<CategoryResponse>

    @GET("/items/companies")
    suspend fun getCompanies(
        @Query("filter[company_status]") companyStatus: Int = 1,
        @Query("filter[category_id]") categoryId: Int
    ) : ListResponse<CompanyResponse>
}