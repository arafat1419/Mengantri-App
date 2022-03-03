package com.arafat1419.mengantri_app.home.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arafat1419.mengantri_app.core.domain.model.CategoryDomain
import com.arafat1419.mengantri_app.core.domain.usecase.DataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel(private val dataUseCase: DataUseCase): ViewModel() {
    fun getCategories(): LiveData<List<CategoryDomain>> =
        dataUseCase.getCategories().asLiveData()
}