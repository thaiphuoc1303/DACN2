package com.vision.exercise.vision.util

import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeUtils {
    private const val HOUR_FORMAT = "%02d:%02d:%02d"
    private const val MINUTE_FORMAT = "%02d:%02d"
    private const val SECOND_FORMAT = "00:%02d"
    private const val DAY_OF_WEEK_FORMAT = "EEE"
    const val EMPTY_TIME = "00:00"

    private const val HOUR = 1000L * 60 * 60
    private const val MINUTE = 1000L * 60
    private const val DAY = HOUR * 24

    fun getTimeFromMilli(milli: Long): String {
        if (isWholeHour(milli)) {
            val h = TimeUnit.MILLISECONDS.toHours(milli)
            val m = TimeUnit.MILLISECONDS.toMinutes(milli) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milli))
            val s = TimeUnit.MILLISECONDS.toSeconds(milli) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milli))
            return String.format(HOUR_FORMAT, h, m, s)
        } else if (isWholeMinute(milli)) {
            val m = TimeUnit.MILLISECONDS.toMinutes(milli) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milli))
            val s = TimeUnit.MILLISECONDS.toSeconds(milli) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milli))
            return String.format(MINUTE_FORMAT, m, s)
        } else if (isLessThenAMinute(milli)) {
            val s = TimeUnit.MILLISECONDS.toSeconds(milli) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milli))
            return String.format(SECOND_FORMAT, s)
        }
        return EMPTY_TIME
    }
    fun getTimeFromSecond(second: Long): String {
        return getTimeFromMilli(second * 1000)
    }
    private fun isWholeHour(timeInMilliseconds: Long): Boolean {
        return timeInMilliseconds >= HOUR
    }

    private fun isWholeMinute(timeInMilliseconds: Long): Boolean {
        return timeInMilliseconds in MINUTE until HOUR
    }

    private fun isLessThenAMinute(timeInMilliseconds: Long): Boolean {
        return timeInMilliseconds < MINUTE
    }

    fun getPrettyTime(time: Long): String {
        val prettyTime = PrettyTime(Locale.ENGLISH)
        return prettyTime.format(Date(time))
    }

    fun getStringDayOfWeek(calendar: Calendar): String {
        val sdf = SimpleDateFormat(DAY_OF_WEEK_FORMAT, Locale.ENGLISH)
        val date = Date(calendar.timeInMillis)
        return sdf.format(date)
    }
}