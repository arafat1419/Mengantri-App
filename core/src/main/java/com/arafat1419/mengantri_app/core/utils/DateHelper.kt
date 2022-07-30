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
        val dateMonth = dateString.substring(5..6).toInt() - 1 // Bug
        val dateDay = dateString.substring(8..9).toInt()

        val calendar = Calendar.getInstance()
        calendar.set(dateYear, dateMonth, dateDay)
        return updateLabel(calendar)
    }

    fun getTodayDateShow(): String {
        val dateFormat = SimpleDateFormat(formatShow)
        return dateFormat.format(Date())
    }

    fun getTodayDatePost(): String {
        val dateFormat = SimpleDateFormat(formatPost)
        return dateFormat.format(Date())
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
        val timeHours = timeUnderTen(timeString.substring(0..1))
        val timeMinutes = timeUnderTen(timeString.substring(3..4))
        val timeSeconds = timeUnderTen(timeString.substring(6..7))

        return mapOf(
            HOURS to timeHours,
            MINUTES to timeMinutes,
            SECONDS to timeSeconds
        )
    }

    private fun timeUnderTen(timeString: String): Int {
        return if (timeString.toInt() < 10) timeString[1].toString().toInt() else timeString.toInt()
    }

    fun timeUnderTen(timeInt: Int): String {
        return if (timeInt < 10) "0$timeInt" else timeInt.toString()
    }
}