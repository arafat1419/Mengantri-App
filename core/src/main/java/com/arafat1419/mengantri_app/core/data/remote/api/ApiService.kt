package com.arafat1419.mengantri_app.core.data.remote.api

import com.arafat1419.mengantri_app.core.BuildConfig
import com.arafat1419.mengantri_app.core.data.remote.response.*
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.ListCity
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.ListDistrics
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.ListProvince
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // -- CATEGORY --
    @GET("items/category")
    suspend fun getCategories(
        @Query("filter[category_status]") categoryStatus: Int = 1,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): ListResponse<CategoryResponse>

    // -- COMPANY --
    @GET("items/company")
    suspend fun getNewestCompanies(
        @Query("sort") sort: String = "-company_date_created",
        @Query("filter[company_status]") companyStatus: Int = 1,
        @Query("limit") limit: Int = 6,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): ListResponse<CompanyResponse>

    @GET("items/company")
    suspend fun getCustomerCompany(
        @Query("filter[customer_id]") customerId: Int,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): ListResponse<CompanyResponse>

    @GET("items/company")
    suspend fun getCompaniesByCategory(
        @Query("filter[company_status]") companyStatus: Int = 1,
        @Query("filter[category_id]") categoryId: Int,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): ListResponse<CompanyResponse>

    @GET("items/company/{company_id}")
    suspend fun getCompany(
        @Path("company_id") companyId: Int,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<CompanyResponse>

    @GET("items/company")
    suspend fun getSearchCompanies(
        @Query("search") keyword: String,
        @Query("filter[company_status]") companyStatus: Int = 1,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): ListResponse<CompanyResponse>

    @POST("items/company")
    suspend fun postCompany(
        @Body companyResponse: CompanyResponse,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<CompanyResponse>

    @PATCH("items/company/{company_id}")
    suspend fun updateCompany(
        @Path("company_id") companyId: Int,
        @Body companyResponse: CompanyResponse,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<CompanyResponse>

    // -- SERVICE --
    @GET("custom-endpoint/service_counted")
    suspend fun getServicesCountByCompany(
        @Query("company_id") companyId: Int,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): ListResponse<ServiceCountResponse>

    @GET("custom-endpoint/service_counted/{service_id}")
    suspend fun getServiceCount(
        @Path("service_id") serviceId: Int,
        @Query("ticket_date") ticketDate: String?,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<ServiceCountResponse>

    @GET("custom-endpoint/estimated_service/{service_id}")
    suspend fun getServiceEstimated(
        @Path("service_id") serviceId: Int,
        @Query("ticket_date") ticketDate: String,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<EstimatedTimeResponse>

    @GET("custom-endpoint/search_service")
    suspend fun getSearchServices(
        @Query("search") keyword: String,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): ListResponse<ServiceCountResponse>

    @GET("items/service")
    suspend fun getServicesByCompany(
        @Query("filter[company_id]") companyId: Int,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): ListResponse<ServiceResponse>

    @GET("items/service/{service_id}")
    suspend fun getService(
        @Path("service_id") serviceId: Int,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<ServiceResponse>

    @POST("items/service")
    suspend fun postService(
        @Body serviceResponse: ServiceResponse,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<ServiceResponse>

    @PATCH("items/service/{service_id}")
    suspend fun updateService(
        @Path("service_id") serviceId: Int,
        @Body serviceResponse: ServiceResponse,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<ServiceResponse>

    @DELETE("items/service/{service_id}")
    suspend fun deleteService(
        @Path("service_id") serviceId: Int,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): Response<String>

    // -- TICKET --
    @GET("custom-endpoint/estimated_ticket/{ticket_id}")
    suspend fun getTicketServiceDetail(
        @Path("ticket_id") ticketId: Int,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<TicketDetailResponse>

    @GET("custom-endpoint/tickets_waiting")
    suspend fun getTicketsWaiting(
        @Query("customer_id") customerId: Int,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): ListResponse<TicketDetailResponse>

    @GET("custom-endpoint/tickets_history")
    suspend fun getTicketsHistory(
        @Query("customer_id") customerId: Int?,
        @Query("service_id") serviceId: Int?,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): ListResponse<TicketDetailResponse>

    @GET("custom-endpoint/tickets_today")
    suspend fun getTicketsToday(
        @Query("service_id") serviceId: Int?,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): ListResponse<TicketDetailResponse>

    @GET("custom-endpoint/tickets_soon")
    suspend fun getTicketsSoon(
        @Query("service_id") serviceId: Int?,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): ListResponse<TicketDetailResponse>

    @POST("items/ticket")
    suspend fun postTicket(
        @Body ticketResponse: TicketResponse,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<TicketResponse>

    @PATCH("items/ticket/{ticket_id}")
    suspend fun updateTicket(
        @Path("ticket_id") ticketId: Int,
        @Body ticketResponse: TicketResponse,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<TicketResponse>

    // -- CUSTOMER --
    @GET("items/customer/{customer_id}")
    suspend fun getCustomer(
        @Path("customer_id") customerId: Int,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<CustomerResponse>

    @GET("items/customer")
    suspend fun getLogin(
        @Query("filter[customer_email]") customerEmail: String,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): ListResponse<CustomerResponse>

    @POST("items/customer")
    suspend fun postRegistration(
        @Body customerResponse: CustomerResponse,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<CustomerResponse>

    @PATCH("items/customer/{customer_id}")
    suspend fun patchCustomer(
        @Path("customer_id") customerId: Int,
        @Body customerResponse: CustomerResponse,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<CustomerResponse>

    // -- FILES --
    @Multipart
    @POST("files")
    suspend fun postUploadFile(
        @Part("filename") fileName: RequestBody,
        @Part("folder") folder: RequestBody,
        @Part file: MultipartBody.Part,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<UploadFileResponse>

    // -- UTILS --
    @POST("utils/hash/verify")
    suspend fun checkHash(
        @Body rawMap: Map<String, String>,
        @Query("access_token") accessToken: String = BuildConfig.ACCESS_TOKEN
    ): DataResponse<Boolean>

    // -- PROVINCE, CITY, DISTRICS --
    @GET
    suspend fun getProvinces(@Url url: String): ListProvince

    @GET
    suspend fun getCities(
        @Url url: String,
        @Query("id_provinsi") idProvince: String
    ): ListCity

    @GET
    suspend fun getDistrics(
        @Url url: String,
        @Query("id_kota") idCity: String
    ): ListDistrics
}