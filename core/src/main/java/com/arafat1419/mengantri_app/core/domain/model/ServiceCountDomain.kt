package com.arafat1419.mengantri_app.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
data class ServiceCountDomain(
    val services: @RawValue ServiceDomain,
    val count: Int? = null
) : Parcelable