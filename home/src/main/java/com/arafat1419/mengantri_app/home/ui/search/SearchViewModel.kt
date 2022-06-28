package com.arafat1419.mengantri_app.home.ui.search

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
    var categoryId: Int? = null

    val companyResult = keywordChannel.asFlow()
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .flatMapLatest {
            if (categoryId == null) {
                dataUseCase.getSearchCompanies(it)
            } else {
                dataUseCase.getSearchCompaniesByCategory(it, categoryId!!)
            }
        }.asLiveData()
}