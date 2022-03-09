package com.arafat1419.mengantri_app.ticket.di

import com.arafat1419.mengantri_app.ticket.ui.TicketsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@FlowPreview
val ticketsModule = module {
    viewModel { TicketsViewModel(get()) }
}