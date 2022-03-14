package com.arafat1419.mengantri_app.company.di

import com.arafat1419.mengantri_app.company.register.RegisterStatusViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@FlowPreview
val companyModule = module {
    viewModel { RegisterStatusViewModel(get()) }
}