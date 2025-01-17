package com.arafat1419.mengantri_app.core.data.remote.api

import com.arafat1419.mengantri_app.core.data.remote.response.*
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.ListCity
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.ListDistrics
import com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse.ListProvince
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    // -- LOGIN MODULE --
    @GET("items/customer")
    suspend fun getLogin(
        @Query("filter[customer_email]") customerEmail: String
    ): ListResponse<CustomerResponse>

    @POST("utils/hash/verify")
    suspend fun checkHash(
        @Body rawMap: Map<String, String>
    ): DataResponse<Boolean>

    @POST("items/customer")
    suspend fun postRegistration(
        @Body customerResponse: CustomerResponse
    ): DataResponse<CustomerResponse>

    @PATCH("items/customer/{customer_id}")
    suspend fun patchCustomer(
        @Path("customer_id") customerId: Int,
        @Body customerResponse: CustomerResponse
    ): DataResponse<CustomerResponse>

    // -- HOME MODULE --
    @GET("items/category")
    suspend fun getCategories(
        @Query("filter[category_status]") categoryStatus: Int = 1
    ): ListResponse<CategoryResponse>

    @GET("items/company")
    suspend fun getCompanies(
        @Query("filter[company_status]") companyStatus: Int = 1,
        @Query("filter[category_id]") categoryId: Int
    ): ListResponse<CompanyResponse>

    @GET("items/company")
    suspend fun getSearchCompanies(
        @Query("search") keyword: String,
        @Query("filter[company_status]") companyStatus: Int = 1
    ): ListResponse<CompanyResponse>

    @GET("items/company")
    suspend fun getSearchCompaniesByCategory(
        @Query("search") keyword: String,
        @Query("filter[category_id]") categoryId: Int,
        @Query("filter[company_status]") companyStatus: Int = 1
    ): ListResponse<CompanyResponse>

    @GET("items/service")
    suspend fun getServices(
        @Query("filter[company_id]") companyId: Int,
        @Query("fields") fields: String = "*,company_id.company_id,company_id.company_name"
    ): ListResponse<ServiceResponse>

    @GET("items/ticket")
    suspend fun getTickets(
        @Query("filter[service_id]") serviceId: Int,
        @Query("filter[ticket_date]") ticketDate: String,
    ): ListResponse<TicketResponse>

    @GET("items/ticket")
    suspend fun getTicketServed(
        @Query("filter[service_id]") serviceId: Int,
        @Query("filter[ticket_date][_gt]") ticketDate: String,
        @Query("fields") fields: String,
        @Query("filter[ticket_status]") ticketStatus: String = "success",
        @Query("meta") metType: String = "filter_count"
    ): MetaResponse<CountResponse>

    @POST("items/ticket")
    suspend fun postTicket(
        @Body ticketResponse: TicketResponse
    ): DataResponse<TicketResponse>

    @GET("items/ticket")
    suspend fun getTicket(
        @Query("filter[ticket_id]") ticketId: Int,
        @Query("fields") fields: String = "*,service_id.*,service_id.company_id.company_id,service_id.company_id.company_name"
    ): ListResponse<TicketWithServiceResponse>

    @PATCH("items/ticket/{ticket_id}")
    suspend fun updateTicket(
        @Path("ticket_id") ticketId: Int,
        @Body ticketResponse: TicketResponse
    ): DataResponse<TicketResponse>

    @GET("items/service_x_day")
    suspend fun getServiceXDay(
        @Query("filter[service_id]") serviceId: Int,
        @Query("filter[day_id]") dayId: Int
    ): ListResponse<ServiceXDayResponse>

    // -- TICKET MODULE --
    @GET("items/ticket")
    suspend fun getTicketByStatus(
        @Query("filter[customer_id]") customerId: Int,
        @Query("filter[ticket_status]") ticketStatus: String,
        @Query("fields") fields: String = "*,service_id.*,service_id.company_id.company_id,service_id.company_id.company_name"
    ): ListResponse<TicketWithServiceResponse>

    // -- COMPANY MODULE --
    @GET("items/company")
    suspend fun getUserCompany(
        @Query("filter[customer_id]") customerId: Int
    ): ListResponse<CompanyResponse>

    @Multipart
    @POST("files")
    suspend fun postUploadFile(
        @Part("filename") fileName: RequestBody,
        @Part("folder") folder: RequestBody,
        @Part file: MultipartBody.Part
    ): DataResponse<UploadFileResponse>

    @POST("items/company")
    suspend fun postCompany(
        @Body companyResponse: CompanyResponse
    ): DataResponse<CompanyResponse>

    @GET("items/ticket")
    suspend fun getTicketsSoon(
        @Query("filter[service_id]") serviceId: Int,
        @Query("filter[ticket_date][_gt]") ticketDate: String
    ): ListResponse<TicketResponse>

    @GET("items/ticket")
    suspend fun getTicketsByService(
        @Query("filter[service_id]") serviceId: Int
    ): ListResponse<TicketResponse>

    @POST("items/service")
    suspend fun postService(
        @Body serviceOnlyResponse: ServiceOnlyResponse
    ): DataResponse<ServiceOnlyResponse>

    @PATCH("items/service/{service_id}")
    suspend fun updateService(
        @Path("service_id") serviceId: Int,
        @Body serviceOnlyResponse: ServiceOnlyResponse
    ): DataResponse<ServiceOnlyResponse>

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