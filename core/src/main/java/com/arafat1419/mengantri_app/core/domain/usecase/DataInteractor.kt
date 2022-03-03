package com.arafat1419.mengantri_app.core.domain.usecase

import com.arafat1419.mengantri_app.core.domain.model.CustomerDomain
import com.arafat1419.mengantri_app.core.domain.repository.IDataRepository
import kotlinx.coroutines.flow.Flow

class DataInteractor(private val iDataRepository: IDataRepository): DataUseCase {
    override fun getLogin(customerEmail: String): Flow<List<CustomerDomain>> =
        iDataRepository.getLogin(customerEmail)
}