package com.arafat1419.mengantri_app.core.domain.repository

import com.arafat1419.mengantri_app.core.domain.model.CustomerDomain
import kotlinx.coroutines.flow.Flow

interface IDataRepository {
    fun getLogin(customerEmail: String): Flow<List<CustomerDomain>>
}