package com.arafat1419.mengantri_app.company.di

import com.arafat1419.mengantri_app.company.home.CompanyHomeViewModel
import com.arafat1419.mengantri_app.company.profile.CompanyProfileViewModel
import com.arafat1419.mengantri_app.company.register.RegisterStatusViewModel
import com.arafat1419.mengantri_app.company.services.CompanyServicesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@FlowPreview
val companyModule = module {
    viewModel { RegisterStatusViewModel(get()) }
    viewModel { CompanyProfileViewModel(get()) }
    viewModel { CompanyHomeViewModel(get()) }
    viewModel { CompanyServicesViewModel(get()) }
}