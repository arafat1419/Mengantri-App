package com.arafat1419.mengantri_app.core.domain.usecase

import com.arafat1419.mengantri_app.core.domain.model.CustomerDomain
import kotlinx.coroutines.flow.Flow

interface DataUseCase {
    fun getLogin(customerEmail: String): Flow<List<CustomerDomain>>
}