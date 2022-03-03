package com.arafat1419.mengantri_app.core.data

import com.arafat1419.mengantri_app.core.data.remote.RemoteDataSource
import com.arafat1419.mengantri_app.core.domain.repository.IDataRepository

class DataRepository(private val remoteDataSource: RemoteDataSource): IDataRepository {
}