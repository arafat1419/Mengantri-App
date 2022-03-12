package com.arafat1419.mengantri_app.core.utils

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    private const val formatShow = "EEEE, dd-MM-y"
    private const val formatPost = "y-MM-dd"

    const val HOURS = "hours"
    const val MINUTES = "minutes"
    const val SECONDS = "seconds"

    fun updateLabel(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat(formatShow, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun returnLabel(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat(formatPost, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun toUpdateLabel(dateString: String): String {
        val dateYear = dateString.substring(0..3).toInt()
        val dateMonth = dateString.substring(5..6).toInt()
        val dateDay = dateString.substring(8..9).toInt()

        val calendar = Calendar.getInstance()
        calendar.set(dateYear, dateMonth, dateDay)
        return updateLabel(calendar)
    }

    fun toReturnLabel(dateString: String): String {
        val dateYear = dateString.substring(dateString.length - 4..dateString.length).toInt()
        val dateMonth = dateString.substring(dateString.length - 7..dateString.length - 5).toInt()
        val dateDay = dateString.substring(dateString.length - 10..dateString.length - 8).toInt()

        val calendar = Calendar.getInstance()
        calendar.set(dateYear, dateMonth, dateDay)
        return updateLabel(calendar)
    }

    fun stringTimeToInt(timeString: String): Map<String, Int> {
        val timeHours =
            if (timeString.substring(0..1) == "00") "0" else timeString.substring(0..1)
        val timeMinutes =
            if (timeString.substring(3..4) == "00") "0" else timeString.substring(3..4)
        val timeSeconds =
            if (timeString.substring(6..7) == "00") "0" else timeString.substring(6..7)

        return mapOf(
            HOURS to timeHours.toInt(),
            MINUTES to timeMinutes.toInt(),
            SECONDS to timeSeconds.toInt()
        )
    }
}