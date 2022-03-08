package com.arafat1419.mengantri_app.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object DateHelper {
    private const val formatShow = "EEEE, dd-MM-y"
    private const val formatPost = "y-MM-dd"

    fun updateLabel(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat(formatShow, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun returnLabel(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat(formatPost, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toUpdateLabel(dateString: String): String {
        val date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE)
        val calendar = Calendar.getInstance()
        calendar.set(date.year, date.monthValue, date.dayOfMonth)
        return updateLabel(calendar)
    }
}