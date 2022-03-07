package com.arafat1419.mengantri_app.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryDomain(
    val categoryId: Int? = null,
    val categoryName: String? = null,
    val categoryImage: String? = null,
    val categoryStatus: Int? = null
): Parcelable
