package com.vision.exercise.vision.util

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import com.vision.exercise.R
import java.text.DateFormat
import java.text.SimpleDateFormat

object DateFormatUtils {
    private const val APP_DATE_FORMAT_PATTERN = "dd MMM yyyy"

    fun formatDate(timeInMillis: Long): String {
        return SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(timeInMillis)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDateTime(timeInMillis: Long): String =
        SimpleDateFormat(APP_DATE_FORMAT_PATTERN).format(timeInMillis)

    fun getDateFormatOn(timeInMillis: Long, context: Context): String {
        if (DateUtils.isToday(timeInMillis)) {
            return getTodayFormat(timeInMillis, context)
        } else if (isYesterday(timeInMillis)) {
            return getYesterdayFormat(timeInMillis, context)
        }
        return SimpleDateFormat.getDateInstance(DateFormat.MEDIUM).format(timeInMillis)
    }

    private fun isYesterday(timeInMillis: Long): Boolean =
        DateUtils.isToday(timeInMillis + DateUtils.DAY_IN_MILLIS)

    private fun getTodayFormat(timeInMillis: Long, context: Context) =
        context.getString(R.string.today_format) + SimpleDateFormat.getDateInstance(DateFormat.MEDIUM)
            .format(timeInMillis)

    private fun getYesterdayFormat(timeInMillis: Long, context: Context) =
        context.getString(R.string.yesterday_format) + SimpleDateFormat.getDateInstance(DateFormat.MEDIUM)
            .format(timeInMillis)
}
