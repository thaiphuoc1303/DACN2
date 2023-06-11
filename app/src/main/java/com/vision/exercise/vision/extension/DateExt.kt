package com.vision.exercise.vision.extension

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

internal fun Date.getDayOfWeek(): String {
    val format = SimpleDateFormat("EEEE", Locale.getDefault())
    return format.format(this)
}

internal fun Date.convertCurrentDateToChartDate(dataFormat:String): String {
    var day: String
    Calendar.getInstance().apply {
        time = this@convertCurrentDateToChartDate
        day = SimpleDateFormat(dataFormat, Locale.ENGLISH).apply {
            timeZone = TimeZone.getDefault()
        }.format(time)
    }
    return day
}
