package com.vision.exercise.vision.extension

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Build
import android.view.View

fun View.setVisible(visible: Boolean, invisible: Int = View.GONE) {
    visibility = if (visible) View.VISIBLE else invisible
}

fun View.setBackgroundTint(color: Int) {
    // API 21 doesn't support this
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
        background?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    backgroundTintList = ColorStateList.valueOf(color)
}
