package com.arafat1419.mengantri_app.di

import com.arafat1419.mengantri_app.core.domain.usecase.DataInteractor
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module

val useCaseModule = module {
    factory<DataUseCase> { DataInteractor(get()) }
}

@ExperimentalCoroutinesApi
@FlowPreview
val viewModelModule = module {
}