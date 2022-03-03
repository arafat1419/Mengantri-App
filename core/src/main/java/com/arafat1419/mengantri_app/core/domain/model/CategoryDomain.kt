package com.arafat1419.mengantri_app.core.domain.model

import com.google.gson.annotations.SerializedName

data class CategoryDomain(
    val categoryId: Int? = null,
    val categoryName: String? = null,
    val categoryImage: String? = null,
    val categoryStatus: Int? = null
)
