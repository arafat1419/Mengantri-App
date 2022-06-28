package com.arafat1419.mengantri_app.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
data class ServiceCountDomain(
    val services: @RawValue ServiceDomain,
    val total: Int? = null,
    val served: Int? = null,
    val waiting: Int? = null,
    val cancel: Int? = null
) : Parcelable