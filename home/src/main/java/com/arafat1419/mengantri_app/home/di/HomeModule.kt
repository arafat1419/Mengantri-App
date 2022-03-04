package com.arafat1419.mengantri_app.home.di

import com.arafat1419.mengantri_app.home.ui.companies.CompaniesViewModel
import com.arafat1419.mengantri_app.home.ui.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@FlowPreview
val homeModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { CompaniesViewModel(get()) }
}