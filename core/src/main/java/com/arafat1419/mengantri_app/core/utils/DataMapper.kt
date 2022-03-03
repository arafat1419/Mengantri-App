package com.arafat1419.mengantri_app.core.utils

import com.arafat1419.mengantri_app.core.data.remote.response.CustomerResponse
import com.arafat1419.mengantri_app.core.domain.model.CustomerDomain

object DataMapper {
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
}