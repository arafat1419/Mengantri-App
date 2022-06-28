package com.arafat1419.mengantri_app.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class CategoryResponse(

    @field:SerializedName("category_id")
    val categoryId: Int? = null,

    @field:SerializedName("category_name")
    val categoryName: String? = null,

    @field:SerializedName("category_image")
    val categoryImage: String? = null,

    @field:SerializedName("category_status")
    val categoryStatus: Int? = null
)
