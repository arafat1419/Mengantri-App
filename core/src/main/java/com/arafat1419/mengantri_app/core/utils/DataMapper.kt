package com.arafat1419.mengantri_app.core.utils

import com.arafat1419.mengantri_app.core.BuildConfig
import com.arafat1419.mengantri_app.core.data.remote.response.CategoryResponse
import com.arafat1419.mengantri_app.core.data.remote.response.CompanyResponse
import com.arafat1419.mengantri_app.core.data.remote.response.CustomerResponse
import com.arafat1419.mengantri_app.core.domain.model.CategoryDomain
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain
import com.arafat1419.mengantri_app.core.domain.model.CustomerDomain

object DataMapper {

    // Get image from Directus
    private const val imageDirectus = "${BuildConfig.BASE_URL_MENGANTRI}assets/"

    // -- Customer Response To Domain --
    fun customerResponseToDomain(input: List<CustomerResponse>): List<CustomerDomain> =
        input.map {
            CustomerDomain(
                it.customerId,
                it.customerName,
                it.customerEmail,
                it.customerPassword,
                it.customerPhone,
                it.customerLocation,
                it.customerStatus,
                it.customerDateCreated,
                it.customerDateUpdated,
            )
        }

    fun customerResponseToDomain(input: CustomerResponse): CustomerDomain =
        CustomerDomain(
            input.customerId,
            input.customerName,
            input.customerEmail,
            input.customerPassword,
            input.customerPhone,
            input.customerLocation,
            input.customerStatus,
            input.customerDateCreated,
            input.customerDateUpdated,
        )

    // -- Category Response To Domain --
    fun categoryResponseToDomain(input: List<CategoryResponse>): List<CategoryDomain> =
        input.map {
            CategoryDomain(
                it.categoryId,
                it.categoryName,
                imageDirectus + it.categoryImage,
                it.categoryStatus
            )
        }

    // -- Company Response To Domain --
    fun companyResponseToDomain(input: List<CompanyResponse>): List<CompanyDomain> =
        input.map {
            CompanyDomain(
                it.companyId,
                it.customerId,
                it.companyName,
                it.companyPhone,
                it.companyBanner,
                it.companyImage,
                it.categoryId,
                it.companyAddress,
                it.companyCity,
                it.companyDistrics,
                it.companyStatus,
                it.companyDateCreated,
                it.companyDateUpdated,
                it.companyOpenTime,
                it.companyCloseTime,
                it.companyExpiredTime
            )
        }
}