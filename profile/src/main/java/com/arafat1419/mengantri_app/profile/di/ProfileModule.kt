package com.arafat1419.mengantri_app.profile.di

import com.arafat1419.mengantri_app.profile.ui.ProfileViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@FlowPreview
val profileModule = module {
    viewModel { ProfileViewModel(get()) }
}