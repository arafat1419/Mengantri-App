package com.arafat1419.mengantri_app.core.di

import com.arafat1419.mengantri_app.core.BuildConfig
import com.arafat1419.mengantri_app.core.data.DataRepository
import com.arafat1419.mengantri_app.core.data.remote.RemoteDataSource
import com.arafat1419.mengantri_app.core.data.remote.api.ApiService
import com.arafat1419.mengantri_app.core.domain.repository.IDataRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_MENGANTRI)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { RemoteDataSource(get()) }
    single<IDataRepository> {
        DataRepository(get())
    }
}