package com.arafat1419.mengantri_app.home.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@ObsoleteCoroutinesApi
@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel(private val dataUseCase: DataUseCase) : ViewModel() {
    val keywordChannel = ConflatedBroadcastChannel<String>()

    val companyResult = keywordChannel.asFlow()
        .debounce(300)
        .distinctUntilChanged()
        .filter { it.trim().isNotEmpty() }
        .flatMapLatest { dataUseCase.getSearchCompanies(it) }.asLiveData()

    val serviceResult = keywordChannel.asFlow()
        .debounce(300)
        .distinctUntilChanged()
        .filter { it.trim().isNotEmpty() }
        .flatMapLatest { dataUseCase.getSearchServices(it) }.asLiveData()
}