package com.arafat1419.mengantri_app.core.data.remote.response

data class ServiceCountResponse(
    val services: ServiceResponse,
    val total: Int? = null,
    val served: Int? = null,
    val waiting: Int? = null,
    val cancel: Int? = null
)