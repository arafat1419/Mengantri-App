package com.arafat1419.mengantri_app.login.di

import com.arafat1419.mengantri_app.login.ui.login.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@FlowPreview
val loginModule = module {
    viewModel { LoginViewModel(get()) }
}