package com.arafat1419.mengantri_app.core.data.remote.response.provinceresponse

import com.google.gson.annotations.SerializedName

data class CityResponse(
	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("id_provinsi")
	val idProvince: String? = null,

	@field:SerializedName("nama")
	val cityName: String? = null
)
