package com.arafat1419.mengantri_app.core.utils

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    fun updateLabel(calendar: Calendar): String {
        val myFormat = "EEEE, dd-MM-y"
        val dateFormat = SimpleDateFormat(myFormat, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun returnLabel(calendar: Calendar): String {
        val myFormat = "y-MM-dd"
        val dateFormat = SimpleDateFormat(myFormat, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}