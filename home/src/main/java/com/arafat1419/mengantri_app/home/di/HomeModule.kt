package com.arafat1419.mengantri_app.home.di

import com.arafat1419.mengantri_app.home.ui.companies.CompaniesViewModel
import com.arafat1419.mengantri_app.home.ui.detail.detailservice.DetailServiceViewModel
import com.arafat1419.mengantri_app.home.ui.detail.detailticket.DetailTicketViewModel
import com.arafat1419.mengantri_app.home.ui.home.HomeViewModel
import com.arafat1419.mengantri_app.home.ui.services.ServicesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@FlowPreview
val homeModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { CompaniesViewModel(get()) }
    viewModel { ServicesViewModel(get()) }
    viewModel { DetailServiceViewModel(get()) }
    viewModel { DetailTicketViewModel(get()) }
}