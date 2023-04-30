package com.vision.exercise.vision.extension

import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.util.Log
import android.view.View
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

const val TAG = "xxxx"

internal fun String.createSpannable(
    textSizeFirst: Int,
    textSizeSecond: Int,
    color: Int,
    count: Int
): SpannableString {
    val spannable = SpannableString(this)
    val firstSpannable = setSpanState(color, textSizeFirst.toFloat())
    val secondSpannable = setSpanState(color, textSizeSecond.toFloat())

    spannable.setSpan(firstSpannable, 0, this.length - count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannable.setSpan(
        secondSpannable,
        this.length - count,
        this.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannable
}

internal fun String.createSpannableExtand(
    textSizeNormal: Int,
    textSizeLager: Int,
    color: Int,
    countFirst: Int,
    countSecond: Int,
    countThird: Int
): SpannableString {
    val spannable = SpannableString(this)
    val normalSpannableFirst = setSpanState(color, textSizeNormal.toFloat())
    val normalSpannableSecond = setSpanState(color, textSizeNormal.toFloat())
    val largerSpannableFirst = setSpanState(color, textSizeLager.toFloat())
    val largerSpannableSecond = setSpanState(color, textSizeLager.toFloat())

    spannable.setSpan(largerSpannableFirst, 0, countFirst, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannable.setSpan(
        normalSpannableFirst,
        countFirst,
        countSecond,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannable.setSpan(
        largerSpannableSecond,
        countSecond,
        countThird,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannable.setSpan(
        normalSpannableSecond,
        countThird,
        this.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannable
}

internal fun String.convertStringDateToAnotherFormat(
    newPattern: String,
    oldPatten: String
): String {
    try {
        val dfNew = SimpleDateFormat(newPattern, Locale.getDefault())
        val dfOld = SimpleDateFormat(oldPatten, Locale.getDefault())
        return dfNew.format(dfOld.parse(this))
    } catch (e: Exception) {
        Log.e(TAG, e.localizedMessage)
    }
    return ""
}

internal fun String.convertStringDateToDateFormat(pattern: String): Date {
    try {
        return SimpleDateFormat(pattern, Locale.getDefault()).parse(this)
    } catch (e: Exception) {
        Log.e(TAG, e.localizedMessage)
    }
    return Date(System.currentTimeMillis())
}

internal fun String.convertFormatRoundNumberToString() = this.replace(Regex("\\D"), "")

internal fun String.setMultiTextSizeForTitleChart(
    textSize: Int,
    textSuffix: String,
    textSuffixTextSize: Int,
    color: Int,
    imageResource: Drawable? = null
): SpannableString {
    var spannable = SpannableString(this.plus(textSuffix))
    val normalSpannable = setSpanState(color, textSize.toFloat())
    val textSuffixSpannable = setSpanState(color, textSuffixTextSize.toFloat())

    if (imageResource != null) {
        spannable = SpannableString("    ".plus(this))
        imageResource.setBounds(0, 0, imageResource.intrinsicWidth, imageResource.intrinsicHeight)
        val imageSpan = ImageSpan(imageResource, ImageSpan.ALIGN_BOTTOM)
        spannable.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(normalSpannable, 4, this.length + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    } else {
        spannable.setSpan(normalSpannable, 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(
            textSuffixSpannable,
            this.length,
            spannable.toString().length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return spannable
}

private fun setSpanState(color: Int, textSizeLager: Float) = object : ClickableSpan() {
    override fun onClick(widget: View) {
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
        ds.color = color
        ds.textSize = textSizeLager
    }
}

internal fun String.isEmailValid(): Boolean {
    val regex =
        "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?!-)(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{1,6}$"
    val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
    return pattern.matcher(this).matches()
}
