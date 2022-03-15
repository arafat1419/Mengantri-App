package com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse

import com.google.gson.annotations.SerializedName

data class ProvinceResponse(
	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("nama")
	val provinceName: String? = null
)
